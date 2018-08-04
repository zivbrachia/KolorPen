package com.example.kolorpen.kolorpenapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class SettingActivity extends AppCompatActivity {

    TextView tvQuickTutorial;
    TextView tvUserGuide;
    TextView tvApplicationUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        tvQuickTutorial = (TextView) findViewById(R.id.tvQuickTutorial);
        tvQuickTutorial.setText(R.string.quick_tutorial);

        tvUserGuide = (TextView) findViewById(R.id.tvUserGuide);
        tvUserGuide.setText(R.string.user_guide);

        tvApplicationUpdate = (TextView) findViewById(R.id.tvApplicationUpdate);
        tvApplicationUpdate.setText(R.string.app_update);

        tvQuickTutorial.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory() + "/kolorpen";
                String filename = getResources().getString(R.string.quick_tutorial_file);
                if (copyAsset(path, filename)) {
                    openPdf(path, filename);
                }
            }
        });

        tvUserGuide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory() + "/kolorpen";
                String filename = getResources().getString(R.string.user_guide_file);
                if (copyAsset(path, filename)) {
                    openPdf(path, filename);
                }
            }
        });

        tvApplicationUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                beep(5);
            }
        });
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

    private void openPdf(String path, String filename) {
        File file = new File(path +"/"+ filename);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private boolean copyAsset(String path, String filename) {
        try {
            InputStream in;
            OutputStream out = null;
            AssetManager assetManager = getAssets();
            in = assetManager.open(filename);
            File outFile = new File(path, filename);
            out = new FileOutputStream(outFile);
            //
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            //
            in.close();
//            in = null;
            out.flush();
            out.close();
//            out = null;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
