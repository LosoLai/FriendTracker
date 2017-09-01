package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.loso.friendtracker.R;

public class Tab_Map extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_map, container, false);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditFriendActivity.class));
            }
        };

        Button addFriend = (Button) rootView.findViewById(R.id.button);
        addFriend.setOnClickListener(listener);

        return rootView;
    }
}
