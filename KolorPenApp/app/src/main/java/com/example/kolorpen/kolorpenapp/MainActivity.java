package com.example.kolorpen.kolorpenapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.akaita.android.circularseekbar.CircularSeekBar;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    long totalDuration = 0;
    Queue<String> queueCommands = new LinkedList<String>();
    LostBluetoothConnectionReceiver lostBluetoothConnectionReceiver;
    // Check keep alive Connection
    volatile long lastKeepAlive;
    long lastHumanInteraction;
    // manage poll from queue
    Thread queueThread;
    ConcurrentLinkedQueue<StatusHandle> queue = new ConcurrentLinkedQueue<StatusHandle>();
    // read from bluetooth
    volatile boolean stopWorker;
    int readBufferPosition;
    byte[] readBuffer;
    Thread workerThread;
    // Status snapshot
    StatusHandle statusHandler;
    // SharedPreferences
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    // Bluetooth parameters
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ConnectBT connectBT;
    private Timer timer;
    //
    private int currentSelectedPen;
    private View greenRingDps, greenRingVolume;
    private View blueRingDps, blueRingVolume;
    private TextView duration, currentTime;
    private volatile ImageView btImageConnect, btImageDisconnect, turnOnProduct;
    private Button pen1, pen2, btnColor, btnSession;
    private Button btnSetting;
    private CircularSeekBar sBarVolume;
    private CircularSeekBar sBarDPS;
    private CircularSeekBar dpsSeekBar;
    //    private TextView customer;
    //
    // This is the activity main thread Handler.
    protected Handler updateUIHandler = null;
    // Message type code.
    protected  final static int REFRESH_TIME = 5;
    protected  final static int REFRESH_DURATION = 4;
    protected final static int START_KEEP_ALIVE = 3;
    protected final static int STOP_KEEP_ALIVE = 2;
    protected final static int BLUETOOTH_CONNECT_SUCCESS = 1;
    protected final static int BLUETOOTH_CONNECT_FAIL = 0;
    protected final static int BACKGROUND_ALERT = 11;
    protected final static int BACKGROUND = 12;
    protected final static int REFRESH_DPS_VOLUME = 13;
    final int AMOUNT_OF_INK_IN_ML_FOR_PER_SECOND = 3;

    @Override
    protected void onResume() {
        super.onResume();
//        if (!btSocket.isConnected()) {
//            try {
//                btSocket.connect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void onPause() {
//        try {
//            stopWorker = true;
//            //btSocket.close();
//        } catch (IOException e) {
//
//        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (lostBluetoothConnectionReceiver != null) {
            unregisterReceiver(lostBluetoothConnectionReceiver);
            lostBluetoothConnectionReceiver = null;
        }
        try {
            stopWorker = true;
            btSocket.close();
        } catch (IOException e) {

        }
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        try {
//            stopWorker = true;
//            btSocket.close();
//        } catch (IOException e) {
//
//        }
//
//        super.onBackPressed();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MainActivity.this));
        //
        sharedPref  = getApplication().getSharedPreferences("KolorPenApp", getApplicationContext().MODE_PRIVATE);
        editor = sharedPref.edit();
        //
        String cust_id = sharedPref.getString("current_cust_id", "root");
        String customerDetails = sharedPref.getString(cust_id, "{}");
        JSONObject customerJson = new JSONObject();
        try {
            customerJson = new JSONObject(customerDetails);
        } catch (JSONException e) {

        }

        statusHandler = new StatusHandle(1, cust_id, 1, 0, 0, false, editor, sharedPref, customerJson);
        //
        setLastInteraction(System.currentTimeMillis());
        address =  sharedPref.getString(getString(R.string.uniqueMacAddress), "");
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceListActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //
        sBarVolume = (CircularSeekBar) findViewById(R.id.seekBarVolume);
        //
        sBarDPS = (CircularSeekBar) findViewById(R.id.seekBarDPS);
//        customer = (TextView) findViewById(R.id.tvCustomer);
        //
//        sBarDPS.setProgressTextFormat(new DecimalFormat("###,###,##0.00"));
//        sBarDPS.setProgressTextFormat(new DecimalFormat("DPS #"));
        sBarDPS.setProgressTextFormat(new DecimalFormat(getResources().getString(R.string.dps) + " #"));
        sBarDPS.setRingColor(Color.GREEN);
        //
        sBarDPS.setOnCenterClickedListener(new CircularSeekBar.OnCenterClickedListener() {
            @Override
            public void onCenterClicked(CircularSeekBar seekBar, float progress) {
                //seekBar.prog
            }
        });

        editor.putBoolean("security", false);
        editor.commit();

        sBarDPS.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            float lastValue = 0;

            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {
                if (progress < 33) {
                    seekBar.setRingColor(Color.GREEN);
                } else if(progress < 66) {
                    seekBar.setRingColor(Color.YELLOW);
                } else {
                    seekBar.setRingColor(Color.RED);
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                lastValue = seekBar.getProgress();

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                try {
                    setDPS((int)seekBar.getProgress());
                } catch (IOException e) {
                    seekBar.setProgress(lastValue);
                }
            }
        });

//        sBarVolume.setProgressTextFormat(new DecimalFormat("Power #'%'"));
        sBarVolume.setProgressTextFormat(new DecimalFormat(getResources().getString(R.string.power) + " #"));
        sBarVolume.setRingColor(Color.GREEN);
        //
        sBarVolume.setOnCenterClickedListener(new CircularSeekBar.OnCenterClickedListener() {
            @Override
            public void onCenterClicked(CircularSeekBar seekBar, float progress) {
                seekBar.setProgress(0);
            }
        });

        sBarVolume.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            float lastValue = 0;
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {
                if (progress < 33) {
                    seekBar.setRingColor(Color.GREEN);
                } else if(progress < 66) {
                    seekBar.setRingColor(Color.YELLOW);
                } else {
                    seekBar.setRingColor(Color.RED);
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                try {
                    setVolume((int)seekBar.getProgress());
                } catch (IOException e) {
                    seekBar.setProgress(lastValue);
                }
            }
        });


        //call the widgets
        pen1 = (Button)findViewById(R.id.btnPen1);
        pen2 = (Button)findViewById(R.id.btnPen2);
        btnColor = (Button)findViewById(R.id.btnColor);
        btnSession = (Button)findViewById(R.id.session);
//        btnCustomer = (Button)findViewById(R.id.btnCustomer);
        btImageConnect = (ImageView) findViewById(R.id.imageConnect);
        btImageDisconnect= (ImageView) findViewById(R.id.imageDisconnect);
//        startTreatment = (Button)findViewById(R.id.startTreatment);
//        stopTreatment = (Button)findViewById(R.id.stopTreatment);
        greenRingDps = (View)findViewById(R.id.greenRingDps);
        greenRingVolume = (View)findViewById(R.id.greenRingVolume);
        blueRingDps = (View)findViewById(R.id.blueRingDps);
        blueRingVolume = (View)findViewById(R.id.blueRingVolume);
        duration = (TextView)findViewById(R.id.duration);
        currentTime = (TextView)findViewById(R.id.currentTime);
        btnSetting = (Button)findViewById(R.id.btnSettings);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutId);
        //turnOnProduct = (ImageView) findViewById(R.id.turnOnProduct);

        //turnOnProduct.setVisibility(View.INVISIBLE);
        btImageDisconnect.setVisibility(View.VISIBLE);

        lastKeepAlive = System.currentTimeMillis();
        createUpdateUiHandler();
        beginKeepAliveBluetoothConnection();
        setEnabled(false);

        connectBT = new ConnectBT(); //Call the class to connect
        connectBT.execute();

        //commands to be sent to bluetooth
        pen1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                turnOnPen1();
                setLastInteraction(System.currentTimeMillis());
            }
        });

        pen2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                turnOnPen2();
//                securityViolation(true);
                setLastInteraction(System.currentTimeMillis());
            }
        });

        btnSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastInteraction(System.currentTimeMillis());
                Intent intent = new Intent(MainActivity.this, customer_add_update.class);
                startActivityForResult(intent, CUSTOMER);
            }
        });

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastInteraction(System.currentTimeMillis());
                Intent intent = new Intent(MainActivity.this, ColorPickerActivity.class);
                startActivityForResult(intent, PICK_COLOR);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CodeScanner.class);
//                intent.putExtra("CUSTOMER_ID", "112233");
//                startActivityForResult(intent, CODE_SCANNER);
                setLastInteraction(System.currentTimeMillis());
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

//        btnCustomer.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                goToCustomer();
//            }
//        });

//        startTreatment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                goToTreatment(true);
//            }
//        });

//        stopTreatment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                goToTreatment(false);
//            }
//        });

        lostBluetoothConnectionReceiver = new LostBluetoothConnectionReceiver();
        lostBluetoothConnectionReceiver.setMainActivity(this);
        IntentFilter filter4 = new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED");
        registerReceiver(lostBluetoothConnectionReceiver, filter4);
    }

    static final int SET_TREATMENT_PARAMETERS = 4;  // The request code
    static final int SET_NEW_TREATMENT = 5;
    static final int STOP_TREATMENT = 6;
    static final int CODE_SCANNER = 7;
    static final int PICK_COLOR = 8;
    static final int CUSTOMER = 9;

    private void setLastInteraction(long millis) {
        lastHumanInteraction = millis;
    }

//    private void goToCustomer() {
//        Intent intent = new Intent(MainActivity.this, TreatmentListActivity.class);
//        intent.putExtra("CUSTOMER_ID", "112233");
//        startActivityForResult(intent, SET_TREATMENT_PARAMETERS);
//    }

//    private void goToTreatment(boolean isStartTreatment) {
//        Intent intent = new Intent(MainActivity.this, TreatmentActivity.class);
//        if (isStartTreatment) {
//            intent.putExtra("TREATMENT", "START");
//            startActivityForResult(intent, SET_NEW_TREATMENT);
//        } else {
//            intent.putExtra("TREATMENT", "STOP");
////            intent.putExtra("CUSTOMER", customer.getText());
//            startActivityForResult(intent, STOP_TREATMENT);
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_SCANNER) {
            if (resultCode == RESULT_OK) {
                Log.d("ok", "OK");
            }
        }

        if (requestCode == PICK_COLOR) {
            if (resultCode == RESULT_OK) {
                statusHandler.getPenIndex();
                if (statusHandler.getPenIndex()==1) {
                    if (!isLessThen("pen_1", 10)) {
                        sendBackground();
                    } else {
                        sendBackgroundAlert();
                        beep(3);
                    }
                } else if (statusHandler.getPenIndex()==2) {
                    if (!isLessThen("pen_2", 10)) {
                        sendBackground();
                    } else {
                        sendBackgroundAlert();
                        beep(3);
                    }
                }
            }
        }

        if (requestCode == CUSTOMER) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("customer_id")) {
                    setCurrentCustomer(data.getExtras().getString("customer_id"));
                }
            }
        }
    }

    private void setCurrentCustomer(String cust_id) {
        String currentCustomer = sharedPref.getString("current_cust_id", null);
        if ((currentCustomer == null) || ((currentCustomer != null) && (!currentCustomer.equals(cust_id)))) {
            editor.putString("current_cust_id", cust_id);
            editor.commit();
            updateStatus(cust_id);
            sendRefreshDpsAndVolume();
            totalDuration = 0;
            sendRefreshDuration();

        }

    }

    private void updateStatus(String cust_id) {
        String customerDetails = sharedPref.getString(cust_id, null);
        if (customerDetails == null) {
            statusHandler.setUserName(cust_id, new JSONObject());
        } else {
            try {
                JSONObject customerJson = new JSONObject(customerDetails);
                statusHandler.setUserName(cust_id, customerJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void setBluetoothConnectImage(boolean status) {
        if (status) {
            btImageConnect.setVisibility(View.VISIBLE);
            btImageDisconnect.setVisibility(View.INVISIBLE);
        } else {
            btImageConnect.setVisibility(View.INVISIBLE);
            btImageDisconnect.setVisibility(View.VISIBLE);
        }
    }

    private String getDpsCommand(int value) {
        return ("F " + String.valueOf(value) +"\n").toString();
    }

    private void setDPS(int value) throws  IOException {
        if (btSocket!=null)
        {
            btSocket.getOutputStream().write(("F " + String.valueOf(value) +"\n").toString().getBytes());
        }
    }

    private int calcVolume(int value, boolean... calcPercent) {
        boolean isCalcPercent = (calcPercent.length >= 1) ? calcPercent[0]: false;
        int flip;
        if (isCalcPercent) {
            flip = (int)Math.ceil((1023 - value) * 100 / 1023);
        } else {
            int volume = (int) Math.ceil(((double) value * (double) 1023) / 100.0);
            flip = 1023 - volume;
        }
        return flip;
    }

    private String getVolumeCommand(int value) {
        int volume = calcVolume(value);
        return ("O " + String.valueOf(volume) +"\n").toString();
    }

    private void setVolume(int value) throws IOException {
        if (btSocket!=null)
        {
            int volume = calcVolume(value);
            btSocket.getOutputStream().write(("O " + String.valueOf(volume) +"\n").toString().getBytes());
        }
    }

    private void turnOnPen1()   // request from hardware to turn on pen 1
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("P 1\n".toString().getBytes());
//                msg("Pen 1 request");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private AlertDialog buildDialog(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", listener);
        AlertDialog alert = builder.create();
//        alert.show();
        return alert;
    }

    private void errorAlert() {
        AlertDialog alert = null;
        DialogInterface.OnClickListener positiveBtn = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // something
            }
        };
        alert = buildDialog("Error!!!!", positiveBtn);
        alert.show();
    }

    private void securityViolation(boolean forceAlert) {
        AlertDialog alert = null;
        if (sharedPref.getBoolean("security", false) == true) {

        } else {
            editor.putBoolean("security", true);
            editor.commit();

            DialogInterface.OnClickListener positiveBtn = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // something
                }
            };

            alert = buildDialog("Security Violation!!!!", positiveBtn);
            alert.show();
        }
    }

    private void pen1IsOn() {    // hardware acknowledge for turning on pen 1;
        pen1.setEnabled(false);
        pen2.setEnabled(true);
        greenRingDps.setVisibility(View.VISIBLE);
        greenRingVolume.setVisibility(View.VISIBLE);
        blueRingDps.setVisibility(View.INVISIBLE);
        blueRingVolume.setVisibility(View.INVISIBLE);

        int dps =  sharedPref.getInt("Pen_1_Dps", 0);
        int volume =  sharedPref.getInt("Pen_1_Volume", 0);
        queueCommands.clear();
        queueCommands.add(getDpsCommand(dps));
        queueCommands.add(getVolumeCommand(volume));
        treatQueueCommand();

        if (isLessThen("pen_1", 10)) {
            beep(3);
            sendBackgroundAlert();
        } else {
            sendBackground();
        }
    }

    private void pen2IsOn() {    // hardware acknowledge for turning on pen 2;
        pen1.setEnabled(true);
        pen2.setEnabled(false);
        greenRingDps.setVisibility(View.INVISIBLE);
        greenRingVolume.setVisibility(View.INVISIBLE);
        blueRingDps.setVisibility(View.VISIBLE);
        blueRingVolume.setVisibility(View.VISIBLE);

        int dps =  sharedPref.getInt("Pen_2_Dps", 0);
        int volume =  sharedPref.getInt("Pen_2_Volume", 0);

        queueCommands.clear();
        queueCommands.add(getDpsCommand(dps));
        queueCommands.add(getVolumeCommand(volume));
        treatQueueCommand();

        if (isLessThen("pen_2", 10)) {
            beep(3);
            sendBackgroundAlert();
        } else {
            sendBackground();
        }
    }

    private void turnOnPen2()   // request from hardware to turn on pen 2
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("P 2\n".toString().getBytes());
//                msg("Pen 2 request");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            try {
                progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            } catch (NullPointerException e) {
                e.getMessage();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            try {
                if (!ConnectSuccess) {
                    DialogInterface.OnClickListener positiveBtn = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    };

                    setBluetoothConnectImage(false);
                    AlertDialog alert = buildDialog("Connection Failed. Is it a KolorPen Bluetooth?", positiveBtn);
                    alert.show();
                } else {
                    msg("Connected.");
                    isBtConnected = true;
                    setEnabled(true);
                    setBluetoothConnectImage(true);
                    editor.putString(getString(R.string.uniqueMacAddress), address);
                    editor.commit();
                    beginListenForData();
                    beginListenForQueue();
                    turnOnPen1();
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            progress.dismiss();
        }
    }

    private void addDuration(String name, long currentDuration) {
        long duration = sharedPref.getLong((name), 0);
        editor.putLong(name, currentDuration + duration);
        editor.commit();
    }

    private boolean isLessThen(String pen, int percent) {
        int amountOfColor = sharedPref.getInt(pen+"_amount_of_color", 0);
        long durationOfUse = sharedPref.getLong(pen+"_duration", 0);
        int limit = (int)(amountOfColor * percent/100);

        if ((durationOfUse/1000) * AMOUNT_OF_INK_IN_ML_FOR_PER_SECOND >= (amountOfColor-limit)) {
            return true;
        }
        return false;
    }

    private void alertForColor(String pen) {
        if (isLessThen(pen, 10)) {
            beep(3);
            sendBackgroundAlert();
        }
    }

    private void beep(Integer times) {
        final int durationMs = 300;
        int delay = (int) (durationMs * 1.7);
        Timer timer[] = new Timer[times];
        for (int i = 0; i < times; i++) {
            timer[i] = new Timer();
            timer[i].schedule(new TimerTask() {
                @Override
                public void run() {
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, durationMs);
                }
            }, delay * i);
        }
    }

    void beginListenForQueue() {
        queueThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (!Thread.currentThread().isInterrupted() && !queue.isEmpty()) {
                        StatusHandle current = queue.poll();
                        if (current.isPedalPressed()==false && current.getPressDuration()>0) {
                            totalDuration = totalDuration + current.getPressDuration();
                            sendRefreshDuration();
                            //
                            if (current.getPenIndex()==1) {
                                addDuration("pen_1_duration", current.getPressDuration());
                                alertForColor("pen_1");
                            } else if (current.getPenIndex()==2) {
                                addDuration("pen_2_duration", current.getPressDuration());
                                alertForColor("pen_2");
                            }
                            //
                            statusHandler.resetPressDuration();
                        }
                        JSONObject obj = makeJsonObj(current);
                        Log.d("status" , current.toString());
                        try {
                            objectToFile(obj);
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        queueThread.start();
    }

    private void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = btSocket.getInputStream().available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            btSocket.getInputStream().read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            treatCommand(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (UnsupportedEncodingException e) {
                        stopWorker = true;
                    }
                    catch (NullPointerException ex) {
                        stopWorker = true;
                    }
                    catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    @SuppressLint("HandlerLeak")
    private void createUpdateUiHandler() {
        if(updateUIHandler == null) {
            updateUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    if(msg.what == BLUETOOTH_CONNECT_SUCCESS) {
                        // Update ui in main thread.
                        setBluetoothConnectImage(true);
                        tryConnecting = 0;
                        if (!workerThread.isAlive()) {
                            beginListenForData();
                        }
                        if (!queueThread.isAlive()) {
                            beginListenForQueue();
                        }
                    } else if (msg.what == BLUETOOTH_CONNECT_FAIL ) {
                        setBluetoothConnectImage(false);
                    } else if (msg.what == STOP_KEEP_ALIVE) {
                        timer.cancel();
                    } else if (msg.what == START_KEEP_ALIVE) {
//                        tryConnecting++;
                        //if (tryConnecting >=3) {
                            //timer.cancel();
                        //} else {
                            beginKeepAliveBluetoothConnection();
                        //}
                    } else if (msg.what == REFRESH_DURATION) {
                        @SuppressLint("DefaultLocale") String myTime =  String.format("%02d:%02d:%02d",
                                //Hours
                                TimeUnit.MILLISECONDS.toHours(totalDuration) -
                                        TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(totalDuration)),
                                //Minutes
                                TimeUnit.MILLISECONDS.toMinutes(totalDuration) -
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalDuration)),
                                //Seconds
                                TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration)));
                        duration.setText(myTime);
                    } else if (msg.what == REFRESH_TIME) {
                        long yourmilliseconds = System.currentTimeMillis();
                        Date resultdate = new Date(yourmilliseconds);
                        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
                        currentTime.setText(format.format(resultdate));
                        //Calendar c = Calendar.getInstance();
                        //currentTime.setText(c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));
                    } else if (msg.what == BACKGROUND_ALERT) {
                        constraintLayout.setBackgroundResource(R.drawable.screen_background_alert);
                    } else if (msg.what == BACKGROUND) {
                        constraintLayout.setBackgroundResource(R.drawable.screen_background);
                    } else if (msg.what == REFRESH_DPS_VOLUME) {
                        // Get from customer last dps and volume
                        String cust_id = sharedPref.getString("current_cust_id", null);
                        String customerDetails = sharedPref.getString(cust_id, null);
                        if (customerDetails==null) {

                        } else {
                            try {
                                JSONObject customerJson = new JSONObject(customerDetails);
                                if (customerJson.has("pen_1_dps")) {
                                    editor.putInt("Pen_1_Dps", customerJson.getInt("pen_1_dps"));

                                } else {
                                    editor.putInt("Pen_1_Dps", 0);
                                }
                                if (customerJson.has("pen_1_volume")) {
                                    customerJson.getInt("pen_1_volume");
                                    editor.putInt("Pen_1_Volume", customerJson.getInt("pen_1_volume"));
                                } else {
                                    editor.putInt("Pen_1_Volume", 0);
                                }
                                if (customerJson.has("pen_2_dps")) {
                                    editor.putInt("Pen_2_Dps", customerJson.getInt("pen_2_dps"));

                                } else {
                                    editor.putInt("Pen_2_Dps", 0);
                                }
                                if (customerJson.has("pen_2_volume")) {
                                    customerJson.getInt("pen_2_volume");
                                    editor.putInt("Pen_2_Volume", customerJson.getInt("pen_2_volume"));
                                } else {
                                    editor.putInt("Pen_2_Volume", 0);
                                }
                                editor.commit();
                                turnOnPen1();
                            } catch (JSONException e) {

                            }
                        }
                        Log.d("haleluya", "hello");
                    }
                }
            };
        }
    }
    private int tryConnecting;
    float dpsLastProgress = 0;
    float volumeLastProgress = 0;

    private void beginKeepAliveBluetoothConnection() {
        //
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                if ((currentTimeMillis - lastKeepAlive) >= (5 * 1000))  {
                    boolean connectSuccess = true;
                    try {
                        btSocket.connect();
                    } catch (IOException e) {
                        connectSuccess = false;
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        connectSuccess = false;
                        e.printStackTrace();
                    }

                    if (connectSuccess) {
                        sendBTConnecionSuccess();
                    } else {
                        sendBTConnecionFail();
                        sendStartKeepAlive();
                    }
                }
//                if (currentTimeMillis >= (10 * 1000)) {
//                    sBarVolume.setVisibility(View.GONE);
//                    sBarDPS.setVisibility(View.GONE);
//                    turnOnProduct.setVisibility(View.VISIBLE);
//                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, (5 * 1000));
    }

    private void sendBackground() {
        Message message = new Message();
        message.what = BACKGROUND;
        updateUIHandler.sendMessage(message);
    }

    private void sendBackgroundAlert() {
        Message message = new Message();
        message.what = BACKGROUND_ALERT;
        updateUIHandler.sendMessage(message);
    }

    private void sendRefreshTime() {
        Message message = new Message();
        message.what = REFRESH_TIME;
        updateUIHandler.sendMessage(message);
    }

    private void sendRefreshDuration() {
        Message message = new Message();
        message.what = REFRESH_DURATION;
        updateUIHandler.sendMessage(message);
    }

    private void sendRefreshDpsAndVolume() {
        Message message = new Message();
        message.what = REFRESH_DPS_VOLUME;
        updateUIHandler.sendMessage(message);
    }

    private void sendStartKeepAlive() {
        Message message = new Message();
        message.what = START_KEEP_ALIVE;
        updateUIHandler.sendMessage(message);
    }

    private void sendBTConnecionSuccess() {
        Message message = new Message();
        message.what = BLUETOOTH_CONNECT_SUCCESS;
        updateUIHandler.sendMessage(message);
    }

    private void sendBTConnecionFail() {
        Message message = new Message();
        message.what = BLUETOOTH_CONNECT_FAIL;
        updateUIHandler.sendMessage(message);
    }

    private JSONObject makeJsonObj(StatusHandle statusHandler) {
        JSONObject obj = new JSONObject();
        JSONObject user = new JSONObject();
        JSONObject device = new JSONObject();
        JSONObject pedal = new JSONObject();
        try {
            user.put("userId", statusHandler.getUserId());
            user.put("userName", statusHandler.getUserName());

            pedal.put("pedalPressed", statusHandler.isPedalPressed());
            pedal.put("pressedDuration", statusHandler.getPressDuration());

            device.put("volume", statusHandler.getVolume());
            device.put("dps", statusHandler.getDps());
            device.put("pen", statusHandler.getPenIndex());
            device.put("pedal", pedal);

            obj.put("timestamp", statusHandler.getDate());
            obj.put("user", user);
            obj.put("device", device);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;

    }

    private void objectToFile(JSONObject json) throws IOException {
        String filepath = Environment.getExternalStorageDirectory().getPath() + "/KolorPen";

        File dir = new File(filepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filepath += "/log.json";
        File file = new File(filepath);
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, true));
        outputStreamWriter.append(json.toString() + ",");
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    void setEnabled(boolean enabled) {
        return;
//        if (enabled == false) {
//            if (pen1.isEnabled()) {
//                currentSelectedPen = 1;
//            } else {
//                currentSelectedPen = 2;
//            }
//            pen1.setEnabled(enabled);
//            pen2.setEnabled(enabled);
//        }
//
//        if (enabled == true) {
//            if (currentSelectedPen==1) {
//                pen1.setEnabled(enabled);
//                pen2.setEnabled(false);
//            } else {
//                pen1.setEnabled(false);
//                pen2.setEnabled(enabled);
//            }
//        }
//
//        sBarVolume.setEnabled(enabled);
//        sBarDPS.setEnabled(enabled);
    }

    void treatCommand(String command) {
        if (command.equals("E 1")) {
            errorAlert();
        } else if (command.equals("S 1")) {
            securityViolation(false);
        } else if (command.equals("p 0")) {
            statusHandler.setPedalPressed(false);
            setEnabled(true);
        } else if (command.equals("p 1")) {
            setEnabled(false);
            statusHandler.setPedalPressed(true);
        } else if (command.equals("command from Serial: P 1")) {
            statusHandler.setPenIndex(1);
            pen1IsOn();
        } else if (command.equals("command from Serial: P 2")) {
            statusHandler.setPenIndex(2);
            pen2IsOn();
        } else if (command.contains("command from Serial: F ")) { // dps
            String[] list = command.split("command from Serial: F ");
            int value = Integer.parseInt(list[1]);
            sBarDPS.setProgress(value);
            statusHandler.setDps(value);
            // save value to cust_id
        } else if (command.contains("command from Serial: O ")) { // volume
            String[] list = command.split("command from Serial: O ");
            int value = Integer.parseInt(list[1]);
            int progress = calcVolume(value, true);
            sBarVolume.setProgress(progress);
            statusHandler.setVolume(progress);
            // save value to cust_id
        }  else {
            lastKeepAlive = System.currentTimeMillis();
            sendRefreshTime();
            Log.d("keep-alive", command);
            if (lastKeepAlive > (lastHumanInteraction + (1000*60*20))) {
                AlertDialog alert = null;
                DialogInterface.OnClickListener positiveBtn = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // something
                    }
                };
                alert = buildDialog("No use of the application, please turn off the device", positiveBtn);
                alert.show();
                //finish();
            }
        }

        if (statusHandler.isStatusChanged()) {
            try {
                queue.add((StatusHandle) statusHandler.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            } finally {
                statusHandler.setIsStatusChanged(false);
            }
        }

        treatQueueCommand();
    }

    private void treatQueueCommand() {
        if (!queueCommands.isEmpty()) {
            String command = queueCommands.poll();
            if (btSocket!=null)
            {
                try {
                    btSocket.getOutputStream().write(command.getBytes());
                } catch (IOException e) {
                    queueCommands.clear();
                }
            }

        }
    }
}


