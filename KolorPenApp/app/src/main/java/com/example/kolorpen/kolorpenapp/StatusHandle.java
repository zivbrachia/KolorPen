package com.example.kolorpen.kolorpenapp;

import android.content.SharedPreferences;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

public class StatusHandle implements Cloneable {

    private int userId;
    private JSONObject customerDetails;
    private String userName;    // current_cust_id
    private Timestamp date;
    private int penIndex;
    private int volume;
    private int dps;
    private boolean pedalPressed;
    private long pressDuration;
    private long startPressedTime;
    private boolean isStatusChanged;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public int getUserId() {
        return userId;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void changeOccured() {
        this.isStatusChanged = true;
        this.date = new Timestamp(System.currentTimeMillis());
    }

    private void setCustomer(JSONObject customer) {
        this.customerDetails = customer;
    }
    public void setUserId(int userId, JSONObject customer) {
        if (this.userId!= userId) {
            changeOccured();
        }
        this.userId = userId;
        this.setCustomer(customer);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName, JSONObject customer) {
        if (this.userName!= userName) {
            changeOccured();
        }
        this.userName = userName;
        this.setCustomer(customer);

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getPenIndex() {
        return penIndex;
    }

    public void setPenIndex(int penIndex) {
        if (this.penIndex != penIndex) {
            changeOccured();
        }
        this.penIndex = penIndex;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (this.volume != volume) {
            changeOccured();
        }
        this.volume = volume;
        //
        saveVolumeValue();

    }

    private void saveVolumeValue() {
        try {
            if (this.penIndex == 1) {
                editor.putInt("Pen_1_Volume", this.volume);
                customerDetails.put("pen_1_volume", this.volume);
            } else if (this.penIndex == 2) {
                editor.putInt("Pen_2_Volume", this.volume);
                customerDetails.put("pen_2_volume", this.volume);
            }
            editor.putString(this.userName, customerDetails.toString());
            editor.commit();
        } catch (JSONException e) {

        }
    }

    private void saveDpsValue() {
        try {
            if (this.penIndex == 1) {
                editor.putInt("Pen_1_Dps", this.dps);
                customerDetails.put("pen_1_dps", this.dps);
            } else if (this.penIndex == 2) {
                editor.putInt("Pen_2_Dps", this.dps);
                customerDetails.put("pen_2_dps", this.dps);
            }
            editor.putString(this.userName, customerDetails.toString());
            editor.commit();
        } catch (JSONException e) {

        }
    }

    public int getDps() {
        return dps;
    }

    public boolean isStatusChanged() {
        return this.isStatusChanged;
    }

    public void setIsStatusChanged(boolean isStatusChanged) {
        this.isStatusChanged = isStatusChanged;
    }

    public void setDps(int dps) {
        if (this.dps != dps) {
            changeOccured();
        }
        this.dps = dps;

        saveDpsValue();
    }

    public boolean isPedalPressed() {
        return pedalPressed;
    }

    @Override
    public String toString() {
        return "time: " + date.toString() + ", username: " + userName + ", Pen: " + penIndex + ", Volume: " + volume + ", DPS: " + dps + ", isPressed: " + pedalPressed + ", Duration: " + pressDuration;
    }

    public void setPedalPressed(boolean pedalPressed) {
        if (this.pedalPressed != pedalPressed) {
            changeOccured();
        }
        if (pedalPressed) {
            startPressedTime = System.currentTimeMillis();
            this.pressDuration = 0;
        } else {
            this.pressDuration = System.currentTimeMillis() - startPressedTime;
        }
        this.pedalPressed = pedalPressed;
    }

    public long getPressDuration() {
        return pressDuration;
    }

    public void resetPressDuration() {
        pressDuration = 0;
    }

    public StatusHandle(int userId, String userName, int penIndex, int volume, int dps, boolean pedalPressed, SharedPreferences.Editor editor, SharedPreferences prefs, JSONObject customer) {
        this.userId = userId;
        this.userName = userName;
        this.date = new Timestamp(System.currentTimeMillis());
        this.penIndex = penIndex;
        this.volume = volume;
        this.dps = dps;
        this.pedalPressed = pedalPressed;
        this.isStatusChanged = false;
        this.editor = editor;
        this.prefs = prefs;
        this.customerDetails = customer;
    }
}
