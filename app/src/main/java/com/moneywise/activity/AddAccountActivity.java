package com.moneywise.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moneywise.R;
import com.moneywise.activity.transaction.AddBankActivity;
import com.moneywise.constant.Constant;
import com.moneywise.model.BankBalanceModel;
import com.moneywise.model.BankModel;
import com.moneywise.repository.BankRepository;
import com.moneywise.repository.IBankRepository;
import com.moneywise.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AddAccountActivity extends AppCompatActivity {
    IBankRepository bankRepository = new BankRepository(this, Constant.DB_NAME, null, Constant.VERSION);

    private ArrayAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private List<String> bankNames;
    private ImageView backBtn;
    private TextView tvInitBal;
    private Button submitBtn;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backBtn = findViewById(R.id.closeButton);
        autoCompleteTextView = findViewById(R.id.dropdown_bank);
        submitBtn = findViewById(R.id.addAccountButton);
        tvInitBal = findViewById(R.id.initBalanceInput);

        userId = Util.getCurrentUserId(this);

        updateData();

        backBtn.setOnClickListener(v -> {
            finish();
        });

        submitBtn.setOnClickListener(v -> {
            submitData();
        });

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                Intent intent = new Intent(parent.getContext(), AddBankActivity.class);
                autoCompleteTextView.setText("");
                startActivityIntent.launch(intent);
            } else {
                String selectedBankName = autoCompleteTextView.getText().toString();
                BankModel bankModel = bankRepository.getByName(selectedBankName);
                BankBalanceModel balanceModel = bankRepository.getBankBalanceByBankId(userId,bankModel.getId());
                if (balanceModel != null) {
                    Toast.makeText(this, "You already have account with this bank!", Toast.LENGTH_SHORT).show();
                    autoCompleteTextView.setText("");
                }
            }
        });
    }

    private void submitData() {
        String bankName = autoCompleteTextView.getText().toString();
        String initialBalance = tvInitBal.getText().toString();
        if (!(bankName.isEmpty() && initialBalance.isEmpty())) {
            int bankId = bankRepository.getByName(bankName).getId();
            boolean success = bankRepository.createAccount(userId, bankId, Integer.parseInt(tvInitBal.getText().toString()));
            if (success) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            }
        } else {
            if(bankName.isEmpty()) {
                autoCompleteTextView.setError("Must not empty!");
            }
            if(initialBalance.isEmpty()) {
                tvInitBal.setError("Must not empty!");
            }
            Toast.makeText(this, "Please fill all form!", Toast.LENGTH_SHORT).show();

        }
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> updateData()
    );

    private void updateData() {
        bankNames = new ArrayList<>();
        bankNames.add("Add new bank");
        for (BankModel bank : bankRepository.getAll(-1)) {
            bankNames.add(bank.getName());
        }
        adapter = new ArrayAdapter(this, R.layout.dropdown_item, bankNames);
        autoCompleteTextView.setAdapter(adapter);
    }
}