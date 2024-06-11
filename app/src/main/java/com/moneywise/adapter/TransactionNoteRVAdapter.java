package com.moneywise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moneywise.R;
import com.moneywise.model.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionNoteRVAdapter extends
        RecyclerView.Adapter<TransactionNoteRVAdapter.ViewHolder> {

    Context context;
    List<TransactionModel> dataArrayList;

    public TransactionNoteRVAdapter(Context context, List<TransactionModel> dataList) {
        this.context = context;
        this.dataArrayList = dataList;
    }

    @NonNull
    @Override
    public TransactionNoteRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.transaction_card_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionNoteRVAdapter.ViewHolder holder, int position) {
        TransactionModel data = dataArrayList.get(position);

        holder.tvTotal.setText(String.format("Rp %.2f", data.getAmount()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        holder.tvMonth.setText(simpleDateFormat.format(data.getCreatedAt()));
        simpleDateFormat = new SimpleDateFormat("dd- yyyy");
        holder.tvDateRange.setText(simpleDateFormat.format(data.getCreatedAt()));
        holder.tvDescription.setText(data.getDescription());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTotal;
        TextView tvMonth;
        TextView tvDateRange;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotal = itemView.findViewById(R.id.txtTotal);
            tvMonth = itemView.findViewById(R.id.txtMonth);
            tvDateRange = itemView.findViewById(R.id.txtDateRange);
            tvDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
