package com.example.loso.friendtracker.View;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.R;

import java.util.Calendar;

public class EditMeetingActivity extends AppCompatActivity {
    private String meetingID = "";
    private MeetingController meetingController;
    final static int MEETING_CHANGED = 126;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This activity interacts with one specific friend
        // Grab friend ID as passed as an extra in the intent
        meetingID = getIntent().getStringExtra("meeting");

        meetingController = new MeetingController();

        // initialise DatePickerDIalog so birthday can be selected
        setupDatePickDialog();

        EditText editTitle = (EditText) findViewById(R.id.editTextTitle);
        //EditText editLocation = (EditText) findViewById(R.id.editTextLocation);
        //TextView tvMeetingDate = (TextView) findViewById(R.id.tvMeetingDate);

        Meeting meeting = meetingController.getMeeting(meetingID);
        editTitle.setText(meeting.getTitle());
        //editLocation.setText(meeting.getLocation());
        //tvMeetingDate.setText(meeting.getStart());
        /*
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
                                //friendController.removeFriend(friendID);
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
                TextView tvTitle = (TextView) findViewById(R.id.editTextTitle);
                TextView tvLocation = (TextView) findViewById(R.id.editTextLocation);
                String title = tvTitle.getText().toString();
                String location = tvLocation.getText().toString();

                meetingController.updateMeetingDetails(meetingID, title);
                Toast.makeText(EditMeetingActivity.this, "Details Updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });*/
    }


    public void setupDatePickDialog() {
        final DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //               Toast.makeText(EditFriendActivity.this, year + "/" + month + "/" + day, Toast.LENGTH_LONG).show();
                meetingController.setMeetingDate(meetingID, year, month, day);
            }
        };

        ImageButton dateButton = (ImageButton) findViewById(R.id.btnStartDatePicker);

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final Calendar cal = Calendar.getInstance();
                        int year_x = cal.get(Calendar.YEAR);
                        int month_x = cal.get(Calendar.MONTH);
                        int day_x = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                EditMeetingActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                dpickerListener,
                                year_x, month_x, day_x);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }
        );
    }
}
