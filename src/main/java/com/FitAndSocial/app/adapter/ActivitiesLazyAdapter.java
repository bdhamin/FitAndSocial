package com.FitAndSocial.app.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.FitAndSocial.app.fragment.ActivityInformationFragment;
import com.FitAndSocial.app.fragment.BaseFragment;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ApplicationConstants;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 7-7-14.
 */
public class ActivitiesLazyAdapter extends BaseAdapter{

    private BaseFragment activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater layoutInflater = null;
    private TextView showMore;
    private TextView title;
    private TextView typeName;
    private TextView distanceInKM;
    private TextView dTime;
    private TextView aDate;
    private TextView aTime;
    private ImageView memberOne;
    private ImageView memberTwo;
    private ImageView memberThree;
    private TextView memberOneName;
    private TextView memberTwoName;
    private  TextView memberThreeName;
    private TextView members;
    private boolean isInformation =false;
    private View view;
    private HashMap<String, String> fas;
    private int activityMembersTotal;

    public ActivitiesLazyAdapter(BaseFragment activity, ArrayList<HashMap<String, String>> data){
        this.activity = activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setIsInformation(boolean isInformation){
            this.isInformation = isInformation;
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
        view = convertView;
        if(convertView == null)
            view = layoutInflater.inflate(R.layout.list_row, null);

        showMore = (TextView) view.findViewById(R.id.showMore);
        title = (TextView) view.findViewById(R.id.title);
        typeName = (TextView) view.findViewById(R.id.typeName);
        distanceInKM = (TextView) view.findViewById(R.id.km);
        dTime = (TextView) view.findViewById(R.id.dTime);
        aDate = (TextView) view.findViewById(R.id.aDate);
        aTime = (TextView) view.findViewById(R.id.aTime);

        memberOne = (ImageView) view.findViewById(R.id.memberOne);
        memberTwo = (ImageView) view.findViewById(R.id.memberTwo);
        memberThree = (ImageView) view.findViewById(R.id.memberThree);

        memberOneName = (TextView) view.findViewById(R.id.memberOneName);
        memberTwoName = (TextView) view.findViewById(R.id.memberTwoName);
        memberThreeName = (TextView) view.findViewById(R.id.memberThreeName);
        members = (TextView)view.findViewById(R.id.members);

        fas = new HashMap<>();

        fas = data.get(position);
        members.setText("Members: "+fas.get(ApplicationConstants.KEY_MEMBERS_TOTAL));
        activityMembersTotal = Integer.valueOf(fas.get(ApplicationConstants.KEY_MEMBERS_TOTAL));
        title.setText(fas.get(ApplicationConstants.KEY_TITLE));
        typeName.setText(fas.get(ApplicationConstants.KEY_TYPE));
        distanceInKM.setText(fas.get(ApplicationConstants.KEY_DISTANCE));
        dTime.setText(fas.get(ApplicationConstants.KEY_DURATION));
        aDate.setText(fas.get(ApplicationConstants.KEY_DATE));
        aTime.setText(fas.get(ApplicationConstants.KEY_TIME));
        if(fas.get("member_0_name") != "" && fas.get("member_0_name") != null){
            memberOneName.setText(fas.get("member_0_name"));
            memberOne.setImageDrawable(activity.getResources().getDrawable(R.drawable.friends));

            if(fas.get("member_1_name") != "" && fas.get("member_1_name") != null){
                memberTwoName.setText(fas.get("member_1_name"));
                memberTwo.setImageDrawable(activity.getResources().getDrawable(R.drawable.friends));
            }else{
                setVisibility(1);
            }
            if(fas.get("member_2_name") != "" && fas.get("member_2_name") != null){
                memberThreeName.setText(fas.get("member_2_name"));
                memberThree.setImageDrawable(activity.getResources().getDrawable(R.drawable.friends));
                if(!isInformation)showMore.setVisibility(View.VISIBLE);
            }else{
                setVisibility(2);
            }
        }else{
            showMore.setVisibility(View.INVISIBLE);
            memberOneName.setText("");
            memberTwoName.setText("");
            memberThreeName.setText("");
            memberOne.setImageDrawable(null);
            memberTwo.setImageDrawable(null);
            memberThree.setImageDrawable(null);
        }

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(data.get(position));
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity", data.get(position));
                bundle.putBoolean("participation", false);

                ActivityInformationFragment activityInformation = new ActivityInformationFragment();

                activityInformation.setArguments(bundle);

                FragmentTransaction transaction = activity.getActivity().getSupportFragmentManager().beginTransaction();
                Fragment activities = activity.getActivity().getSupportFragmentManager().findFragmentById(R.id.activities_container);
                Fragment noActivities = activity.getActivity().getSupportFragmentManager().findFragmentById(R.id.no_activities_fragment_container);
                transaction.remove(activities);
                transaction.remove(noActivities);
                transaction.add(R.id.create_fragment_container, activityInformation);
                //TODO check if the fragment already added to the backStack: http://stackoverflow.com/questions/14518086/android-fragment-addtobackstacknull-how-to-add-the-same-fragment-to-stack-jus
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void showPopup(HashMap<String, String> stringStringHashMap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getActivity());
        builder.setTitle("Activity Members");

        ListView modeList = new ListView(activity.getActivity());
        String[] stringArray = new String[Integer.valueOf(stringStringHashMap.get(ApplicationConstants.KEY_MEMBERS_TOTAL))];
        for(int i =0; i<Integer.valueOf(stringStringHashMap.get(ApplicationConstants.KEY_MEMBERS_TOTAL)); i++){
            stringArray[i] = stringStringHashMap.get("member_"+i+"_name");
        }
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(activity.getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final Dialog dialog = builder.create();

        dialog.show();
    }

    private void setVisibility(int members){
        switch (members){
            case 0:
                break;
            case 1:
                showMore.setVisibility(View.INVISIBLE);
                memberTwoName.setText("");
                memberThreeName.setText("");
                memberTwo.setImageDrawable(null);
                memberThree.setImageDrawable(null);
                break;
            case 2:
                showMore.setVisibility(View.INVISIBLE);
                memberThreeName.setText("");
                memberThree.setImageDrawable(null);
                break;
        }
    }

}
