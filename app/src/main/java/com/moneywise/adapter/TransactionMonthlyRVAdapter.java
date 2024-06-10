package com.moneywise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moneywise.R;

import java.util.ArrayList;

public class TransactionMonthlyRVAdapter extends
        RecyclerView.Adapter<TransactionMonthlyRVAdapter.ViewHolder> {

    Context context;
    ArrayList<String> dataArrayList;

    public TransactionMonthlyRVAdapter(Context context, ArrayList<String> dataList) {
        this.context = context;
        this.dataArrayList = dataList;
    }

    @NonNull
    @Override
    public TransactionMonthlyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.transaction_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionMonthlyRVAdapter.ViewHolder holder, int position) {
        String text = dataArrayList.get(position);
        holder.total.setText(text);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.txtTotal);
        }
    }
}
