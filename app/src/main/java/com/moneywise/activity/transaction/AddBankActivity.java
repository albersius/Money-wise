package com.moneywise.activity.transaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moneywise.R;
import com.moneywise.constant.Constant;
import com.moneywise.repository.BankRepository;
import com.moneywise.repository.IBankRepository;

public class AddBankActivity extends AppCompatActivity {
    IBankRepository bankRepository = new BankRepository(this, Constant.DB_NAME, null, Constant.VERSION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bank);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backBtn = findViewById(R.id.closeButton);
        Button addBankBtn = findViewById(R.id.addBankButton);
        EditText etBankName = findViewById(R.id.bankNameInput);

        addBankBtn.setOnClickListener(v -> {
            String bankName = etBankName.getText().toString();
            if (bankName.isEmpty()) {
                etBankName.setError("Bank name still empty!");
            } else {
                boolean success = bankRepository.create(bankName);
                if (success) {
                    Toast.makeText(this, "Bank " + bankName + " created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}