package com.moneywise.activity.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.moneywise.adapter.TransactionDailyRVAdapter;
import com.moneywise.constant.Constant;
import com.moneywise.model.TransactionModel;
import com.moneywise.repository.ITransactionRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DailyFragment extends Fragment {

    private ITransactionRepository transactionRepository;

    private List<TransactionModel> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView tvTotalIncome;
    private TextView tvTotalExpense;
    private TextView tvAllTotal;
    private int userId;

    private TransactionDailyRVAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        transactionRepository = new TransactionRepository(context, Constant.DB_NAME, null, Constant.VERSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.rv_monthly_transaction);
        tvTotalIncome = view.findViewById(R.id.txtTotalIncome);
        tvTotalExpense = view.findViewById(R.id.txtTotalExpense);
        tvAllTotal = view.findViewById(R.id.txtTotalAll);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        userId = Util.getCurrentUserId(getContext());

        updateDashboard();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            startActivityIntent.launch(intent);
        });
    }

    private void updateDashboard() {
        tvTotalIncome.setText(String.format("Rp %.2f", transactionRepository.getIncome(userId)));
        tvTotalExpense.setText(String.format("Rp %.2f", transactionRepository.getExpense(userId)));
        tvAllTotal.setText(String.format("Rp %.2f", transactionRepository.getBalance(userId)));
        data = transactionRepository.getAll(userId, -1);
        adapter = new TransactionDailyRVAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    updateDashboard();
                }
            }
    );
}