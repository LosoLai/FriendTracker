package com.example.loso.friendtracker.View;

/**
 * Created by Loso on 2017/8/19.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loso.friendtracker.R;

public class Tab_Map extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_map, container, false);
        return rootView;
    }
}
