package com.FitAndSocial.app.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.FitAndSocial.app.fragment.helper.EventHelperService;
import com.FitAndSocial.app.fragment.helper.SearchFragmentHelper;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.adapter.SearchResultAdapter;
import com.FitAndSocial.app.util.ApplicationConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 28-7-14.
 */
public class SearchResultFragment extends BaseFragment implements SearchFragmentHelper{

    private ListView listView;
    private SearchResultAdapter searchResultAdapter;
    private NodeList nodelist;
    private ProgressDialog pDialog;
    private View view;
    private TextView notification;
    private ArrayList<HashMap<String, String>> searchResultList;
    private final String USER_ID = "userId";
    private final String ACTIVITY_ID ="activityId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.search_result, container, false);
        notification = (TextView)view.findViewById(R.id.notification);
        Bundle bundle = this.getArguments();
        //TODO: search criteria should be passed to the main activity not via bundle
        String addressUrl = bundle.getString("search");
        new SearchResults().execute(addressUrl);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
        super.onSaveInstanceState(onState);
        setUserVisibleHint(true);
    }

    private class SearchResults extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Loading Results");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String ... Url){
            try {
                URLConnection connection = new URL(Url[0]).openConnection();
                connection.setConnectTimeout(7000);
                connection.setReadTimeout(7000);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(connection.getInputStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName(ApplicationConstants.KEY_ACTIVITY);

            }catch (MalformedURLException | ParserConfigurationException | SAXException | FileNotFoundException | SocketTimeoutException e ) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                if (nodelist != null && nodelist.getLength() > 0) {
                    /**
                     * The backend should returns all the needed information inc.
                     * google maps location. However the user at first will only
                     * see the general information but, when he/she clicks on an event
                     * the rest of the information will displayed.
                     * Also the search result should be cached for later use like getting
                     * the google maps values. This is useful so that the user don't need
                     * to make a request to the server when he clicks on a an event.
                     */

                    searchResultList = new ArrayList<>();

                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        HashMap<String, String> map = new HashMap<>();
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            map.put(ApplicationConstants.KEY_ACTIVITY_ID, getNode(ApplicationConstants.KEY_ACTIVITY_ID, eElement));
                            map.put(ApplicationConstants.KEY_TITLE, getNode(ApplicationConstants.KEY_TITLE, eElement));
                            map.put(ApplicationConstants.KEY_TYPE, getNode(ApplicationConstants.KEY_TYPE, eElement));
                            map.put(ApplicationConstants.KEY_DISTANCE, getNode(ApplicationConstants.KEY_DISTANCE, eElement));
                            map.put(ApplicationConstants.KEY_DURATION, getNode(ApplicationConstants.KEY_DURATION, eElement));
                            map.put(ApplicationConstants.KEY_DATE, getNode(ApplicationConstants.KEY_DATE, eElement));
                            map.put(ApplicationConstants.KEY_TIME, getNode(ApplicationConstants.KEY_TIME, eElement));
                            map.put(ApplicationConstants.KEY_MEMBERS_TOTAL, getNode(ApplicationConstants.KEY_MEMBERS_TOTAL, eElement));
                            map.put(ApplicationConstants.START_LAT, getNode(ApplicationConstants.START_LAT, eElement));
                            map.put(ApplicationConstants.START_LNG, getNode(ApplicationConstants.START_LNG, eElement));
                            map.put(ApplicationConstants.END_LAT, getNode(ApplicationConstants.END_LAT, eElement));
                            map.put(ApplicationConstants.END_LNG, getNode(ApplicationConstants.END_LNG, eElement));
                            map.put(ApplicationConstants.START_STREET_NAME, getNode(ApplicationConstants.START_STREET_NAME, eElement));
                            map.put(ApplicationConstants.END_STREET_NAME, getNode(ApplicationConstants.END_STREET_NAME, eElement));
                            map.put(ApplicationConstants.COMPLETE_START_ADDRESS, getNode(ApplicationConstants.COMPLETE_START_ADDRESS, eElement));
                            map.put(ApplicationConstants.COMPLETE_END_ADDRESS, getNode(ApplicationConstants.COMPLETE_END_ADDRESS, eElement));
                            NodeList members = ((Element) nodelist.item(temp)).getElementsByTagName("member");
                            if (members != null && members.getLength() > 0) {
                                for (int j = 0; j < members.getLength(); j++) {
                                    Element member = (Element) members.item(j);
                                    map.put("member_" + j + "_id", getNode("id", member));
                                    map.put("member_" + j + "_name", getNode("name", member));
                                    map.put("member_" + j + "_pictureURL", getNode("pictureURL", member));
                                }
                            }
                            searchResultList.add(map);
                        }

                        listView = (ListView) view.findViewById(R.id.list);
                        searchResultAdapter = new SearchResultAdapter(SearchResultFragment.this, searchResultList);
                        searchResultAdapter.setFragment(SearchResultFragment.this);
                        listView.setAdapter(searchResultAdapter);
                        setFragmentTitle("Search Results");
                    }
                }else{
                    pDialog.dismiss();
                    notification.setText("No result found");
                }
            }else{
                pDialog.dismiss();
                notification.setText("Cannot connect to the server \ntry again later");
            }
            pDialog.dismiss();
        }

        private String getNode(String sTag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                    .getChildNodes();
            Node nValue = (Node) nlList.item(0);
            return nValue.getNodeValue();
        }
    }


    @Override
    public void removeActivityFromList(int position) {
        searchResultList.remove(position);
        searchResultAdapter.notifyDataSetChanged();
        searchResultAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void setParticipationRequestInfo(Long activityId, String loggedInUserId) {
        Intent eventHelperIntent = new Intent(getActivity(), EventHelperService.class);
        eventHelperIntent.putExtra(ACTIVITY_ID, activityId);
        eventHelperIntent.putExtra(USER_ID, loggedInUserId);
        eventHelperIntent.putExtra(ApplicationConstants.EVENT_TYPE, ApplicationConstants.EVENT_TYPE_PARTICIPATE_ACTION);
        getActivity().startService(eventHelperIntent);
    }

}