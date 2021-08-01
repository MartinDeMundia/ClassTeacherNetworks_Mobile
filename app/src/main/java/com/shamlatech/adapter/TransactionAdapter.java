package com.shamlatech.adapter;

import android.content.Context;



import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamlatech.pojo.Transaction;
import com.shamlatech.school_management.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Context context;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.transaction_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.title.setText(transaction.getInvoiceNumber());
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH);
        holder.name.setText("Paid by "+transaction.getName());
        holder.date.setText(transaction.getCreated());
        holder.amount.setText(String.format("KES.%s", transaction.getAmount()));
        switch (transaction.getCode()) {
            case "paid":
                holder.amount.setTextColor(context.getResources().getColor(R.color.currency_green));
                break;
            default:
                holder.amount.setTextColor(context.getResources().getColor(R.color.currency_red));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView date;
        TextView amount;
        ImageView typeArrow;
        TextView name;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            title=itemView.findViewById(R.id.title);
            date=itemView.findViewById(R.id.date);
            amount=itemView.findViewById(R.id.amount);
            name=itemView.findViewById(R.id.name);
        }
    }
}
