package com.moneywise.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moneywise.R;
import com.moneywise.constant.Constant;
import com.moneywise.repository.ITransactionRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.util.Util;

public class StatFragment extends Fragment {
    private ITransactionRepository transactionRepository;

    private TextView tvTotalIncomeBank;
    private TextView tvTotalExpenseBank;
    private TextView tvTotalNoteBank;
    private int userId;

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
        return inflater.inflate(R.layout.fragment_stat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTotalIncomeBank = view.findViewById(R.id.tvTotalIncomeBank);
        tvTotalExpenseBank = view.findViewById(R.id.tvTotalExpenseBank);
        tvTotalNoteBank = view.findViewById(R.id.tvTotalNoteBank);

        userId = Util.getCurrentUserId(getActivity());

        tvTotalIncomeBank.setText(String.format("Rp %.2f", transactionRepository.getIncome(userId)));
        tvTotalExpenseBank.setText(String.format("Rp %.2f", transactionRepository.getExpense(userId)));
        tvTotalNoteBank.setText(String.format("Rp %.2f", transactionRepository.getTotalNote(userId)));
    }
}