package com.moneywise.activity.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.moneywise.R;
import com.moneywise.activity.account.AddAccountActivity;
import com.moneywise.adapter.AccountRVAdapter;
import com.moneywise.constant.Constant;
import com.moneywise.model.BankBalanceModel;
import com.moneywise.repository.BankRepository;
import com.moneywise.repository.IBankRepository;
import com.moneywise.repository.ITransactionRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.util.Util;

import java.util.List;

public class AccountFragment extends Fragment {
    private IBankRepository bankRepository;
    private ITransactionRepository transactionRepository;

    private ImageView btnAddAccount;
    private RecyclerView recyclerView;
    private AccountRVAdapter adapter;
    private TextView tvTotalLiabilities;
    private TextView tvTotalAssets;
    private int userId;
    private List<BankBalanceModel> datas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bankRepository = new BankRepository(context, Constant.DB_NAME, null, Constant.VERSION);
        transactionRepository = new TransactionRepository(context, Constant.DB_NAME, null, Constant.VERSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddAccount = view.findViewById(R.id.addAccountBtn);
        recyclerView = view.findViewById(R.id.rv_account);
        tvTotalLiabilities = view.findViewById(R.id.txtTotalLiabilities);
        tvTotalAssets = view.findViewById(R.id.txtTotalAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        userId = Util.getCurrentUserId(getActivity());

        updateDashboard();

        tvTotalAssets.setText(String.format("Rp. %.2f", bankRepository.getTotalBalance(userId)));
        tvTotalLiabilities.setText(String.format("Rp. %.2f", transactionRepository.getTotalNote(userId)));

        btnAddAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddAccountActivity.class);
            startActivityIntent.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> updateDashboard()
    );

    private void updateDashboard() {
        datas = bankRepository.getAllBankBalance(userId);
        adapter = new AccountRVAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);
    }

}