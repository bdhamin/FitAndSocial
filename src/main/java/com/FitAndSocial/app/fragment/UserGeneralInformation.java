package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 11-9-14.
 */

import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.FitAndSocial.app.integration.DatabaseHandler;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.FASUser;

public class UserGeneralInformation extends BaseFragment{

    private View view;
    private TextView username;
    private TextView activeSince;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.user_general_information, null);
        initTextViews();
        populateTextViews();
        if(getIsExternalInformationRequired()){
            loadDataFromServer();
        }else{
            loadDataLocal();
        }
        return view;
    }

    private void initTextViews() {
        username =(TextView) view.findViewById(R.id.user_nickname);
        activeSince = (TextView) view.findViewById(R.id.user_active_since_date);
    }

    private void populateTextViews() {
        DatabaseHandler db = getDBInstance();
        String userId = getLoggedInUserId();
        FASUser user = db.findUser(userId);
        username.setText(user.getUsername());
        activeSince.setText(user.getActiveSince());
    }



    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        setUserVisibleHint(true);
    }


    private void loadDataLocal(){

    }

    private void loadDataFromServer(){

    }

}
