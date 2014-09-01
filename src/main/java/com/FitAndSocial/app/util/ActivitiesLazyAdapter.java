package com.FitAndSocial.app.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 7-7-14.
 */
public class ActivitiesLazyAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater layoutInflater = null;
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_MEMBERS_TOTAL="members_total";
    private TextView showMore;
    TextView no_members;
    TextView title;
    TextView typeName;
    TextView distanceInKM;
    TextView dTime;
    TextView aDate;
    TextView aTime;
    ImageView memberOne;
    ImageView memberTwo;
    ImageView memberThree;
    TextView memberOneName;
    TextView memberTwoName;
    TextView memberThreeName;
    TextView members;
    private boolean test =false;



    public ActivitiesLazyAdapter(Activity activity, ArrayList<HashMap<String, String>> data){
        this.activity = activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setIsInformation(boolean isInformation){
        if(isInformation){
          test = true;
        }
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(convertView == null)
            view = layoutInflater.inflate(R.layout.list_row, null);

        showMore = (TextView) view.findViewById(R.id.showMore);
        no_members = (TextView) view.findViewById(R.id.no_members);
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

        HashMap<String, String> fas = new HashMap<>();

//        if(test){
//            members.setText("Participants: " + fas.get(KEY_PARTICIPANT));
//            showMore.setVisibility(View.INVISIBLE);
//        }

        fas = data.get(position);
        members.setText("Members: "+fas.get(KEY_MEMBERS_TOTAL));
        title.setText(fas.get(KEY_TITLE));
        typeName.setText(fas.get(KEY_TYPE));
        distanceInKM.setText(fas.get(KEY_DISTANCE));
        dTime.setText(fas.get(KEY_DURATION));
        aDate.setText(fas.get(KEY_DATE));
        aTime.setText(fas.get(KEY_TIME));
        if(fas.get("member_0_name") != "" && fas.get("member_0_name") != null){
            memberOneName.setText(fas.get("member_0_name"));
            memberOne.setImageDrawable(activity.getResources().getDrawable(R.drawable.friends));
            no_members.setVisibility(View.INVISIBLE);

            if(fas.get("member_1_name") != "" && fas.get("member_1_name") != null){
                memberTwoName.setText(fas.get("member_1_name"));
                memberTwo.setImageDrawable(activity.getResources().getDrawable(R.drawable.friends));
            }else{
                setVisibility(1);
            }
            if(fas.get("member_2_name") != "" && fas.get("member_2_name") != null){
                memberThreeName.setText(fas.get("member_2_name"));
                memberThree.setImageDrawable(activity.getResources().getDrawable(R.drawable.friends));
                if(!test)showMore.setVisibility(View.VISIBLE);
            }else{
                setVisibility(2);
            }
        }else{
            no_members.setVisibility(View.VISIBLE);
            showMore.setVisibility(View.INVISIBLE);
            memberOneName.setText("");
            memberTwoName.setText("");
            memberThreeName.setText("");
            memberOne.setImageDrawable(null);
            memberTwo.setImageDrawable(null);
            memberThree.setImageDrawable(null);
        }
        return view;
    }

    private void setVisibility(int members){
        switch (members){
            case 0:
                break;
            case 1:
                showMore.setVisibility(View.INVISIBLE);
                no_members.setVisibility(View.INVISIBLE);
                memberTwoName.setText("");
                memberThreeName.setText("");
                memberTwo.setImageDrawable(null);
                memberThree.setImageDrawable(null);
                break;
            case 2:
                showMore.setVisibility(View.INVISIBLE);
                no_members.setVisibility(View.INVISIBLE);
                memberThreeName.setText("");
                memberThree.setImageDrawable(null);
                break;
        }
    }


}
