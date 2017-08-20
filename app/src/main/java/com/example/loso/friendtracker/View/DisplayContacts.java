package com.example.loso.friendtracker.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.loso.friendtracker.R;

public class DisplayContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        String[] items = {"AAA", "BBB", "CCC", "DDD", "EEE", "FFF"};
        ListView contactList = (ListView)findViewById(R.id.lvContactsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        contactList.setAdapter(adapter);
    }
}
