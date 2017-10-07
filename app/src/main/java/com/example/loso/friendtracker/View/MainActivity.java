package com.example.loso.friendtracker.View;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.loso.friendtracker.Controller.DatabaseController;
import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Controller.MeetingController;
import com.example.loso.friendtracker.Controller.PreferenceController;
import com.example.loso.friendtracker.Model.Location;
import com.example.loso.friendtracker.Model.Meeting;
import com.example.loso.friendtracker.R;
import com.example.loso.friendtracker.Service.CurrentLocationService;
import com.example.loso.friendtracker.Service.FriendWalkTimeService;
import com.example.loso.friendtracker.Service.NetworkStatusReceiver;

/**
 * @author LosoLai
 * @author Lettisia George
 */


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 17;
    private static final int PICK_CONTACTS = 100;
    private static final int SETTINGS_RESULT = 0;
    private DatabaseController dbController;

    //Added by LosoLai  24/09/2017

    /**
     * Database sync
     * maintain original in memory model which is synced (loaded) in onStart()
     * saved/persisted in onStop()
     */
    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "onStart()");
        super.onStart();


        // Start the broadcast receiver that monitors network connectivity
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getApplicationContext().registerReceiver(new NetworkStatusReceiver(), intentFilter);


        // This code chunk could potentially be moved to where meeting suggestion functionality is:
        IntentFilter intentFilter1 = new IntentFilter(NetworkStatusReceiver.NETWORK_CHANGE_DETECTED);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(LOG_TAG, "entered onReceive()");
                boolean connected = intent.getBooleanExtra(NetworkStatusReceiver.IS_NETWORK_CONNECTED, false);
                String text = connected ? "Network Connected" : "Network Disconnected";
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();

                if (connected) {
                    // TODO @Loso Start suggest now function when network becomes reconnected.
                }
                Log.d(LOG_TAG, text);
            }
        }, intentFilter1);

        // call CurrentLocationService constructor to update current location in preferences.
        Location currentLocation = new CurrentLocationService(this).getCurrentLocation();

        // initial model setup from DB
        dbController = new DatabaseController(this);
        dbController.setupDB();

        // Start the FriendWalkTimeService to calculate friend walking times
        Intent intent = new Intent(this, FriendWalkTimeService.class);
        getApplicationContext().startService(intent);

        //iitial PrefereceCtrller
        final SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final PreferenceController preferenceController = PreferenceController.getInstance();
        preferenceController.loadSharedPreference(prefs);
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onStop()");
        super.onStop();

        //clean DB and store memory data into DB
        dbController.closeDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                Log.d(LOG_TAG, "Tab position: " + Integer.toString(selectedTabPosition));
                if (selectedTabPosition == 0) {
                    startContactPicker();
                } else if (selectedTabPosition == 1) {
                    addMeeting();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tab.getPosition();
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                Log.i(LOG_TAG, "Tab position: " + Integer.toString(selectedTabPosition));
                if (selectedTabPosition == 0) {
                    fab.setVisibility(View.VISIBLE);
                } else if (selectedTabPosition == 1) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
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
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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
            case CurrentLocationService.MY_PERMISSIONS_REQUEST_LOCATION: {
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
            Intent i = new Intent(getApplicationContext(), UserSettingActivity.class);
            this.startActivity(i);
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
