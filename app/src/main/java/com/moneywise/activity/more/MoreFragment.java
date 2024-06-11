package com.moneywise.activity.more;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.moneywise.R;
import com.moneywise.activity.login.LoginActivity;
import com.moneywise.constant.Constant;

public class MoreFragment extends Fragment {

    private Button btnLogout;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.logoutBtn);

        btnLogout.setOnClickListener(v -> {
            // delete login info
            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(Constant.ID_USER_KEY);
            editor.apply();
            Toast.makeText(getActivity(), "Logout success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().finish();
            startActivity(intent);
        });
    }
}