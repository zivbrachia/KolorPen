package com.example.kolorpen.kolorpenapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigInteger;
import java.util.Map;

public class CodeScanner extends AppCompatActivity {

    Button btnScan;
    ImageView imageView;
    private String privateKey;
    private String publicKey;
    private byte[] encodeData = null;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);

        btnScan = (Button)findViewById(R.id.btnScan);
        imageView = (ImageView)findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });

        bundle = getIntent().getExtras();
        if(bundle != null) {
            if (bundle.getBoolean("START_SCAN")) {
                startScan();
            }
        }


//        try {
//            Map<String, Object> keyMap = rsa.initKey();
//            publicKey = rsa.getPublicKey(keyMap);
//            privateKey = rsa.getPrivateKey(keyMap);
//            encrypt();
//            decrypt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void encrypt() {
        String publicKey = getPublicKey();
        String rsaStr = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiWml2IEJyYWNoaWEiLCJhZG1pbiI6dHJ1ZSwidHlwZSI6ImNvbG9yIiwiY29sb3IiOiJibGFjayJ9.jR7IVUlB1Jg6rU8qCPvHSgAB16QksGKkbwQhcChz7Z0";

        byte[] rsaData = rsaStr.getBytes();

        try {
            encodeData = rsa.encryptByPublicKey(rsaData, publicKey);
            String encodeStr = new BigInteger(1, encodeData).toString();
            Log.d("encodeStr", encodeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decrypt() {
        String privateKey = getPrivateKey();
        try {
            byte[] decodeData = rsa.encryptByPrivateKey(encodeData, privateKey);
            String decodeStr = new String(decodeData);
            JWT jwt = new JWT(decodeStr.trim());
            Log.d("decodeStr", decodeStr.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPublicKey() {
        return publicKey;
    }

    private String getPrivateKey() {
        return privateKey;
    }

    private void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(CodeScanner.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // https://github.com/auth0/JWTDecode.Android
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
                if (bundle.getBoolean("START_SCAN")) {
                    Intent intent = new Intent();
                    intent.putExtra("content", "");
                    intent.putExtra("status", "cancelled");
                    intent.putExtra("PEN", bundle.getInt("PEN"));
                    setResult(RESULT_OK, intent);
                    finish();
                }

            } else {
                Log.e("Scan", "Scanned");

                //tv_qr_readTxt.setText(result.getContents());
                Log.d("content", result.getContents());
                if (bundle.getBoolean("START_SCAN")) {
                    Intent intent = new Intent();
                    intent.putExtra("content", result.getContents());
                    intent.putExtra("status", "scanned");
                    intent.putExtra("PEN", bundle.getInt("PEN"));
                    setResult(RESULT_OK, intent);
                    finish();
                }


//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
