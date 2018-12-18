package tig167.myfirstapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tig167.myfirstapp.Trafikverket.VolleyTraffic;
import tig167.myfirstapp.police.Handelser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import tig167.myfirstapp.VolleyPolice.HandelserChangeListener;
import tig167.myfirstapp.VolleyPolice;


public class MainActivity extends Activity {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArrayAdapter<Handelser> adapter;
    private List<Handelser> handelser;
    private MainActivity me;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor prefEditor = pref.edit();


        String stad = pref.getString("stad", "ingen stad sparad");
        Boolean pressOnOff = pref.getBoolean("press", true);
        Boolean poliusenOnOff = pref.getBoolean("polisen", false);
        Boolean trafikOnOff = pref.getBoolean("trafik", true);
        Boolean tidtabellOnOff = pref.getBoolean("tidtabell", false);

        Settings.setStad(stad);


        Log.d(LOG_TAG, "Här är inställningarna:" + stad + pressOnOff + poliusenOnOff + trafikOnOff + tidtabellOnOff);


        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        handelser = new ArrayList<>();

        me = this;

        // register to listen to member updates in VolleyMember
        VolleyPolice.getInstance(this).addHandelserChangeListener(new HandelserChangeListener() {
            @Override
            public void onHandelserChangeList(List<Handelser> handelser) {
                if (handelser==null) {
                    Log.d(LOG_TAG, "   Failed to fetch JSON");
                    //ActivitySwitcher.showToast.length_LONG(me, "No response from Polisen, try again", );
                    Toast.makeText(me, "No response from Polisen, try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                // resetexpListView(handelser);
                List <String> polisgrejer = new ArrayList<>();
                for (Handelser h : handelser) {
                    polisgrejer.add(h.toString());
                }
                //Log.d(LOG_TAG, "onHandelserChangeList()   Wowie Zowie:  " + handelser);
                listDataChild.put(listDataHeader.get(1), polisgrejer);
                listAdapter = new ExpandableListAdapter(MainActivity.this, listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);

                ActivitySwitcher.showToast(me, "Handelser updated");
            }
        });

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Pressmeddelanden");
        listDataHeader.add("Polisen");
        listDataHeader.add("Trafikinfo");

        List<String> pressmeddelanden = new ArrayList<String>();
        pressmeddelanden.add("Löfven somnar på jobbet");
        pressmeddelanden.add("Gratis bira i stadshuset");
        pressmeddelanden.add("Håkan Hellström utsedd till tronarvinge; 'fan va soft'");

        List<String> polisen = new ArrayList<String>();
        polisen.add("Ingen data att visa");

        List<String> trafikinfo = new ArrayList<String>();
        trafikinfo.add("Olycka på Älvsborgsbron");
        trafikinfo.add("Krock skapar köer på Avenyn");


        listDataChild.put(listDataHeader.get(0), pressmeddelanden);
        listDataChild.put(listDataHeader.get(1), polisen);
        listDataChild.put(listDataHeader.get(2), trafikinfo);
    }

    public void gotoSettings(View view) {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }

    public void refreshList(View view) {
        ActivitySwitcher.showToast(this, "Updating handelser");
        VolleyPolice.getInstance(this).getHandelser();
        //handelser.add();
        System.out.println("Här är händelser: " + handelser);

    }
}
