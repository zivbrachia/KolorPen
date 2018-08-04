package com.example.kolorpen.kolorpenapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class customer_add_update extends AppCompatActivity {

    ImageView medicalAggrement;
    Button btnBefore, btnAfter;
    EditText etName, etDesc;

    String mCurrentPhotoPath;
    JSONObject customerJson;
    String custId;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    static final int REQUEST_TAKE_PHOTO_MEDICAL = 1;
    static final int REQUEST_TAKE_PHOTO_BEFORE = 2;
    static final int REQUEST_TAKE_PHOTO_AFTER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_update);

        prefs = getApplication().getSharedPreferences("KolorPenApp", getApplicationContext().MODE_PRIVATE);
        editor = prefs.edit();

        medicalAggrement = (ImageView)findViewById(R.id.medicalAggrement);
        btnBefore = (Button)findViewById(R.id.btnBefore);
        btnAfter = (Button)findViewById(R.id.btnAfter);
        etName = (EditText) findViewById(R.id.etName);
        etDesc = (EditText) findViewById(R.id.etDesc);
        //
        custId = prefs.getString("current_cust_id", null);
        if (custId != null) {
            if (customerExist(custId)) {
                customerJson = getCustomer(custId);
                try {
                    if (customerJson.has("name")) {
                        etName.setText(customerJson.getString("name"));
                    }
                    if (customerJson.has("desc")) {
                        etDesc.setText(customerJson.getString("desc"));
                    }
                } catch (JSONException e) {

                }
            }
        }
        //


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {

                }
            }
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("after", s.toString().toLowerCase());
                custId = "cust_" + s.toString().toLowerCase();
                String name = s.toString().toLowerCase();
                if (name.length()==0) {
                    clearInputFields();
                } else {
                    if (customerExist(custId)) {
                        customerJson = getCustomer(custId);
                    } else {
                        customerJson = createCustomer(custId, name);
                    }
                    // load to screen
                    try {
                        if (customerJson.has("desc")) {
                            etDesc.setText(customerJson.getString("desc"));
                        } else {
                            etDesc.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!isHasValue(etName)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_is_must), Toast.LENGTH_LONG).show();
                    if (after!=0) {
                        etDesc.setText("");
                    }
                    etName.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isHasValue(etName)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_is_must), Toast.LENGTH_LONG).show();
                } else {
                    try {
                        customerJson.put("desc", s.toString());
                        saveCustomer(custId, customerJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.clearFocus();
                btnBefore.requestFocus();
                if (!isHasValue(etName)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_is_must), Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (customerJson.has("before_photo")) {
                            showPicture(customerJson.getString("before_photo"));
                        } else {
                            dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_BEFORE, "JPG_BEFORE");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.clearFocus();
                btnAfter.requestFocus();
                if (!isHasValue(etName)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_is_must), Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (customerJson.has("after_photo")) {
                            showPicture(customerJson.getString("after_photo"));
                        } else {
                            dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_AFTER, "JPG_AFTER");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        medicalAggrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.clearFocus();
                medicalAggrement.requestFocus();
                if (!isHasValue(etName)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_is_must), Toast.LENGTH_LONG).show();
                } else {
                    try {
                        //showPicture(customerJson.getString("medical_photo"));
                        if (customerJson.has("medical_photo")) {
                            showPicture(customerJson.getString("medical_photo"));
                        } else {
                            dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_MEDICAL, "JPG_MEDIC");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void clearInputFields() {
        customerJson = null;
        etDesc.setText("");
    }

    private boolean customerExist(String custId) {
        custId = custId.toLowerCase();
        if (prefs.getString(custId, null) == null) {
            return false;
        } else {
            return true;
        }
    }

    private JSONObject getCustomer(String custId) {
        custId = custId.toLowerCase();
        String customerDetails = prefs.getString(custId, null);
        if (customerDetails==null) {
            return null;
        }
        try {
            customerJson = new JSONObject(customerDetails);
            // update screen with the customer details;
            return customerJson;
        } catch (JSONException e) {
//            e.printStackTrace();
            return null;
        }
    }

    private JSONObject createCustomer(String custId, String name) {
        custId = custId.toLowerCase();
        name = name.toLowerCase();
        try {
            customerJson = new JSONObject("{}");
            customerJson.put("name", name);

            saveCustomer(custId, customerJson);
            return customerJson;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveCustomer(String name, JSONObject json) {
        editor.putString(name, json.toString());
        editor.commit();
    }

    private boolean isHasValue(EditText et) {
        if (et.getText().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void showPicture(String filenamePrefix) {
        String imageFileName = filenamePrefix;/// + "." + fileNameSuffix; //"JPG_20180801_101103_661206096";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageFileName), "image/*");
        startActivity(intent);
    }

    private void dispatchTakePictureIntent(int resultActivity, String txt) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(txt);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("error", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this, "com.example.kolorpen.kolorpenapp", photoFile);
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, resultActivity);
            }
        }
    }

    private File createImageFile(String txt) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = txt + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (customerJson==null) {
            return;
        }
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_TAKE_PHOTO_MEDICAL) {
                    customerJson.put("medical_photo", mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/")+1));
                }
            }
            if (requestCode == REQUEST_TAKE_PHOTO_BEFORE) {
                customerJson.put("before_photo", mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/")+1));
            }
            if (requestCode == REQUEST_TAKE_PHOTO_AFTER) {
                customerJson.put("after_photo", mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/")+1));
            }
            saveCustomer(custId, customerJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //
        String currentCustomer = prefs.getString("current_cust_id", null);
        if (currentCustomer != custId) {
            editor.remove("current_cust_id");
            editor.commit();
        }
        //
        Intent intent = new Intent();
        intent.putExtra("customer_id", custId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
