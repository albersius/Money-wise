package com.moneywise.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moneywise.R;
import com.moneywise.constant.Constant;
import com.moneywise.model.UserModel;
import com.moneywise.repository.IUserRepository;
import com.moneywise.repository.UserRepository;

public class LoginActivity extends AppCompatActivity {

    private final IUserRepository userRepository = new UserRepository(
            LoginActivity.this,
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
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtSignUp = findViewById(R.id.textSignUp);
        EditText etEmail = findViewById(R.id.emailTextInputEditText);
        EditText etPassword = findViewById(R.id.passwordTextInputEditText);
        Button btnLogin = findViewById(R.id.btnLogin);

        txtSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            try {
                UserModel user = userRepository.getByEmailPassword(
                        etEmail.getText().toString(),
                        etPassword.getText().toString()
                );

                if (user != null) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}