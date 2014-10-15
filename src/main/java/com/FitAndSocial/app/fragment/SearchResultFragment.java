package com.FitAndSocial.app.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.R;
import com.FitAndSocial.app.adapter.SearchResultAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mint on 28-7-14.
 */
public class SearchResultFragment extends BaseFragment {

    private ListView listView;
    private SearchResultAdapter searchResultAdapter;
    //HashMap Keys
    private final String KEY_ACTIVITY = "activity"; //parent node name
    private final String KEY_ACTIVITY_ID = "id";
    private final String KEY_TITLE = "title";
    private final String KEY_TYPE = "type";
    private final String KEY_DISTANCE = "distance";
    private final String KEY_DURATION = "duration";
    private final String KEY_DATE = "date";
    private final String KEY_TIME = "time";
    private final String KEY_MEMBERS_TOTAL = "members_total";
    private String address;
    private NodeList nodelist;
    private ProgressDialog pDialog;
    private View view;
    private TextView notification;

    /**
     *
     * @param address
     * Create a constructor with as parameter an address string.
     * The address should be the address of the xml document
     * This address is coming from SearchFragment and contains
     * all the necessary information like date, time, type, radius etc
     */
    public SearchResultFragment(String address){
        this.address = address;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.search_result, container, false);
        notification = (TextView)view.findViewById(R.id.notification);
        new SearchResults().execute(address);
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
                URL url = new URL(Url[0]);
                System.out.println(url.toString());
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName(KEY_ACTIVITY);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
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

                    ArrayList<HashMap<String, String>> searchResultList = new ArrayList<>();

                    for (int temp = 0; temp < nodelist.getLength(); temp++) {
                        HashMap<String, String> map = new HashMap<>();
                        Node nNode = nodelist.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            map.put(KEY_ACTIVITY_ID, getNode(KEY_ACTIVITY_ID, eElement));
                            map.put(KEY_TITLE, getNode(KEY_TITLE, eElement));
                            map.put(KEY_TYPE, getNode(KEY_TYPE, eElement));
                            map.put(KEY_DISTANCE, getNode(KEY_DISTANCE, eElement));
                            map.put(KEY_DURATION, getNode(KEY_DURATION, eElement));
                            map.put(KEY_DATE, getNode(KEY_DATE, eElement));
                            map.put(KEY_TIME, getNode(KEY_TIME, eElement));
                            map.put(KEY_MEMBERS_TOTAL, getNode(KEY_MEMBERS_TOTAL, eElement));
                            searchResultList.add(map);
                        }

                        listView = (ListView) view.findViewById(R.id.list);
                        searchResultAdapter = new SearchResultAdapter(SearchResultFragment.this, searchResultList);
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

}