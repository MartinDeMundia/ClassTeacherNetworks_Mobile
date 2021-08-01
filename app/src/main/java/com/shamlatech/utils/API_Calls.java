package com.shamlatech.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.shamlatech.utils.Support.ShowAlertWithOutTitle;
import static com.shamlatech.utils.Support.hideProgress;
import static com.shamlatech.utils.Support.showProgress;


/**
 * Created by SUN on 30-01-2018.
 */

public class API_Calls {

    final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS).build();

    /*Retrofit instance*/
    public static Retrofit retrofit_static = new Retrofit.Builder()
            .baseUrl(Vars.Static_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /*Retrofit instance*/
    public static Retrofit retrofit_development = new Retrofit.Builder()
            .baseUrl(Vars.Live_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // public static RetrofitObjectAPIStatic service = retrofit_static.create(RetrofitObjectAPIStatic.class);
    public static RetrofitObjectAPI service = retrofit_development.create(RetrofitObjectAPI.class);
    static String url = "http://shamlatech.net/android_portal/school/";

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static void getRetro_Call(final Context context, Call<Object> call, final boolean progress, final OnApiResult onApiResult) {
        if (!NetworkUtil.isConnected(context)) {
            ShowAlertWithOutTitle(context, "Please Check your internet. It may turned off or slow");
        }
        if (progress)
            showProgress(Vars.Custom_Progress);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {

                    String data = call.request().url().toString() + " " + bodyToString(call.request().body());
                    Log.e("Access URL ", data);
                    if (progress)
                        hideProgress(Vars.Custom_Progress);
                    if (response != null) {
                        if (response.isSuccessful()) {
                            onApiResult.onSuccess(response);
                        } else {
                            String message = "API Error\nResponse Code : " + response.raw().code() +
                                    "\nError  Msg : " + response.raw().message() +
                                    "\nUrl : " + response.raw().request().url() +
                                    "\nMethod : " + response.raw().request().method();
                            Support.ShowAlertWithTitle(context, message);
                            Log.w("API Response Error", " " +
                                    "\nResponse Code : " + response.raw().code() +
                                    "\nError  Msg : " + response.raw().message() +
                                    "\nUrl : " + response.raw().request().url() +
                                    "\nMethod : " + response.raw().request().method());
                        }
                    } else {
                        Log.w("API : ", "Response  null ");
                    }
                } catch (Exception e) {
                    if (progress)
                        hideProgress(Vars.Custom_Progress);
                    e.printStackTrace();
                    Log.w("API Exception : ", e.getLocalizedMessage());

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (progress)
                    hideProgress(Vars.Custom_Progress);
                t.printStackTrace();
                onApiResult.onFailure(t.getMessage());
                Log.w("API onFailure :  ", "API call failed! " + t.getLocalizedMessage());
            }


        });
    }

    public static <T> T onPojoBuilder(Response<Object> objectResponse, Type type) {
        String stringJson = new Gson().toJson(objectResponse.body());
        Gson gson = new Gson();
       // Gson gson = new GsonBuilder() .setLenient() .create();
        return gson.fromJson(stringJson, type);
    }

    /*interface for response callbacks*/
    public interface APIResInter {
    }


    public interface OnApiResult {
        void onSuccess(Response<Object> objectResponse);

        void onFailure(String message);
    }


}
