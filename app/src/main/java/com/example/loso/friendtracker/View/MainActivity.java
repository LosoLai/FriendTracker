package com.example.loso.friendtracker.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Database_SQLite.DatabaseHelper;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.FriendModel;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.Model.MeetingModel;
import com.example.loso.friendtracker.R;
import com.example.loso.friendtracker.Service.DataManager;
import com.example.loso.friendtracker.Service.LocationService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 17;
    private static final int PICK_CONTACTS = 100;
    private DatabaseHelper db;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //Added by LosoLai  24/09/2017
    /**
     * Database sync
     * maintain original in memory model which is synced (loaded) in onStart()
     * saved/persisted in onStop()
     */
    @Override
    protected void onStart()
    {
        Log.i(LOG_TAG, "onStart()");
        super.onStart();
//        //Add dummy data to MeetingModel
//        MeetingModel mMeetingModel = MeetingModel.getInstance();
//        FriendModel friendModel = FriendModel.getInstance();

//        db = new DatabaseHelper(this);
//        db.deleteRows();
//        // set friends
//        for(int i=0 ; i<friendModel.getFriends().size() ; i++)
//        {
//            db.addFriend(friendModel.getFriends().get(i));
//        }
//        // set meetings
//        for(int j=0 ; j<mMeetingModel.getMeetings().size() ; j++)
//        {
//            db.addMeeting(mMeetingModel.getMeetings().get(j));
//        }

        db = new DatabaseHelper(this);
        FriendModel mFriendModel = FriendModel.getInstance();
        boolean hasFriends = db.checkFriendDB(mFriendModel.getFriends());
        //Add dummy data to Model
        if(!hasFriends)
            mFriendModel.setFriends(DataManager.createDummyFriendList(getApplicationContext()));

        MeetingModel mMeetingModel = MeetingModel.getInstance();
        boolean hasMeetings = db.checkMeetingDB(mMeetingModel.getMeetings());
        //Add dummy data to Model
        if(!hasMeetings)
            mMeetingModel.setMeetings(DataManager.createDummMeetingList());
        else //setting attendlist
        {
            int size = mMeetingModel.getMeetings().size();
            for(int i=0 ; i<size ; i++)
            {
                Meeting meeting = mMeetingModel.getMeetings().get(i);
                String meetingID = meeting.getID();
                ArrayList<String> attendIDList = db.readDB_AttendListTable(meetingID);
                for(int j=0 ; j<attendIDList.size() ; j++)
                {
                    Friend attend = mFriendModel.findFriendByID(attendIDList.get(j));
                    meeting.addAttend(attend);
                }
            }
        }
    }

    @Override
    protected void onStop()
    {
        Log.i(LOG_TAG, "onStop()");
        super.onStop();
        //clean tables
        db.deleteRows();
        //saving data into db
        FriendModel mFModel = FriendModel.getInstance();
        MeetingModel mMModel = MeetingModel.getInstance();
        // set friends
        for(int i=0 ; i<mFModel.getFriends().size() ; i++)
        {
            db.addFriend(mFModel.getFriends().get(i));
        }
        // set meetings
        for(int j=0 ; j<mMModel.getMeetings().size() ; j++)
        {
            db.addMeeting(mMModel.getMeetings().get(j));
        }
    }
    //------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                Log.d(LOG_TAG, "Tab position: " + Integer.toString(selectedTabPosition));
                if (selectedTabPosition == 0) {
                    startContactPicker();
                } else if (selectedTabPosition == 1) {
                    addMeeting();
                } else {
                    // map view - do nothing
                }
            }
        });
    }

    public void addMeeting() {
        MeetingController mc = new MeetingController();
        Meeting meet = mc.createNewMeeting();
        startActivity(new Intent(this, EditMeetingActivity.class).putExtra("meeting", meet.getID()));
    }


    private void startContactPicker() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS);
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, PICK_CONTACTS);
        } else { //Still not granted
            Toast.makeText(this, "Requires access to contacts", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when the contact picked returns
     *
     * @author ermyasabebe
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(LOG_TAG, "onActivityResult()");
        if (requestCode == PICK_CONTACTS) {
            if (resultCode == RESULT_OK) {
                FriendController fc = new FriendController();
                boolean ok = fc.addFriendFromContacts(this, data);
                if (!ok) {
                    Log.d(LOG_TAG, "not ok activity result");
                    Toast.makeText(MainActivity.this, "Friend with that name and email already exists", Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(this)
                            .setTitle("Friend Exists")
                            .setMessage("Friend with that name and email already exists")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setNeutralButton(android.R.string.ok, null).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start contacts picker
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, PICK_CONTACTS);

                } else {
                    Toast.makeText(this, "Requires access to contacts", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "onRequestPermissionResult() - no to contacts");
                }
            }
            case LocationService.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Requires access to location", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "onRequestPermissionResult() - no to location");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // returning the current tab
            switch (position) {
                case 0:
                    return new Tab_Friend();
                case 1:
                    return new Tab_Meeting();
                case 2:
                    return new Tab_Map();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FRIEND";
                case 1:
                    return "MEETING";
                case 2:
                    return "MAP";
            }
            return null;
        }
    }
}
