package com.moneywise.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.moneywise.R;
import com.moneywise.activity.transaction.CalendarFragment;
import com.moneywise.activity.transaction.DailyFragment;
import com.moneywise.activity.transaction.MonthlyFragment;
import com.moneywise.activity.transaction.NoteFragment;
import com.moneywise.activity.transaction.TotalFragment;
import com.moneywise.adapter.TransactionVPAdapter;
import com.moneywise.databinding.FragmentTransactionBinding;

public class TransactionFragment extends Fragment {
    private FragmentTransactionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        TransactionVPAdapter adapter = new TransactionVPAdapter(getParentFragmentManager(), getLifecycle());
        adapter.addFragment(new DailyFragment(), "Daily");
        adapter.addFragment(new CalendarFragment(), "Calendar");
        adapter.addFragment(new MonthlyFragment(), "Monthly");
        adapter.addFragment(new TotalFragment(), "Total");
        adapter.addFragment(new NoteFragment(), "Note");
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, i) -> {
            tab.setText(adapter.getTitle(i));
        }).attach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}