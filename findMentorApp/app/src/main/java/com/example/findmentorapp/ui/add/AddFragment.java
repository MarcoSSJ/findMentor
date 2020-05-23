package com.example.findmentorapp.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.findmentorapp.LoginActivity;
import com.example.findmentorapp.R;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel =
                ViewModelProviders.of(this).get(AddViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        final TextView textView = root.findViewById(R.id.text_add);
        addViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //标题与信息输入
        final EditText editText_title = root.findViewById(R.id.editText_add_title);
        final EditText editText_info = root.findViewById(R.id.editText_add_info);

        Button button_submit = (Button)root.findViewById(R.id.button_add_submit);
        button_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
            //todo 点击提交按钮后处理，输入框在上面，注意多行输入

            }
        });


        return root;
    }
}
