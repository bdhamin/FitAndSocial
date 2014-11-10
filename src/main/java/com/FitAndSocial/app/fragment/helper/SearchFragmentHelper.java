package com.FitAndSocial.app.fragment.helper;

/**
 * Created by mint on 28-10-14.
 */
public interface SearchFragmentHelper {

    public void removeActivityFromList(int position);
    public void setParticipationRequestInfo(Long activityId, String loggedInUserId);
}
