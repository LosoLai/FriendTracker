package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 * Modified to use Model class by Lettisia George 2017/9/1
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Controller.FriendListAdapter;
import com.example.loso.friendtracker.Model.Model;
import com.example.loso.friendtracker.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class Tab_Friend extends Fragment implements Observer {
    private static final String LOG_TAG = "friendtab";
    private View rootView;
    private FriendListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_friend, container, false);

        //Log.d(LOG_TAG, "OnCreateView()");

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayContacts = new Intent(getActivity(), DisplayContacts.class);
                startActivity(displayContacts);
            }
        };

        Button addFriend = (Button) rootView.findViewById(R.id.bAddFriend);
        addFriend.setOnClickListener(listener);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Log.d(LOG_TAG, "onActivityCreated()");
        // ListView
        Model mModel = Model.getInstance();
        mModel.addObserver(this);
        ArrayList<Friend> friends = mModel.getFriends();
        adapter = new FriendListAdapter(rootView.getContext(), friends);
        ListView lvFriend = (ListView) rootView.findViewById(R.id.friendlist);
        //String[] items = {"AAA", "BBB", "CCC", "DDD", "EEE", "FFF"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, items);
        lvFriend.setAdapter(adapter);
        lvFriend.setLongClickable(true);
        lvFriend.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = adapter.getItem(position);
                startActivity(new Intent(getActivity(), EditFriendActivity.class).putExtra("friend", friend.getID()));
                return true;
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d(LOG_TAG, "MADE IT TO UPDATE METHOD.");
        if (arg.equals(Model.FRIENDS_CHANGED)) {
            adapter.notifyDataSetChanged();
        } else if (arg instanceof Friend) { // srg is a friend that has been removed
            adapter.remove((Friend) arg);
            adapter.notifyDataSetChanged();
        }
    }
}
