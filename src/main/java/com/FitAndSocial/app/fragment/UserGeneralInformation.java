package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 11-9-14.
 */

import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.FitAndSocial.app.mobile.R;

public class UserGeneralInformation extends BaseFragment{

    private View view;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.user_general_information, null);
        if(getIsExternalInformationRequired()){
            loadDataFromServer();
        }else{
            loadDataLocal();
        }
        return view;
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
