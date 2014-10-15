package com.FitAndSocial.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.FitAndSocial.app.fragment.ActivityInformationFragment;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.FitAndSocial.app.fragment.GoogleMapsFragment;
import com.FitAndSocial.app.fragment.SearchResultFragment;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ParticipationHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 28-7-14.
 */
public class SearchResultAdapter extends BaseAdapter{

    private BaseFragment activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater layoutInflater = null;
    private final String PARTICIPATION_URL = "http://192.168.2.9:9000/participationRequest";

    //HashMap Keys
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_ACTIVITY_ID = "id";
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_PARTICIPANT = "participants";


    //View TextView
    TextView title;
    TextView typeName;
    TextView km;
    TextView dTime;
    TextView participants_total;
    TextView aTime;
    TextView aDate;
    Button participate;

    public SearchResultAdapter(BaseFragment searchActivity, ArrayList<HashMap<String, String>> searchData){
        this.activity = searchActivity;
        this.data = searchData;
        layoutInflater = (LayoutInflater) activity.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(convertView == null)
            view = layoutInflater.inflate(R.layout.list_row_search_result, null);

        title = (TextView) view.findViewById(R.id.title);
        typeName = (TextView) view.findViewById(R.id.typeName);
        km = (TextView) view.findViewById(R.id.km);
        dTime = (TextView) view.findViewById(R.id.dTime);
        participants_total = (TextView) view.findViewById(R.id.participants_total);
        aTime = (TextView) view.findViewById(R.id.aTime);
        aDate = (TextView) view.findViewById(R.id.aDate);
        participate = (Button) view.findViewById(R.id.participate);

        HashMap<String, String> search = new HashMap<>();
        search = data.get(position);

        title.setText(search.get(KEY_TITLE));
        typeName.setText(search.get(KEY_TYPE));
        km.setText(search.get(KEY_DISTANCE));
        dTime.setText(search.get(KEY_DURATION));
        participants_total.setText(search.get(KEY_PARTICIPANT));
        aTime.setText(search.get(KEY_TIME));
        aDate.setText(search.get(KEY_DATE));


        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ParticipationHelper(269,Long.valueOf(data.get(position).get(KEY_ACTIVITY_ID)), activity).execute(PARTICIPATION_URL);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityInformationFragment activityInformation = new ActivityInformationFragment(data.get(position), true);
                FragmentTransaction transaction = activity.getActivity().getSupportFragmentManager().beginTransaction();
                Fragment searchResult = activity.getActivity().getSupportFragmentManager().findFragmentById(R.id.activities_container);
                transaction.remove(searchResult);
                transaction.add(R.id.create_fragment_container, activityInformation);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }


}
