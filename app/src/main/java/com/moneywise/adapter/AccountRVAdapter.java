package com.moneywise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moneywise.R;
import com.moneywise.model.BankBalanceModel;

import java.util.List;

public class AccountRVAdapter extends
        RecyclerView.Adapter<AccountRVAdapter.ViewHolder> {

    Context context;
    List<BankBalanceModel> dataArrayList;

    public AccountRVAdapter(Context context, List<BankBalanceModel> dataList) {
        this.context = context;
        this.dataArrayList = dataList;
    }

    @NonNull
    @Override
    public AccountRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.account_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountRVAdapter.ViewHolder holder, int position) {
        BankBalanceModel data = dataArrayList.get(position);
        holder.tvBankName.setText(data.getBank().getName());
        holder.tvBalance.setText(String.format("Rp %.2f", data.getBalance()));
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBalance;
        TextView tvBankName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBalance = itemView.findViewById(R.id.totalBalance);
            tvBankName = itemView.findViewById(R.id.txtBankName);
        }
    }
}
