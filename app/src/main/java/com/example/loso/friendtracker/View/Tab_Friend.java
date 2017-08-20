package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 */
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.loso.friendtracker.Controller.DataManager;
import com.example.loso.friendtracker.Model.Friend;
import com.example.loso.friendtracker.Controller.FriendListAdapter;
import com.example.loso.friendtracker.R;

import java.util.ArrayList;

public class Tab_Friend extends Fragment {
    View rootView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_friend, container, false);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayContacts = new Intent(getActivity(), DisplayContacts.class);
                startActivity(displayContacts);
            }
        };

        Button addFriend = (Button)rootView.findViewById(R.id.bAddFriend);
        addFriend.setOnClickListener(listener);
        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ListView
        DataManager data = DataManager.getInstance();
        ArrayList<Friend> friends = data.getFriendList();
        FriendListAdapter adapter = new FriendListAdapter(rootView.getContext(), friends);
        ListView lvFriend = (ListView)rootView.findViewById(R.id.friendlist);
        //String[] items = {"AAA", "BBB", "CCC", "DDD", "EEE", "FFF"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, items);
        lvFriend.setAdapter(adapter);
    }
}
