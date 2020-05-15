package com.example.findmentorapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.findmentorapp.LoginActivity;
import com.example.findmentorapp.MainActivity;
import com.example.findmentorapp.PersonalDataActivity;
import com.example.findmentorapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final TextView toPersonalData = (TextView)root.findViewById(R.id.textView_home_data);
        //toPersonalData.setVisibility(View.INVISIBLE);
        toPersonalData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(intent);
            }
        });

        //跳转登录界面
        Button toLogButton = (Button)root.findViewById(R.id.toLogin);
        toLogButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }
}
