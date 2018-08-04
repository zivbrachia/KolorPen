package com.example.kolorpen.kolorpenapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ColorPickerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
//    private ViewPager mViewPager;
    private ImageView pen1;
    private ImageView pen2;
    private Button btnScan;
    private Button btnScan3;
    private TextView tvColorRemain;
    private TextView tvDescOfUseData;
    private TextView tvDescOfUseData1;
    private TextView tvDescOfUseData2;
    private TextView tvDescOfUseData3;
    private TextView tvDescOfUseData4;
    private TextView tvDescOfUseData5;
    private TextView tvDescOfUseData6;
    private ImageView ivPen1Selected;
    private ImageView ivPen2Selected;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private JSONObject jsonObj;
    static final int CODE_SCANNER = 7;
    protected  final static int UPDATE1 = 9;
    protected  final static int UPDATE2 = 10;

    static final int AMOUNT_OF_INK_IN_ML_FOR_PER_SECOND = 3;

    // This is the activity main thread Handler.
    protected Handler updateUIHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker2);

//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        prefs = getApplication().getSharedPreferences("KolorPenApp", getApplicationContext().MODE_PRIVATE);
        editor = prefs.edit();

        createUpdateUiHandler();

        tvColorRemain = (TextView)findViewById(R.id.tvColorRemain);
        tvDescOfUseData = (TextView)findViewById(R.id.tvDescOfUseData);
        tvDescOfUseData1 = (TextView)findViewById(R.id.tvDescOfUseData1);
        tvDescOfUseData2 = (TextView)findViewById(R.id.tvDescOfUseData2);
        tvDescOfUseData3 = (TextView)findViewById(R.id.tvDescOfUseData3);
        tvDescOfUseData4 = (TextView)findViewById(R.id.tvDescOfUseData4);
        tvDescOfUseData5 = (TextView)findViewById(R.id.tvDescOfUseData5);
        tvDescOfUseData6 = (TextView)findViewById(R.id.tvDescOfUseData6);
        ivPen1Selected = (ImageView)findViewById(R.id.ivPen1Selected);
        ivPen2Selected = (ImageView)findViewById(R.id.ivPen2Selected);

        loadPen1();

        btnScan = (Button)findViewById(R.id.btnScan2);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorPickerActivity.this, CodeScanner.class);
                intent.putExtra("START_SCAN", true);
                intent.putExtra("PEN", 1);
                startActivityForResult(intent, CODE_SCANNER);

            }
        });

        btnScan3 = (Button)findViewById(R.id.btnScan3);
        btnScan3.setEnabled(false);
        btnScan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorPickerActivity.this, CodeScanner.class);
                intent.putExtra("START_SCAN", true);
                intent.putExtra("PEN", 2);
                startActivityForResult(intent, CODE_SCANNER);
            }
        });

        ivPen1Selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScan.setEnabled(true);
                btnScan3.setEnabled(false);
                loadPen1();
            }
        });

        ivPen2Selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScan.setEnabled(false);
                btnScan3.setEnabled(true);
                loadPen2();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
//        intent.putExtra("content", result.getContents());
//        intent.putExtra("status", "scanned");
//        intent.putExtra("PEN", bundle.getInt("PEN"));
        setResult(RESULT_OK, intent);
        finish();

        //super.onBackPressed();


    }

    private void loadPen1() {
        tvColorRemain.setText( getResources().getString(R.string.color_remain) + " " + calcRemainColor("pen_1") + " ml");
        tvDescOfUseData.setText(prefs.getString("pen_1_description_of_use", ""));
        tvDescOfUseData1.setText(prefs.getString("pen_1_ingredients", ""));
        tvDescOfUseData2.setText(prefs.getString("pen_1_name_of_color", ""));
        tvDescOfUseData3.setText(prefs.getInt("pen_1_amount_of_color", 0) + "");
        tvDescOfUseData4.setText(prefs.getString("pen_1_before", ""));
        tvDescOfUseData5.setText(prefs.getString("pen_1_after", ""));
        tvDescOfUseData6.setText(prefs.getString("pen_1_name","") + " " + prefs.getString("pen_1_address", ""));
    }

    private void loadPen2() {
        tvColorRemain.setText( getResources().getString(R.string.color_remain) + " " + calcRemainColor("pen_2") + " ml");
        tvDescOfUseData.setText(prefs.getString("pen_2_description_of_use", ""));
        tvDescOfUseData1.setText(prefs.getString("pen_2_ingredients", ""));
        tvDescOfUseData2.setText(prefs.getString("pen_2_name_of_color", ""));
        tvDescOfUseData3.setText(prefs.getInt("pen_2_amount_of_color", 0) + "");
        tvDescOfUseData4.setText(prefs.getString("pen_2_before", ""));
        tvDescOfUseData5.setText(prefs.getString("pen_2_after", ""));
        tvDescOfUseData6.setText(prefs.getString("pen_2_name","") + " " + prefs.getString("pen_2_address", ""));
    }

    private int calcRemainColor(String pen) {
        int amountOfColor = prefs.getInt(pen+"_amount_of_color", 0);
        long durationOfUse = prefs.getLong(pen+"_duration", 0);

        int result = (amountOfColor - ((int)(durationOfUse/1000) * AMOUNT_OF_INK_IN_ML_FOR_PER_SECOND));
        if (result < 0) {
            result = 0;
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_SCANNER) {
                Log.d("ok", data.getExtras().getString("content"));
//                String json = prefs.getString("json", "{}");
                try {
                    jsonObj = new JSONObject(data.getExtras().getString("content"));
//                    jsonObj = new JSONObject(json);
                    int pen = data.getExtras().getInt("PEN");
                    if (pen == 1) {
                        sendUiUpdatePen1();
                    } else {
                        sendUiUpdatePen2();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private void createUpdateUiHandler() {
        if(updateUIHandler == null) {
            updateUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    if(msg.what == UPDATE1) {
                        try {
                            tvDescOfUseData.setText(jsonObj.getString("description_of_use"));
                            tvDescOfUseData1.setText(jsonObj.getString("ingredients"));
                            tvDescOfUseData2.setText(jsonObj.getString("name_of_color"));
                            tvDescOfUseData3.setText(jsonObj.getString("amount_of_color"));
                            JSONObject life = jsonObj.getJSONObject("shelf_life");
                            tvDescOfUseData4.setText(life.getString("before"));
                            tvDescOfUseData5.setText(life.getString("after"));
                            JSONObject comp = jsonObj.getJSONObject("company");
                            tvDescOfUseData6.setText(comp.getString("name") + " " + comp.get("address"));
                            //
                            editor.putString("pen_1_description_of_use",jsonObj.getString("description_of_use"));
                            editor.putString("pen_1_ingredients",jsonObj.getString("ingredients"));
                            editor.putString("pen_1_name_of_color",jsonObj.getString("name_of_color"));
                            editor.putInt("pen_1_amount_of_color",jsonObj.getInt("amount_of_color"));
                            editor.putString("pen_1_before",life.getString("before"));
                            editor.putString("pen_1_after",life.getString("after"));
                            editor.putString("pen_1_name",comp.getString("name"));
                            editor.putString("pen_1_address",comp.getString("address"));
                            //
                            editor.putLong("pen_1_duration", 0);
                            editor.commit();
                            //
                            tvColorRemain.setText( getResources().getString(R.string.color_remain) + " " + calcRemainColor("pen_1") + " ml");
                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (msg.what == UPDATE2) {
                        try {
                            tvDescOfUseData.setText(jsonObj.getString("description_of_use"));
                            tvDescOfUseData1.setText(jsonObj.getString("ingredients"));
                            tvDescOfUseData2.setText(jsonObj.getString("name_of_color"));
                            tvDescOfUseData3.setText(jsonObj.getString("amount_of_color"));
                            JSONObject life = jsonObj.getJSONObject("shelf_life");
                            tvDescOfUseData4.setText(life.getString("before"));
                            tvDescOfUseData5.setText(life.getString("after"));
                            JSONObject comp = jsonObj.getJSONObject("company");
                            tvDescOfUseData6.setText(comp.getString("name") + " " + comp.get("address"));
                            //
                            //
                            editor.putString("pen_2_description_of_use",jsonObj.getString("description_of_use"));
                            editor.putString("pen_2_ingredients",jsonObj.getString("ingredients"));
                            editor.putString("pen_2_name_of_color",jsonObj.getString("name_of_color"));
                            editor.putInt("pen_2_amount_of_color",jsonObj.getInt("amount_of_color"));
                            editor.putString("pen_2_before",life.getString("before"));
                            editor.putString("pen_2_after",life.getString("after"));
                            editor.putString("pen_2_name",comp.getString("name"));
                            editor.putString("pen_2_address",comp.getString("address"));
                            //
                            editor.putLong("pen_2_duration", 0);
                            //
                            editor.commit();
                            //
                            tvColorRemain.setText( getResources().getString(R.string.color_remain) + " " + calcRemainColor("pen_2") + " ml");
                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
        }
    }

    private void sendUiUpdatePen1() {
        Message message = new Message();
        message.what = UPDATE1;
        updateUIHandler.sendMessage(message);
    }

    private void sendUiUpdatePen2() {
        Message message = new Message();
        message.what = UPDATE2;
        updateUIHandler.sendMessage(message);
    }
}
