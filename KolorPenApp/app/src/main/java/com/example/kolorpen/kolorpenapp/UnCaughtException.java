package com.example.kolorpen.kolorpenapp;

import android.content.Context;
import android.util.Log;

public class UnCaughtException implements Thread.UncaughtExceptionHandler {
        private Context context;
        private static Context context1;
       public UnCaughtException(Context ctx) {
            context = ctx;
            context1 = ctx;
        }

        public void uncaughtException(Thread t, Throwable e) {
            try {

                Log.e(UnCaughtException.class.getName(),e.getStackTrace().toString());

            } catch (Throwable ignore) {
                Log.e(UnCaughtException.class.getName(),
                        "Error while sending error e-mail");
            }
        }

    }

