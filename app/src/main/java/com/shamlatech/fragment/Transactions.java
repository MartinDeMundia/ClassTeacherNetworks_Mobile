package com.shamlatech.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shamlatech.adapter.TransactionAdapter;
import com.shamlatech.api_response.Res_UserInfo;
import com.shamlatech.pojo.Transaction;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.PaymentsUtil;
import com.shamlatech.utils.Support;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Transactions extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    List<Transaction> transactions=new ArrayList<>();
    TransactionAdapter transactionAdapter;
    public Transactions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fetch transactions
        transactionAdapter=new TransactionAdapter(getContext(),transactions);
        recyclerView.setAdapter(transactionAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTransactions();
            }
        });
        fetchTransactions();

    }

    private void fetchTransactions() {
        Res_UserInfo userInfo = Support.AccessUserInfo(getContext());
        if(PaymentsUtil.isNetworkAvailable(getContext())){
            swipeRefreshLayout.setRefreshing(true);
            PaymentsUtil.getInstance().history(userInfo.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            swipeRefreshLayout.setRefreshing(false);
                            if(response.isSuccessful()){
                                try {
                                    String body=response.body().string();
                                    JSONObject jsonObject=new JSONObject(body);
                                    if(jsonObject.getBoolean("status")){
                                        transactions.clear();
                                        transactions.addAll(parseTransactionsFromResponse(jsonObject.getJSONArray("history")));
                                        updateUI();
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Could not parse transactions", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(getContext(), "Could not fetch transactions", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            swipeRefreshLayout.setRefreshing(false);
                            t.printStackTrace();
                            Toast.makeText(getContext(), "Could not fetch transactions", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Snackbar.make(recyclerView,"Could not connect to internet",Snackbar.LENGTH_SHORT).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchTransactions();
                }
            }).show();
        }
    }

    private void updateUI() {
        transactionAdapter.notifyDataSetChanged();
    }

    private List<Transaction> parseTransactionsFromResponse(JSONArray jsonArray) throws JSONException {
        List<Transaction> tr=new ArrayList<>();
        Gson gson=new Gson();
        for(int i=0;i<jsonArray.length();i++){
            String string = jsonArray.getJSONObject(i).toString();
            Transaction transaction = gson.fromJson(string, Transaction.class);
            tr.add(transaction);
        }
        return tr;
    }
}
