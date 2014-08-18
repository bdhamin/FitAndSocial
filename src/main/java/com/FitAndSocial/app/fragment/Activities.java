package com.FitAndSocial.app.fragment;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.util.ActivitiesLazyAdapter;
import com.FitAndSocial.app.util.Utils;
import com.FitAndSocial.app.util.XMLParser;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 12-7-14.
 */
public class Activities extends BaseFragment {

    private ListView listView;
    private ActivitiesLazyAdapter activitiesLazyAdapter;
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.activities, container, false);
        ArrayList<HashMap<String, String>> activitiesList = new ArrayList<>();
        XMLParser xmlParser = new XMLParser();
        AssetManager assetManager = getActivity().getAssets();
        InputStream inputStream = null;
        try{
            inputStream = assetManager.open("activities.xml");
        }catch (IOException e){System.out.println("DOCUMENT NOT FOUND");}

        String xml = Utils.readTextFile(inputStream);
        Document document = xmlParser.getDomElement(xml);
        NodeList nl = document.getElementsByTagName(KEY_ACTIVITY);
        for(int i=0; i<nl.getLength(); i++){
            HashMap<String, String> map = new HashMap<>();
            Element e = (Element) nl.item(i);
            map.put(KEY_TITLE, xmlParser.getValue(e, KEY_TITLE));
            map.put(KEY_TYPE, xmlParser.getValue(e, KEY_TYPE));
            map.put(KEY_DISTANCE, xmlParser.getValue(e, KEY_DISTANCE));
            map.put(KEY_DURATION, xmlParser.getValue(e, KEY_DURATION));
            map.put(KEY_DATE, xmlParser.getValue(e, KEY_DATE));
            map.put(KEY_TIME, xmlParser.getValue(e, KEY_TIME));
            NodeList members = ((Element) nl.item(i)).getElementsByTagName("member");
            if(members != null && members.getLength() > 0){
                for(int j=0; j< members.getLength(); j++){
                    Element member = (Element) members.item(j);
                    map.put("member_"+j+"_id", xmlParser.getValue(member, "id"));
                    map.put("member_"+j+"_name", xmlParser.getValue(member, "name"));
                    map.put("member_"+j+"_pictureURL", xmlParser.getValue(member, "pictureURL"));
                }
            }
            activitiesList.add(map);
        }
        listView = (ListView)view.findViewById(R.id.list);
        activitiesLazyAdapter = new ActivitiesLazyAdapter(getActivity(), activitiesList);
        listView.setAdapter(activitiesLazyAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
        super.onSaveInstanceState(onState);
        setUserVisibleHint(true);
    }



}
