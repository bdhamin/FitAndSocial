package com.FitAndSocial.app.socialLogin.google;


import com.FitAndSocial.app.fragment.BaseFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * Created by mint on 29-9-14.
 */
public final class GoogleApiClientInstance{

    private static GoogleApiClient _googleApiClient = null;

    private GoogleApiClientInstance(){}

    public static GoogleApiClient getGoogleApiClientInstance(BaseFragment baseFragment){
        if(_googleApiClient == null){

            _googleApiClient =  new GoogleApiClient.Builder(baseFragment.getActivity())
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();
            return _googleApiClient;
        }else{
            return _googleApiClient;
        }
    }
}
