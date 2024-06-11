package com.moneywise.activity.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moneywise.R;
import com.moneywise.constant.Constant;
import com.moneywise.model.BankModel;
import com.moneywise.repository.BankRepository;
import com.moneywise.repository.IBankRepository;
import com.moneywise.repository.ITransactionRepository;
import com.moneywise.repository.TransactionRepository;
import com.moneywise.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {
    IBankRepository bankRepository = new BankRepository(this, Constant.DB_NAME, null, Constant.VERSION);
    ITransactionRepository transactionRepository = new TransactionRepository(this, Constant.DB_NAME, null, Constant.VERSION);

    private final List<String> bankNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.dropdown_bank);
        EditText etAmount = findViewById(R.id.amountInput);
        EditText etDescription = findViewById(R.id.descriptionInput);
        RadioGroup radioLabel = findViewById(R.id.radioGroup);
        ImageView backBtn = findViewById(R.id.closeButton);
        Button submitBtn = findViewById(R.id.addTransactionButton);

        bankNames.add("Add new bank");
        for (BankModel bank : bankRepository.getAll(-1)) {
            bankNames.add(bank.getName());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, bankNames);
        autoCompleteTextView.setAdapter(arrayAdapter);


        submitBtn.setOnClickListener(v -> {
            BankModel bankModel = bankRepository.getByName(autoCompleteTextView.getText().toString());
            double amount = Double.parseDouble(etAmount.getText().toString());
            String description = etDescription.getText().toString();
            String label = Constant.LABEL_EXPENSE;

            if (radioLabel.getCheckedRadioButtonId() == findViewById(R.id.radioButtonExpense).getId()) {
                label = Constant.LABEL_EXPENSE;
            } else if (radioLabel.getCheckedRadioButtonId() == findViewById(R.id.radioButtonIncome).getId()) {
                label = Constant.LABEL_INCOME;
            } else if (radioLabel.getCheckedRadioButtonId() == findViewById(R.id.radioButtonNote).getId()) {
                label = Constant.LABEL_NOTE;
            }

            boolean success = transactionRepository.create(
                    Util.getCurrentUserId(this),
                    label,
                    amount,
                    description,
                    bankModel.getId()
            );

            if (success) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                Intent intent = new Intent(parent.getContext(), AddBankActivity.class);

                finish();
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });

    }
}