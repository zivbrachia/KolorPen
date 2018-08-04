package com.example.kolorpen.kolorpenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TreatmentListActivity extends AppCompatActivity {

    ListView list;

    String customerId = null;

    String[] maintitle ={
            "Two days ago","a Week ago",
            "2018/04/08","2018/02/10",
            "2018/10/13",
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
    };

    Integer[] imgidBefore={
            R.drawable.treatment, R.drawable.treatment,
            R.drawable.treatment, R.drawable.treatment,
            R.drawable.treatment,
    };

    Integer[] imgidAfter={
            R.drawable.treatment, R.drawable.treatment,
            R.drawable.treatment, R.drawable.treatment,
            R.drawable.treatment,
    };

    Treatment[] treatments = {
            new Treatment(30 , 70, 1),
            new Treatment(100 , 20, 2),
            new Treatment(55 , 40, 1),
            new Treatment(10 , 50, 1),
            new Treatment(30 , 100, 1),
    };

    class Treatment {
        int dps;
        int volume;
        int pen;

        public Treatment(int dps, int volume, int pen) {
            this.dps = dps;
            this.volume = volume;
            this.pen = pen;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        Intent newint = getIntent();
        customerId = newint.getStringExtra("CUSTOMER_ID"); //receive the address of the bluetooth device
        Log.d("customerId", customerId);
        TreatmentListAdapter adapter = new TreatmentListAdapter(this, maintitle, subtitle,imgidBefore, imgidAfter);
        list = (ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                Intent data = new Intent();

                data.putExtra("PEN", treatments[position].pen);
                data.putExtra("DPS", treatments[position].dps);
                data.putExtra("VOLUME", treatments[position].volume);
                // Activity finished return ok, return the data
                setResult(RESULT_OK, data);
                finish();
            }
        });
        //

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
