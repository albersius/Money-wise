package com.moneywise.activity.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moneywise.R;
import com.moneywise.adapter.TransactionMonthlyRVAdapter;
import com.moneywise.constant.Constant;
import com.moneywise.model.MonthlyTransactionModel;
import com.moneywise.model.TransactionModel;
import com.moneywise.repository.ITransactionRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MonthlyFragment extends Fragment {

    private ITransactionRepository transactionRepository;

    private List<MonthlyTransactionModel> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView tvTotalIncome;
    private TextView tvAllTotal;
    private TextView tvTotalExpense;
    private TransactionMonthlyRVAdapter adapter;
    private int userId;

    public MonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        transactionRepository = new TransactionRepository(context, Constant.DB_NAME, null, Constant.VERSION);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.rv_monthly_transaction);
        tvTotalIncome = view.findViewById(R.id.txtTotalIncome);
        tvTotalExpense = view.findViewById(R.id.txtTotalExpense);
        tvAllTotal = view.findViewById(R.id.txtTotalAll);
        recyclerView = view.findViewById(R.id.rv_monthly_transaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        userId = Util.getCurrentUserId(getContext());

        updateDasboard();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            startActivity(intent);
        });
    }

    private void updateDasboard() {
        tvTotalIncome.setText(String.format("Rp %.2f", transactionRepository.getIncome(userId)));
        tvTotalExpense.setText(String.format("Rp %.2f", transactionRepository.getExpense(userId)));
        tvAllTotal.setText(String.format("Rp %.2f", transactionRepository.getBalance(userId)));
        data = transactionRepository.getMonthlyByYear(userId, 2024);
        adapter = new TransactionMonthlyRVAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}