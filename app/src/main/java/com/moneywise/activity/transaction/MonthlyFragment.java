package com.moneywise.activity.transaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moneywise.R;
import com.moneywise.adapter.TransactionMonthlyRVAdapter;

import java.util.ArrayList;

public class MonthlyFragment extends Fragment {

    private final ArrayList<String> data = new ArrayList<>();
    private RecyclerView recyclerView;

    public MonthlyFragment() {
        // Required empty public constructor
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
        dataInitialize();
        recyclerView = view.findViewById(R.id.rv_monthly_transaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        TransactionMonthlyRVAdapter adapter = new TransactionMonthlyRVAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void dataInitialize() {
        // This is dummy data!
        data.add("Rp. 1000.00");
        data.add("Rp. 2000.00");
        data.add("Rp. 3000.00");
        data.add("Rp. 4000.00");
        data.add("Rp. 5000.00");
        data.add("Rp. 1000.00");
        data.add("Rp. 2000.00");
        data.add("Rp. 3000.00");
        data.add("Rp. 4000.00");
        data.add("Rp. 5000.00");
        data.add("Rp. 1000.00");
        data.add("Rp. 2000.00");
        data.add("Rp. 3000.00");
        data.add("Rp. 4000.00");
        data.add("Rp. 5000.00");
        data.add("Rp. 1000.00");
        data.add("Rp. 2000.00");
        data.add("Rp. 3000.00");
        data.add("Rp. 4000.00");
        data.add("Rp. 5000.00");
    }
}