package com.moneywise.activity.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moneywise.R;
import com.moneywise.activity.AddAccountActivity;
import com.moneywise.constant.Constant;
import com.moneywise.model.BankBalanceModel;
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

    private List<String> bankNames;

    private AutoCompleteTextView autoCompleteTextView;
    private EditText etAmount;
    private EditText etDescription;
    private RadioGroup radioLabel;
    private ImageView backBtn;
    private Button submitBtn;
    private int userId;
    private String selectedLabel = Constant.LABEL_INCOME;
    private ArrayAdapter adapter;

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

        userId = Util.getCurrentUserId(this);

        autoCompleteTextView = findViewById(R.id.dropdown_bank);
        etAmount = findViewById(R.id.amountInput);
        etDescription = findViewById(R.id.descriptionInput);
        radioLabel = findViewById(R.id.radioGroup);
        backBtn = findViewById(R.id.closeButton);
        submitBtn = findViewById(R.id.addTransactionButton);

        updateAccountData();



        submitBtn.setOnClickListener(v -> {
            submitData();
        });

        radioLabel.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == findViewById(R.id.radioButtonIncome).getId()) {
                selectedLabel = Constant.LABEL_INCOME;
            } else if (checkedId == findViewById(R.id.radioButtonExpense).getId()) {
                selectedLabel = Constant.LABEL_EXPENSE;
            } else if (checkedId == findViewById(R.id.radioButtonNote).getId()) {
                selectedLabel = Constant.LABEL_NOTE;
            }
        });

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                autoCompleteTextView.setText("");
                Intent intent = new Intent(parent.getContext(), AddAccountActivity.class);
                startActivityIntent.launch(intent);
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> updateAccountData()
    );

    private void updateAccountData() {
        bankNames = new ArrayList<>();
        bankNames.add("Add new account");
        for (BankBalanceModel bank : bankRepository.getAllBankBalance(userId)) {
            bankNames.add(bankRepository.getById(bank.getId()).getName());
        }
        adapter = new ArrayAdapter(this, R.layout.dropdown_item, bankNames);
        autoCompleteTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void submitData() {
        String description = etDescription.getText().toString();
        String amountText = etAmount.getText().toString();
        double amount;
        if (amountText.isEmpty()) {
            amount = 0;
        } else {
            amount = Double.parseDouble(amountText);
        }

        String account = autoCompleteTextView.getText().toString();

        if(!(description.isEmpty() && amount == 0 && account.isEmpty())) {
            BankModel bankModel = bankRepository.getByName(account);
            BankBalanceModel bankBalanceModel = bankRepository.getBankBalanceByBankId(userId, bankModel.getId());

            if(bankBalanceModel != null && bankModel != null) {
                if (radioLabel.getCheckedRadioButtonId() == findViewById(R.id.radioButtonExpense).getId()) {
                    selectedLabel = Constant.LABEL_EXPENSE;
                    if (bankBalanceModel.getBalance() < amount) {
                        Toast.makeText(this, "Not enough balance!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        bankBalanceModel.subtractBal(amount);
                        bankRepository.updateBalance(bankBalanceModel);
                    }
                } else if (radioLabel.getCheckedRadioButtonId() == findViewById(R.id.radioButtonIncome).getId()) {
                    selectedLabel = Constant.LABEL_INCOME;
                    bankBalanceModel.addBal(amount);
                    bankRepository.updateBalance(bankBalanceModel);
                } else if (radioLabel.getCheckedRadioButtonId() == findViewById(R.id.radioButtonNote).getId()) {
                    selectedLabel = Constant.LABEL_NOTE;
                }

                boolean success = transactionRepository.create(
                        Util.getCurrentUserId(this),
                        selectedLabel,
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
            }
        } else {
            if (amountText.isEmpty()) {
                etAmount.setError("Must not empty!");
            }
            if (description.isEmpty()) {
                etDescription.setError("Must not empty!");
            }
            if (account.isEmpty()) {
                autoCompleteTextView.setError("Must not empty!");
            }
            Toast.makeText(this, "Please fill all form!", Toast.LENGTH_SHORT).show();
        }
    }
}