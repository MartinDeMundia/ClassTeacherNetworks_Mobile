package com.shamlatech.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class PaymentsUtil {
    private static PaymentsUtil instance;
    private ApiService apiService;

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private PaymentsUtil() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .baseUrl("http://www.classteacher.school/")
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static PaymentsUtil getInstance() {
        if (instance == null) {
            instance = new PaymentsUtil();
        }
        return instance;
    }

    public Call<ResponseBody> initiatePayment(String name, String email, String amount, String phone) {
        return apiService.initiatePayment(name, email, amount, phone);
    }

    public Call<ResponseBody> history(String userId) {
        return apiService.history(userId);
    }

    public interface ApiService {
        @POST("http://www.classteacher.school/ipay/pay.php")
        @FormUrlEncoded
        Call<ResponseBody> initiatePayment(@Field("username") String name, @Field("email") String email,
                                           @Field("amount") String amount, @Field("phone") String phone);

        @GET("http://www.classteacher.school/ipay/history.php")
        Call<ResponseBody> history(@Query("user_id") String userId);
    }

}
