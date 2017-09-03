package com.example.loso.friendtracker.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Controller.FriendListAdapter;
import com.example.loso.friendtracker.Model.FriendLocation;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;


public class EditMeetingActivity extends AppCompatActivity {
    private String meetingID = "";
    private MeetingController meetingController;
    private FriendListAdapter adapter;
    private static final String LOG_TAG = "EditMeetingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This activity interacts with one specific meeting
        // Grab meeting ID as passed as an extra in the intent
        meetingID = getIntent().getStringExtra("meeting");
        meetingController = new MeetingController();

        // initialise DatePickerDIalog so birthday can be selected
        setupDatePickDialog();

        EditText editTitle = (EditText) findViewById(R.id.editTextTitle);

        final Meeting meeting = meetingController.getMeeting(meetingID);
        editTitle.setText(meeting.getTitle());

        EditText etLat = (EditText) findViewById(R.id.etLatitude);
        EditText etLong = (EditText) findViewById(R.id.etLongitude);

        FriendLocation location = meetingController.getMeetingLocation(meetingID);
        if (location != null) {
            etLat.setText(Double.toString(location.getLatitude()));
            etLong.setText(Double.toString(location.getLongitude()));
        }

        TextView startDate = (TextView) findViewById(R.id.tvMeetStartDate);
        TextView endDate = (TextView) findViewById(R.id.tvMeetEndDate);
        TextView startTime = (TextView) findViewById(R.id.tvMeetStartTime);
        TextView endTime = (TextView) findViewById(R.id.tvMeetEndTime);

        String[] dates = meetingController.getStartEndStrings(meetingID);

        startDate.setText(dates[0]);
        startTime.setText(dates[1]);
        endDate.setText(dates[2]);
        endTime.setText(dates[3]);

        // display attend list
        setupAttendSection(meeting);

        ImageButton removeButton = (ImageButton) findViewById(R.id.btnRemoveMeeting);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditMeetingActivity.this)
                        .setTitle("Remove Meeting")
                        .setMessage("Do you really want to remove this meeting?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                meetingController.removeMeeting(meeting);
                                Toast.makeText(EditMeetingActivity.this, "Meeting removed", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        Button saveButton = (Button) findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean finished = true;

                TextView tvTitle = (TextView) findViewById(R.id.editTextTitle);
                String title = tvTitle.getText().toString();

                try {
                    EditText etLat = (EditText) findViewById(R.id.etLatitude);
                    EditText etLong = (EditText) findViewById(R.id.etLongitude);

                    double lati = Double.parseDouble(etLat.getText().toString());
                    double longi = Double.parseDouble(etLong.getText().toString());
                    meetingController.updateMeetingDetails(meetingID, title, lati, longi);
                } catch (NumberFormatException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

                try {
                    TextView startDate = (TextView) findViewById(R.id.tvMeetStartDate);
                    TextView startTime = (TextView) findViewById(R.id.tvMeetStartTime);

                    String[] timeStartTokens = startTime.getText().toString().split(":");
                    String[] dateStartTokens = startDate.getText().toString().split("/");

                    int year = Integer.parseInt(dateStartTokens[0]);
                    int month = Integer.parseInt(dateStartTokens[1]) - 1;
                    int day = Integer.parseInt(dateStartTokens[2]);
                    int hour = Integer.parseInt(timeStartTokens[0]);
                    int minute = Integer.parseInt(timeStartTokens[1]);

                    meetingController.setMeetingStart(meetingID, year, month, day, hour, minute);

                    TextView endDate = (TextView) findViewById(R.id.tvMeetEndDate);
                    TextView endTime = (TextView) findViewById(R.id.tvMeetEndTime);
                    String[] timeEndTokens = endTime.getText().toString().split(":");
                    String[] dateEndTokens = endDate.getText().toString().split("/");

                    year = Integer.parseInt(dateEndTokens[0]);
                    month = Integer.parseInt(dateEndTokens[1]) - 1;
                    day = Integer.parseInt(dateEndTokens[2]);
                    hour = Integer.parseInt(timeEndTokens[0]);
                    minute = Integer.parseInt(timeEndTokens[1]);

                    meetingController.setMeetingEnd(meetingID, year, month, day, hour, minute);
                } catch (MeetingController.InvalidDateException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    new AlertDialog.Builder(EditMeetingActivity.this)
                            .setTitle("Causality Broken")
                            .setMessage(e.getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.ok, null).show();
                    //Toast.makeText(EditMeetingActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    finished = false;
                } catch (NumberFormatException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

                if (finished) {
                    Toast.makeText(EditMeetingActivity.this, "Details Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    public void setupAttendSection(Meeting meeting) {
        final Meeting currentM = meeting;
        Button addAttend = (Button) findViewById(R.id.bAddAttend);

        ArrayList<Friend> friends = (ArrayList<Friend>) meeting.getFriends();
        adapter = new FriendListAdapter(getApplicationContext(), friends);
        ListView lvAttend = (ListView) findViewById(R.id.attendlist);
        lvAttend.setAdapter(adapter);
        lvAttend.setLongClickable(true);
        lvAttend.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "OnItemLongClickListener reached ok");
                final Friend friend = adapter.getItem(position);

                new AlertDialog.Builder(EditMeetingActivity.this)
                        .setTitle("Remove Attend")
                        .setMessage("Do you really want to remove this friend?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                meetingController.removeAttend(currentM, friend);
                                Toast.makeText(EditMeetingActivity.this, "Attend removed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    public void setupDatePickDialog() {
        final TextView startDate = (TextView) findViewById(R.id.tvMeetStartDate);
        final TextView endDate = (TextView) findViewById(R.id.tvMeetEndDate);
        final TextView startTime = (TextView) findViewById(R.id.tvMeetStartTime);
        final TextView endTime = (TextView) findViewById(R.id.tvMeetEndTime);


        final DateFormat sdf = SimpleDateFormat.getDateInstance();


        final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                startDate.setText(year + "/" + Integer.toString(month + 1) + "/" + day);
            }
        };

        final TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime.setText(hourOfDay + ":" + minute);
            }
        };

        final DatePickerDialog.OnDateSetListener endDateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                endDate.setText(year + "/" + Integer.toString(month + 1) + "/" + day);
            }
        };

        final TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTime.setText(hourOfDay + ":" + minute);
            }
        };

        ImageButton dateStartButton = (ImageButton) findViewById(R.id.btnStartDatePicker);
        ImageButton timeStartButton = (ImageButton) findViewById(R.id.btnStartTimePicker);
        ImageButton dateEndButton = (ImageButton) findViewById(R.id.btnEndDatePicker);
        ImageButton timeEndButton = (ImageButton) findViewById(R.id.btnEndTimePicker);

        dateStartButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();
                        int year_x = cal.get(Calendar.YEAR);
                        int month_x = cal.get(Calendar.MONTH);
                        int day_x = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                EditMeetingActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                startDateListener,
                                year_x, month_x, day_x);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }
        );

        timeStartButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);


                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                EditMeetingActivity.this,
                                startTimeListener, hour, minute, true);
                        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        timePickerDialog.show();
                    }
                }
        );

        dateEndButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();
                        int year_x = cal.get(Calendar.YEAR);
                        int month_x = cal.get(Calendar.MONTH);
                        int day_x = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                EditMeetingActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                endDateListner,
                                year_x, month_x, day_x);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }
        );

        timeEndButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);


                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                EditMeetingActivity.this,
                                endTimeListener, hour, minute, true);
                        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        timePickerDialog.show();
                    }
                }
        );
    }
}
