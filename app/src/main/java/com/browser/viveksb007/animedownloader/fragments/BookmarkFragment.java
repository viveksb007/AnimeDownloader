package com.browser.viveksb007.animedownloader.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.browser.viveksb007.animedownloader.R;

/**
 * Created by viveksb007 on 3/10/17.
 */

public class BookmarkFragment extends Fragment {

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        return view;
    }
}
