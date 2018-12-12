//Student Name: Kieran Chi Io Song
//Student ID: S1423416

package mpdproject.gcu.me.org.assignmenttest1;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //variables
    private String url1 = "http://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String url2 = "http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private Button refreshButton1;
    private Button switchButton1;
    private Button switchButton2;
    private Button refreshButton2;
    private String result1 = "";
    private String result2 = "";
    private ViewSwitcher viewSwitcher;
    private boolean currentRoadWork = true;
    private List<String> listCurrent = new ArrayList<>();
    private List<String> listCurrentDescription = new ArrayList<>();
    private List<String> listCurrentMapPoints = new ArrayList<>();
    private List<String> listPlanned = new ArrayList<>();
    private List<String> listPlannedDescription = new ArrayList<>();
    private List<String> listPlannedMapPoints = new ArrayList<>();
    private ListView listView1;
    private ListView listView2;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private ViewFlipper currentFlip;
    private TextView currentDescriptionView;
    private Button backFromCurrentDescriptionButton;
    private ViewFlipper plannedFlip;
    private TextView plannedDescriptionView;
    private Button backFromPlannedDescriptionButton;
    private EditText currentSearch;
    private EditText plannedSearch;
    private GoogleMap gMap;
    private GoogleMap gMap2;
    private Button toMapFromCurrentDescriptionButton;
    private Button backFromCurrentMapButton;
    private Button toMapFromPlannedDescriptionButton;
    private Button backFromPlannedMapButton;
    private int tempCurrentIndex = 0;
    private int tempPlannedIndex = 0;
    private String[] splittedMapPoints;
    private float firstPoint = 0;
    private float secondPoint = 0;
    private SupportMapFragment mapFragment1;
    private SupportMapFragment mapFragment2;
    private Button currentSearchButton;
    private Button plannedSearchButton;
    private TextView currentDescriptionViewTitle;
    private TextView plannedDescriptionViewTitle;
    private TextView currentMapViewTitle;
    private TextView plannedMapViewTitle;


    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //change action bar color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#191970")));

        //instances of layout components
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitch);
        viewSwitcher.setInAnimation(this, android.R.anim.fade_in);
        viewSwitcher.setOutAnimation(this, android.R.anim.fade_out);

        refreshButton1 = (Button) findViewById(R.id.refreshButton1);
        refreshButton1.setOnClickListener(this);
        refreshButton2 = (Button) findViewById(R.id.refreshButton2);
        refreshButton2.setOnClickListener(this);
        switchButton1 = (Button) findViewById(R.id.switchButton1);
        switchButton1.setOnClickListener(this);
        switchButton2 = (Button) findViewById(R.id.switchButton2);
        switchButton2.setOnClickListener(this);

        currentFlip = (ViewFlipper) findViewById(R.id.currentFlip);
        currentFlip.setInAnimation(this, android.R.anim.fade_in);
        currentFlip.setOutAnimation(this, android.R.anim.fade_out);
        currentDescriptionView = (TextView) findViewById(R.id.currentDescriptionView);
        currentDescriptionViewTitle = (TextView) findViewById(R.id.currentDescriptionViewTitle);
        currentMapViewTitle = (TextView) findViewById(R.id.currentMapViewTitle);
        backFromCurrentDescriptionButton = (Button) findViewById(R.id.backFromCurrentDescriptionButton);
        backFromCurrentDescriptionButton.setOnClickListener(this);

        plannedFlip = (ViewFlipper) findViewById(R.id.plannedFlip);
        plannedFlip.setInAnimation(this, android.R.anim.fade_in);
        plannedFlip.setOutAnimation(this, android.R.anim.fade_out);
        plannedDescriptionView = (TextView) findViewById(R.id.plannedDescriptionView);
        plannedDescriptionViewTitle = (TextView) findViewById(R.id.plannedDescriptionViewTitle);
        plannedMapViewTitle = (TextView) findViewById(R.id.plannedMapViewTitle);
        backFromPlannedDescriptionButton = (Button) findViewById(R.id.backFromPlannedDescriptionButton);
        backFromPlannedDescriptionButton.setOnClickListener(this);

        toMapFromCurrentDescriptionButton = (Button) findViewById(R.id.toMapFromCurrentDescriptionButton);
        toMapFromCurrentDescriptionButton.setOnClickListener(this);
        backFromCurrentMapButton = (Button) findViewById(R.id.backFromCurrentMapButton);
        backFromCurrentMapButton.setOnClickListener(this);
        toMapFromPlannedDescriptionButton = (Button) findViewById(R.id.toMapFromPlannedDescriptionButton);
        toMapFromPlannedDescriptionButton.setOnClickListener(this);
        backFromPlannedMapButton = (Button) findViewById(R.id.backFromPlannedMapButton);
        backFromPlannedMapButton.setOnClickListener(this);
        currentSearch = (EditText) findViewById(R.id.currentSearch);
        plannedSearch = (EditText) findViewById(R.id.plannedSearch);

        currentSearchButton = (Button) findViewById(R.id.currentSearchButton);
        currentSearchButton.setOnClickListener(this);
        plannedSearchButton = (Button) findViewById(R.id.plannedSearchButton);
        plannedSearchButton.setOnClickListener(this);

        listView1 = (ListView) findViewById(R.id.listView1);
        listView2 = (ListView) findViewById(R.id.listView2);

        mapFragment1 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment1.getMapAsync(onMapReadyCallback1());

        mapFragment2 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment2.getMapAsync(onMapReadyCallback2());

        //Run network access on separate threads;
        new Thread(new Task(url1)).start();
        new Thread(new Task(url2)).start();

        //Prevent layout being adjust when virtual keyborad pop up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        //list view on click listener
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFlip.showNext();
                currentDescriptionViewTitle.setText(listCurrent.get(position));
                currentMapViewTitle.setText(listCurrent.get(position));
                currentDescriptionView.setText(listCurrentDescription.get(position));
                tempCurrentIndex = position;

            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                plannedFlip.showNext();
                plannedDescriptionViewTitle.setText(listPlanned.get(position));
                plannedMapViewTitle.setText(listPlanned.get(position));
                String[] splittedDescription = listPlannedDescription.get(position).split("<br />");
                plannedDescriptionView.setText(splittedDescription[0] + "\n" + "\n" + splittedDescription[1] + "\n" + "\n" + splittedDescription[2]);
                tempPlannedIndex = position;

            }
        });
    } // End of onCreate


    //list view will get update when the current Window of the activity gains or loses focus
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateListView();
        }
    }

    //on click method
    public void onClick(View v) {

        //refresh buttons on current incidents and planned roadwork view
        if (v == refreshButton1) {
            plannedSearch.setText("");
            currentSearch.setText("");
            updateListView();
        } else if (v == refreshButton2) {

            plannedSearch.setText("");
            currentSearch.setText("");
            updateListView();
        }

        //switch view buttons on current incidents and planned roadwork view
        if (v == switchButton1) {
            currentRoadWork = false;
            viewSwitcher.showNext();
            plannedSearch.setText("");
            currentSearch.setText("");
            updateListView();
        } else if (v == switchButton2) {
            currentRoadWork = true;
            viewSwitcher.showPrevious();
            plannedSearch.setText("");
            currentSearch.setText("");
            updateListView();

            //back buttons on current incidents and planned roadwork description view
        } else if (v == backFromCurrentDescriptionButton) {
            currentFlip.showPrevious();
        } else if (v == backFromPlannedDescriptionButton) {
            plannedFlip.showPrevious();

            //button that direct to current incidents map view
            //when clicked, it will obtain the map location points from the listCurrentMapPoints
            //and split the points by space, these 2 points will then be convert to float number
            //and assign to the location varible which will then be use to add marker on the map
            //and move the camera to the new location
        } else if (v == toMapFromCurrentDescriptionButton) {
            splittedMapPoints = listCurrentMapPoints.get(tempCurrentIndex).split("\\s+");

            firstPoint = Float.parseFloat(splittedMapPoints[0]);
            secondPoint = Float.parseFloat(splittedMapPoints[1]);

            LatLng location = new LatLng(firstPoint, secondPoint);
            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(location).title("Location"));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            currentFlip.showNext();

            //back buttons on current incidents map view
        } else if (v == backFromCurrentMapButton) {
            currentFlip.showPrevious();

            //button that direct to planned roadworks map view
        } else if (v == toMapFromPlannedDescriptionButton) {
            splittedMapPoints = listPlannedMapPoints.get(tempPlannedIndex).split("\\s+");

            firstPoint = Float.parseFloat(splittedMapPoints[0]);
            secondPoint = Float.parseFloat(splittedMapPoints[1]);

            LatLng location = new LatLng(firstPoint, secondPoint);
            gMap2.clear();
            gMap2.addMarker(new MarkerOptions().position(location).title("Location"));
            gMap2.moveCamera(CameraUpdateFactory.newLatLng(location));
            gMap2.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            plannedFlip.showNext();

            //back buttons on planned roadworks map view
        } else if (v == backFromPlannedMapButton) {
            plannedFlip.showPrevious();

            //search buttons that search the list view based on the text in edit text view
        } else if (v == currentSearchButton) {
            updateListView();
            searchCurrentItem(currentSearch.getText().toString());
        } else if (v == plannedSearchButton) {
            updateListView();
            searchPlannedItem(plannedSearch.getText().toString());
        }
    }


    //access data from traffic scotland website by using separate thread and store these data in to the result variables
    class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    if (url == url1) {
                        result1 = result1 + inputLine;
                    } else if (url == url2) {
                        result2 = result2 + inputLine;
                    }
                }
                in.close();
            } catch (IOException ae) {}

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {}
            });
        }
    }

    //parse data from the result variable
    private void parseData(String dataToParse) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();
            boolean skipFirstTitle = true;
            boolean skipFirstDescription = true;
            listCurrent.clear();
            listPlanned.clear();
            listCurrentDescription.clear();
            listPlannedDescription.clear();
            listCurrentMapPoints.clear();
            listPlannedMapPoints.clear();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Found a start tag
                if (eventType == XmlPullParser.START_TAG) {

                    //if the tag equal to title and the app is on current incidents view, pass the text inside
                    //the title tag to the current incidents list, else pass to the planned road works list
                    if (xpp.getName().equalsIgnoreCase("title")) {
                        if (skipFirstTitle) {
                            skipFirstTitle = false;
                        } else {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            if (currentRoadWork) {
                                listCurrent.add(temp);
                            } else {
                                listPlanned.add(temp);
                            }
                        }

                    //same as above except dealing with description text and pass to a different list
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (skipFirstDescription) {
                            skipFirstDescription = false;
                        } else {
                            String temp = xpp.nextText();
                            if (currentRoadWork) {
                                listCurrentDescription.add(temp);
                            } else {
                                listPlannedDescription.add(temp);
                            }
                        }

                        //same as above except dealing with location points text and pass to a different list
                    } else if (xpp.getName().equalsIgnoreCase("point")) {
                        String temp = xpp.nextText();
                        if (currentRoadWork) {
                            listCurrentMapPoints.add(temp);
                        } else {
                            listPlannedMapPoints.add(temp);
                        }
                    }

                }

                // Get the next event
                eventType = xpp.next();

            } // End of while

            //update adapter for listview
            if (currentRoadWork) {
                adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCurrent);
            } else {
                adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listPlanned);
            }

        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }
        Log.e("MyTag", "End document");

    }


    //method that takes the text from the search bar as parameter and search through the current incidents lists
    //remove elements with the same index number from differnt lists if the title text does not match the text form the search bar
    //if the list became empty that mean none of the title on the list view match the text from the search bar
    //then a dialog method will be call which will display a dialog to inform the user no result has been found
    //update the adapter for listview at the end
    public void searchCurrentItem(String textToSearch) {
        String[] arrayHolder = new String[listCurrent.size()];
        arrayHolder = listCurrent.toArray(arrayHolder);

        for (String item : arrayHolder) {
            if (!item.toLowerCase().contains(textToSearch.toLowerCase())) {
                int tempIndex = listCurrent.indexOf(item);
                listCurrent.remove(tempIndex);
                listCurrentDescription.remove(tempIndex);
                listCurrentMapPoints.remove(tempIndex);
            }
        }
        if (listCurrent.isEmpty()) {
            showNoResultDialog();
        }
        adapter1.notifyDataSetChanged();
    }

    //same as above but this is for planned roadwork views
    public void searchPlannedItem(String textToSearch) {

        String[] arrayHolder = new String[listPlanned.size()];
        arrayHolder = listPlanned.toArray(arrayHolder);

        for (String item : arrayHolder) {
            if (!item.toLowerCase().contains(textToSearch.toLowerCase())) {
                int tempIndex = listPlanned.indexOf(item);
                listPlanned.remove(tempIndex);
                listPlannedDescription.remove(tempIndex);
                listPlannedMapPoints.remove(tempIndex);
            }
        }
        if (listPlanned.isEmpty()) {
            showNoResultDialog();
        }
        adapter2.notifyDataSetChanged();
    }

    //show a dialog that tell user no result found and ask user to click a button to remove the dialog
    private void showNoResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No result found");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (currentRoadWork) {
                    currentSearch.setText("");
                    updateListView();
                } else {
                    plannedSearch.setText("");
                    updateListView();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //update list view function
    public void updateListView() {
        if (currentRoadWork) {
            parseData(result1);
            listView1.setAdapter(adapter1);
        } else {
            parseData(result2);
            listView2.setAdapter(adapter2);
        }
    }

    //initialise maps
    public OnMapReadyCallback onMapReadyCallback1() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                LatLng location = new LatLng(0, 0);
                gMap.addMarker(new MarkerOptions().position(location).title("Location"));
                gMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            }
        };
    }
    public OnMapReadyCallback onMapReadyCallback2() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap2 = googleMap;
                LatLng location = new LatLng(0, 0);
                gMap2.addMarker(new MarkerOptions().position(location).title("Location"));
                gMap2.moveCamera(CameraUpdateFactory.newLatLng(location));
                gMap2.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            }
        };
    }

    //initialise option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //actions that preform when click on the items in the option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //change backgrounds
        if (id == R.id.bg1) {
            viewSwitcher.setBackgroundResource(R.drawable.low_res_bg_image3);
            Toast.makeText(getApplicationContext(), "Switched to background 1", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.bg2) {
            viewSwitcher.setBackgroundResource(R.drawable.low_res_bg_image4);
            Toast.makeText(getApplicationContext(), "Switched to background 2", Toast.LENGTH_SHORT).show();

        //change map types
        } else if (id == R.id.mapType1) {

            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            gMap2.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Toast.makeText(getApplicationContext(), "Map has been switched to normal view", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mapType2) {

            gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            gMap2.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            Toast.makeText(getApplicationContext(), "Map has been switched to terrain view", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.mapType3) {

            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            gMap2.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Toast.makeText(getApplicationContext(), "Map has been switched to hybrid view", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.mapType4) {

            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            gMap2.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            Toast.makeText(getApplicationContext(), "Map has been switched to satellite view", Toast.LENGTH_SHORT).show();

        //display info through alert dialog
        } else if (id == R.id.about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This is an coursework application for GCU mobile platform development module, created by Kieran Chi Io Song");
            builder.setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();
        }
        return true;
    }

}














////////////////////////////Old and backup codes, please ignore all the codes below///////////////////////////////////////////////////////////////////////////
/*
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    getSupportActionBar().hide();

  //String[] cars ={"abc","123","poiu"};
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cars);

  //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

   //listView2.setOnClickListener(this);
        //listView.setAdapter(adapter);

/*
        currentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    updateListView();
                } else {
                    searchCurrentItem(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        plannedSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    updateListView();
                } else {
                    searchPlannedItem(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/
/*
        if(result1 =="")
        {
            Toast.makeText(getApplicationContext(), "Fail to obtain data from the feed", Toast.LENGTH_SHORT).show();
        }
        */

  /*
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //Configuration c = getResources().getConfiguration();
        if (newConfig.orientation  == Configuration.ORIENTATION_PORTRAIT)
        {
            Toast.makeText(this, "portrait mode", Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation  == Configuration.ORIENTATION_LANDSCAPE)
        {
            Toast.makeText(this, "landscape mode", Toast.LENGTH_SHORT).show();
        }
    }
*/
//if (temp.toLowerCase().contains(plannedSearch.getText().toString().toLowerCase()))
    /*
    public void onMapReady(GoogleMap googleMap)
    {

        if(!tempFrag)
        {
            gMap = googleMap;
            LatLng location = new LatLng(0, 0);

            gMap.addMarker(new MarkerOptions().position(location).title("Location"));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mapFragment2.getMapAsync(this);
            tempFrag=true;
        }
        else
            {
            gMap2 = googleMap;
            LatLng location = new LatLng(0, 0);

            gMap2.addMarker(new MarkerOptions().position(location).title("Location"));
            gMap2.moveCamera(CameraUpdateFactory.newLatLng(location));
            gMap2.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        }
    }
    */

     /*
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("link"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                //Log.e("MyTag","Washer is " + temp);
                                modifiedResult = modifiedResult + temp;
                            }
                            */
    //listCurrent.set(listCurrent.indexOf(item),"");
    //listView1.setVisibility(View.INVISIBLE);
    //listCurrent.remove(item);

//Log.d("UI thread", "I am the UI thread");
/*
                    if (currentRoadWork) {
                        //urlInput1.setText(modifiedResult1);
                        listView1.setAdapter(adapter1);
                    } else  {
                        listView2.setAdapter(adapter2);
                    }
*/



