package com.example.yasin.taksmssender.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasin.taksmssender.Adapter.RecyclerAdapterSms;
import com.example.yasin.taksmssender.Adapter.RecyclerAdapterSmsHistory;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.Model.SmsHistory;
import com.example.yasin.taksmssender.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsHistoryFragment extends Fragment {


    public SmsHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSmsHistoryFragment);
        if (SmsHistory.getData(getContext()).size() != 0){
            RecyclerAdapterSmsHistory adapter = new RecyclerAdapterSmsHistory(getContext() , SmsHistory.getData(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(0);
            LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        return view;
    }

}
