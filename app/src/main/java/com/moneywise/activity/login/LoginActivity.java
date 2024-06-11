package com.moneywise.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.moneywise.activity.MainActivity;
import com.moneywise.constant.Constant;
import com.moneywise.model.UserModel;
import com.moneywise.repository.IUserRepository;
import com.moneywise.repository.UserRepository;

public class LoginActivity extends AppCompatActivity {

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private TextView txtSignUp;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

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

        txtSignUp = findViewById(R.id.textSignUp);
        etEmail = findViewById(R.id.emailTextInputEditText);
        etPassword = findViewById(R.id.passwordTextInputEditText);
        btnLogin = findViewById(R.id.btnLogin);

        txtSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            submitData();
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String email = etEmail.getText().toString().trim();
                if (email.matches(emailPattern) && editable.length() > 0) {
                    btnLogin.setEnabled(true);
                } else {
                    etEmail.setError("Invalid Email");
                    btnLogin.setEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });
    }

    private void submitData() {
        try {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!(email.isEmpty() && password.isEmpty())) {
                UserModel user = userRepository.getByEmailPassword(
                        email,
                        password
                );

                if (user != null) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                    // save info
                    SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(Constant.ID_USER_KEY, user.getId());
                    editor.apply();

                    // start activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (email.isEmpty()) {
                    etEmail.setError("Must not empty!");
                } if (password.isEmpty()) {
                    etPassword.setError("Must not empty!");
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
        }
    }
}