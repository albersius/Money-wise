package com.moneywise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.moneywise.R;
import com.moneywise.constant.Constant;
import com.moneywise.model.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionDailyRVAdapter extends
        RecyclerView.Adapter<TransactionDailyRVAdapter.ViewHolder> {

    Context context;
    List<TransactionModel> dataArrayList;

    public TransactionDailyRVAdapter(Context context, List<TransactionModel> dataList) {
        this.context = context;
        this.dataArrayList = dataList;
    }

    @NonNull
    @Override
    public TransactionDailyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.transaction_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDailyRVAdapter.ViewHolder holder, int position) {
        TransactionModel data = dataArrayList.get(position);

        holder.tvTotal.setText(String.format("Rp %.2f", data.getAmount()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        holder.tvMonth.setText(simpleDateFormat.format(data.getCreatedAt()));
        simpleDateFormat = new SimpleDateFormat("dd- yyyy");
        holder.tvDateRange.setText(simpleDateFormat.format(data.getCreatedAt()));
        holder.tvBankName.setText(data.getBank().getName());

        if(data.getLabel().equals(Constant.LABEL_INCOME)) {
            holder.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.text_blue));
            holder.tvLabel.setText("Income");
            holder.tvIncome.setText(String.format("Rp %.2f", data.getAmount()));
        } else if (data.getLabel().equals(Constant.LABEL_EXPENSE)){
            holder.tvLabel.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tvLabel.setText("Expense");
            holder.tvExpense.setText(String.format("Rp %.2f", data.getAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTotal;
        TextView tvMonth;
        TextView tvLabel;
        TextView tvIncome;
        TextView tvExpense;
        TextView tvDateRange;
        TextView tvBankName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotal = itemView.findViewById(R.id.txtTotal);
            tvMonth = itemView.findViewById(R.id.txtMonth);
            tvLabel = itemView.findViewById(R.id.txtLabel);
            tvIncome = itemView.findViewById(R.id.txtIncome);
            tvExpense = itemView.findViewById(R.id.txtExpense);
            tvDateRange = itemView.findViewById(R.id.txtDateRange);
            tvBankName = itemView.findViewById(R.id.txtBankName);
        }
    }
}
