package com.moneywise.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.moneywise.repository.IUserRepository;
import com.moneywise.repository.UserRepository;

public class SignUpActivity extends AppCompatActivity {

    private final IUserRepository userRepository = new UserRepository(
            SignUpActivity.this,
            Constant.DB_NAME,
            null,
            Constant.VERSION
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMainPage();
    }

    private void loadMainPage() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imgBack = findViewById(R.id.imgBack);
        EditText etEmail = findViewById(R.id.emailRegTextInput);
        EditText etPassword1 = findViewById(R.id.passwordReg1TextInputEditText);
        EditText etPassword2 = findViewById(R.id.passwordReg2TextInputEditText);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        btnSignUp.setEnabled(false);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String email = etEmail.getText().toString().trim();
                if (email.matches(emailPattern) && editable.length() > 0) {
                    btnSignUp.setEnabled(true);
                } else {
                    etEmail.setError("Invalid Email");
                    btnSignUp.setEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        imgBack.setOnClickListener(view -> finish());

        btnSignUp.setOnClickListener(view -> {
            try {
                String email = etEmail.getText().toString();
                String password1 = etPassword1.getText().toString();
                String password2 = etPassword2.getText().toString();

                boolean success = false;

                if (password1.equals(password2)) {
                    success = userRepository.create(email, password1);
                } else {
                    etPassword2.setError("Password are not the same!");
                }

                if (success) {
                    Toast.makeText(this, "Success create account!", Toast.LENGTH_SHORT).show();
                } else {
                    if (userRepository.isExistByEmail(email)) {
                        Toast.makeText(this, "Account with this email already exist!", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(this, "Fail to create account!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}