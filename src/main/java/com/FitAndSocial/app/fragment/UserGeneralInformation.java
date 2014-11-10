package com.FitAndSocial.app.fragment;

/**
 * Created by mint on 11-9-14.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.FitAndSocial.app.integration.service.IFASUserRepo;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.model.FASUser;
import com.google.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

public class UserGeneralInformation extends BaseFragment{

    private View view;
    private TextView username;
    private TextView activeSince;
    private ImageView userImage;
    @Inject
    private IFASUserRepo _userRepo;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceBundle){
        view = layoutInflater.inflate(R.layout.user_general_information, null);
        initTextViews();
        populateTextViews();
        return view;
    }

    private void initTextViews() {
        username =(TextView) view.findViewById(R.id.user_nickname);
        activeSince = (TextView) view.findViewById(R.id.user_active_since_date);
        userImage = (ImageView) view.findViewById(R.id.user_image);
    }

    private void populateTextViews() {
        String userId = getLoggedInUserId();
        Bitmap mIcon11;
        try {
            FASUser user = _userRepo.find(userId);
            username.setText(user.getUsername());
            activeSince.setText(user.getActiveSince());
            if(user.getImageBytes()!=null){
                InputStream in = new ByteArrayInputStream(user.getImageBytes());
                mIcon11 = BitmapFactory.decodeStream(in);
                userImage.setImageBitmap(mIcon11);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        setUserVisibleHint(true);
    }
}
