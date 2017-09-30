package com.example.loso.friendtracker.View;

/**
 *
 * Created by Loso on 2017/8/19.
 * Modified to use MeetingModel class by Lettisia George 2017/9/1
 *
 * @author Lettisia George
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.loso.friendtracker.Controller.FriendController;
import com.example.loso.friendtracker.Controller.FriendListAdapter;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Model.FriendModel;
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

        Log.d(LOG_TAG, "OnCreateView()");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "onActivityCreated()");
        // ListView
        FriendModel mFriendModel = FriendModel.getInstance();
        mFriendModel.addObserver(this);
        ArrayList<Friend> friends = mFriendModel.getFriends();
        FriendController fc = new FriendController();
        //fc.updateFriendLocations(this.getContext());

        adapter = new FriendListAdapter(rootView.getContext(), friends);
        ListView lvFriend = (ListView) rootView.findViewById(R.id.friendlist);
        lvFriend.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        lvFriend.setClickable(true);
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "OnItemClickListener reached ok");
                Friend friend = adapter.getItem(position);
                startActivity(new Intent(getActivity(), EditFriendActivity.class).putExtra("friend", friend.getID()));

            }
        });

        lvFriend.setLongClickable(true);
        lvFriend.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "OnItemLongClickListener reached ok");
                final Friend friend = adapter.getItem(position);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Remove Friend")
                        .setMessage("Do you really want to remove this friend?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FriendController fc = new FriendController();
                                fc.removeFriend(friend);
                                Toast.makeText(getActivity(), "Friend removed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FriendModel && adapter != null && !(arg instanceof Friend)) {
            Log.d(LOG_TAG, "MADE IT TO UPDATE METHOD.");
            //FriendController fc = new FriendController();
            //fc.updateFriendLocations(this.getContext());
            adapter.notifyDataSetChanged();
        }
    }
}
