package com.example.findmentorapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.findmentorapp.AccountActivity;
import com.example.findmentorapp.FavoriteActivity;
import com.example.findmentorapp.LoginActivity;
import com.example.findmentorapp.MyApplication;
import com.example.findmentorapp.PersonalDataSActivity;
import com.example.findmentorapp.PersonalDataTActivity;
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

        //获取登陆状态
        MyApplication application = (MyApplication) getActivity().getApplication();
        String sessionID = application.getSessionID();

        //跳转登录界面
        Button toLogButton = (Button) root.findViewById(R.id.toLogin);
        LinearLayout layout_toAccount = (LinearLayout)root.findViewById(R.id.layout_home_account);
        LinearLayout layout_toFavorite = (LinearLayout)root.findViewById(R.id.layout_home_favorite);
        final TextView toPersonalData = (TextView)root.findViewById(R.id.textView_home_data);

        if(sessionID.equals("")) {
            toLogButton.setVisibility(View.VISIBLE);
            layout_toAccount.setVisibility(View.INVISIBLE);
            layout_toFavorite.setVisibility(View.INVISIBLE);
            toPersonalData.setVisibility(View.INVISIBLE);
        }
        else {
            layout_toAccount.setVisibility(View.VISIBLE);
            layout_toFavorite.setVisibility(View.VISIBLE);
            toLogButton.setVisibility(View.INVISIBLE);
            toPersonalData.setVisibility(View.VISIBLE);
        }

        //todo 跳转个人信息界面，处理下学生与老师不同情况
        toPersonalData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                //老师跳转
                Intent intent = new Intent(getActivity(), PersonalDataTActivity.class);
                startActivity(intent);

                //学生跳转
                //Intent intent = new Intent(getActivity(), PersonalDataSActivity.class);
                //startActivity(intent);

            }
        });

        toLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        layout_toAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        //跳转关注列表
        layout_toFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
            }
        });
        //layout_toAccount.setVisibility(View.INVISIBLE);



        return root;
    }
}
