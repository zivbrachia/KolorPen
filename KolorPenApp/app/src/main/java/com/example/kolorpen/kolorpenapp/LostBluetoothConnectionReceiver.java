package com.example.kolorpen.kolorpenapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

public class LostBluetoothConnectionReceiver extends BroadcastReceiver {

    private boolean connectSuccess = true; //if it's here, it's almost connected
    MainActivity main = null;

    public void setMainActivity(MainActivity main)
    {
        this.main = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        final PendingResult pendingResult = goAsync();
        //
//        sendStopKeepAlive();
        sendBTConnecionFail();
        //
        try {
            main.btSocket.connect();//start connection
        } catch (IOException e) {
            connectSuccess = false;
            Log.d("error", e.getMessage());
        }

        if (connectSuccess) {
            sendBTConnecionSuccess();
        } else {
            sendBTConnecionFail();
            sendStartKeepAlive();

        }
        pendingResult.finish();
    }

//    private void sendStopKeepAlive() {
//        Message message = new Message();
//        message.what = main.STOP_KEEP_ALIVE;
//        main.updateUIHandler.sendMessage(message);
//    }

    private void sendStartKeepAlive() {
        Message message = new Message();
        message.what = main.START_KEEP_ALIVE;
        main.updateUIHandler.sendMessage(message);
    }

    private void sendBTConnecionFail() {
        Message message = new Message();
        message.what = main.BLUETOOTH_CONNECT_FAIL;
        main.updateUIHandler.sendMessage(message);
    }

    private void sendBTConnecionSuccess() {
        Message message = new Message();
        message.what = main.BLUETOOTH_CONNECT_SUCCESS;
        main.updateUIHandler.sendMessage(message);
    }
}