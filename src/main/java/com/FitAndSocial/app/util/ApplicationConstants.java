package com.FitAndSocial.app.util;

/**
 * Created by mint on 27-10-14.
 */
public final class ApplicationConstants {

    private ApplicationConstants(){}

    //SERVER XML FILE ATTRIBUTES
    public static final String KEY_ACTIVITY_ID = "id";
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_MEMBERS_TOTAL="members_total";
    public static final String KEY_USER = "user";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_ACTIVITIES = "activities";
    public static final String KEY_ABOUT = "about";
    public static final String ACTIVE_SINCE = "active_since";
    public static final String KEY_ACTIVITY_TYPE = "activityType";
    public static final String KYE_DISTANCE = "distance";
    public static final String KEY_Min_DURATION = "durationMin";
    public static final String KEY_Max_DURATION = "durationMax";
    public static final String KEY_Min_RADIUS = "radiusMin";
    public static final String KEY_Max_RADIUS = "radiusMax";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_START_TIME = "time";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_CREATED = "created";
    public static final String KEY_PARTICIPATED = "participated";
    public static final String KEY_CANCELLED = "cancelled";
    public static final String KEY_PARTICIPANT = "participants";

    //CURRENT LOCAL SERVER BASE ADDRESS
    public static final String SERVER_BASE_ADDRESS = "http://192.168.2.9:9000";

    //USER ACTIONS TO THE BACKEND
    public static final String SERVER_ADDRESS_ACTION_REGISTER = "/register";
    public static final String SERVER_ADDRESS_ACTION_UPCOMING_ACTIVITIES = "/upcomingActivities";
    public static final String SERVER_ADDRESS_ACTION_CREATE_ACTIVITY = "/createActivity";
    public static final String SERVER_ADDRESS_ACTION_LAST_ACTIVITY = "/lastActivity";
    public static final String SERVER_ADDRESS_ACTION_UPDATE_PROFILE = "/updateProfile";
    public static final String SERVER_ADDRESS_ACTION_USER_PROFILE = "/userProfile/";
    public static final String SERVER_ADDRESS_ACTION_USER_ACTIVITIES_SUMMARY = "/userActivitiesSummary/";
    public static final String SERVER_ADDRESS_ACTION_PARTICIPATION_REQUEST = "/participationRequest";
    public static final String SERVER_ADDRESS_ACTION_CANCEL_PARTICIPATION = "/cancelParticipation";
    public static final String SERVER_ADDRESS_ACTION_DEVICE_REGISTRATION = "/deviceRegistrationId/";
    public static final String SERVER_ADDRESS_ACTION_CAN_CONNECT = "/canConnect";

    //SHARED PREFERENCE ATTRIBUTES
    public static final String APPLICATION_PREFERENCE = "applicationPreference";
    public static final String APPLICATION_PREFERENCE_REGISTERED_USERS = "registeredUsers";
    public static final String APPLICATION_PREFERENCE_USER_ID = "userId";
    public static final String APPLICATION_PREFERENCE_USERNAME = "username";
    public static final String APPLICATION_PREFERENCE_LOGIN_TYPE = "loginType";
    public static final String APPLICATION_PREFERENCE_LOGIN_TYPE_FACEBOOK = "facebook";
    public static final String APPLICATION_PREFERENCE_LOGIN_TYPE_GOOGLE = "google";
    public static final String APPLICATION_PREFERENCE_USERS = "users";
    public static final String APPLICATION_PREFERENCE_LOGGED_IN = "loggedIn";


    //GCM
    public static final String PROJECT_NUMBER = "5876528920";

    //ViewPager Tab Title
    public static final String VIEW_PAGER_TAB_PERSONAL = "Personal";
    public static final String VIEW_PAGER_TAB_PROFILE = "Profile";
    public static final String VIEW_PAGER_TAB_NOTIFICATIONS = "Notifications";
    public static final String VIEW_PAGER_TAB_ACCOUNT = "Account";


    //Fragments title
    public static final String FRAGMENT_TITLE_ACCOUNT = "Account";
    public static final String FRAGMENT_TITLE_ACTIVITY_DETAILS = "Event Details";
    public static final String FRAGMENT_TITLE_CREATE_NEW_ACTIVITY = "Create New Activity";
    public static final String FRAGMENT_TITLE_SEARCH = "Search";


    //Event type (Create event, Participate to an event, etc..)
    public static final String EVENT_TYPE = "eventType";
    public static final String EVENT_TYPE_CREATE_ACTION = "create";
    public static final String EVENT_TYPE_PARTICIPATE_ACTION = "participate";
    public static final String EVENT_TYPE_PARTICIPATE_CANCEL_ACTION = "cancel";


    //Google Maps
    public static final String START_LAT = "startLat";
    public static final String START_LNG = "startLng";
    public static final String END_LAT = "endLat";
    public static final String END_LNG = "endLng";
    public static final String START_STREET_NAME = "startStreet";
    public static final String END_STREET_NAME = "endStreet";
    public static final String COMPLETE_START_ADDRESS = "completeStartStreet";
    public static final String COMPLETE_END_ADDRESS = "completeEndStreet";

}
