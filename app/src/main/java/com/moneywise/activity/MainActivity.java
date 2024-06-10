package com.moneywise.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.moneywise.R;
import com.moneywise.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMainPage();
    }

    private void loadMainPage() {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        TransactionFragment transactionFragment = new TransactionFragment();
        StatFragment statFragment = new StatFragment();
        AccountFragment accountFragment = new AccountFragment();
        MoreFragment moreFragment = new MoreFragment();

        replaceFragment(transactionFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.transaction) {
                replaceFragment(transactionFragment);
            } else if (menuItem.getItemId() == R.id.stats) {
                replaceFragment(statFragment);
            } else if (menuItem.getItemId() == R.id.accounts) {
                replaceFragment(accountFragment);
            } else if (menuItem.getItemId() == R.id.more) {
                replaceFragment(moreFragment);
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}