package com.moneywise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moneywise.R;
import com.moneywise.model.MonthlyTransactionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionMonthlyRVAdapter extends
        RecyclerView.Adapter<TransactionMonthlyRVAdapter.ViewHolder> {

    Context context;
    List<MonthlyTransactionModel> dataArrayList;

    public TransactionMonthlyRVAdapter(Context context, List<MonthlyTransactionModel> dataList) {
        this.context = context;
        this.dataArrayList = dataList;
    }

    @NonNull
    @Override
    public TransactionMonthlyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.transaction_card_monthly, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionMonthlyRVAdapter.ViewHolder holder, int position) {
        try {
            MonthlyTransactionModel data = dataArrayList.get(position);
            holder.tvIncome.setText(String.format("Rp %.2f", data.getTotalIncome()));
            holder.tvExpense.setText(String.format("Rp %.2f", data.getTotalExpense()));
            holder.tvTotalMonthly.setText(String.format("Rp %.2f", data.getTotalMonthly()));
            Date d = new SimpleDateFormat("yyyy/MM/dd").parse(data.getMonth());
            holder.tvMonth.setText(new SimpleDateFormat("MMM").format(d));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvIncome;
        TextView tvExpense;
        TextView tvTotalMonthly;
        TextView tvMonth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIncome = itemView.findViewById(R.id.txtIncome);
            tvExpense = itemView.findViewById(R.id.txtExpense);
            tvTotalMonthly = itemView.findViewById(R.id.txtTotal);
            tvMonth = itemView.findViewById(R.id.txtMonth);
        }
    }
}
