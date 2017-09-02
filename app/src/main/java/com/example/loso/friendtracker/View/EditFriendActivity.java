package com.example.loso.friendtracker.View;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.R;

import java.util.Calendar;

public class EditFriendActivity extends AppCompatActivity {
    private String friendID = "";
    private FriendController friendController;
    final static int FRIEND_CHANGED = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This activity interacts with one specific friend
        // Grab friend ID as passed as an extra in the intent
        friendID = getIntent().getStringExtra("friend");

        friendController = new FriendController();

        // initialise DatePickerDIalog so birthday can be selected
        setupDatePickDialog();

        EditText editName = (EditText) findViewById(R.id.editTextName);
        EditText editEmail = (EditText) findViewById(R.id.editTextEmail);
        TextView tvBirthday = (TextView) findViewById(R.id.tvBirthday);

        editName.setText(friendController.getFriendName(friendID));
        editEmail.setText(friendController.getFriendEmail((friendID)));
        tvBirthday.setText(friendController.getFriendBirthday(friendID));

        ImageButton removeButton = (ImageButton) findViewById(R.id.btnRemoveFriend);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditFriendActivity.this)
                        .setTitle("Remove Friend")
                        .setMessage("Do you really want to remove this friend?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                friendController.removeFriend(friendID);
                                Toast.makeText(EditFriendActivity.this, "Friend removed", Toast.LENGTH_SHORT).show();
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
                TextView tvName = (TextView) findViewById(R.id.editTextName);
                TextView tvEmail = (TextView) findViewById(R.id.editTextEmail);
                String name = tvName.getText().toString();
                String email = tvEmail.getText().toString();

                friendController.updateFriendDetails(friendID, name, email);
                Toast.makeText(EditFriendActivity.this, "Details Updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    public void setupDatePickDialog() {
        final DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //               Toast.makeText(EditFriendActivity.this, year + "/" + month + "/" + day, Toast.LENGTH_LONG).show();
                friendController.setBirthday(friendID, year, month, day);
                TextView tvBirthday = (TextView) findViewById(R.id.tvBirthday);
                tvBirthday.setText(friendController.getFriendBirthday(friendID));
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
                                EditFriendActivity.this,
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
