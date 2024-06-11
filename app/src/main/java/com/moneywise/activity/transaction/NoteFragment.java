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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moneywise.R;
import com.moneywise.adapter.TransactionNoteRVAdapter;
import com.moneywise.constant.Constant;
import com.moneywise.model.TransactionModel;
import com.moneywise.repository.ITransactionRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.util.Util;

import java.util.List;

public class NoteFragment extends Fragment {
    private ITransactionRepository transactionRepository;

    private List<TransactionModel> data;

    private TransactionNoteRVAdapter adapter;
    private int userId;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView tvTotalNote;

    public NoteFragment() {
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
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.rv_note_transaction);
        tvTotalNote = view.findViewById(R.id.txtTotalAll);
        userId = Util.getCurrentUserId(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        updateDashboard();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            startActivityIntent.launch(intent);
        });
    }

    private void updateDashboard() {
        tvTotalNote.setText(String.format("Rp %.2f", transactionRepository.getTotalNote(userId)));
        data = transactionRepository.getAllNote(userId, -1);
        Log.i("KAMPRET", data.toString());
        adapter = new TransactionNoteRVAdapter(getContext(), data);
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