package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;

public class LoginActivity extends AppCompatActivity {

    String username;
    String password;
    private Button loginButton;
    private Button registerButton;
    private EditText username_text;
    private EditText password_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //用户名与密码输入
        username_text = (EditText)findViewById(R.id.editText_log_username);
        password_text = (EditText)findViewById(R.id.editText_log_password);

        //处理点击登录按钮
        loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {



            //以下为登陆成功跳转代码
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            //intent.putExtra("fragid",1); //添加Extra
            startActivity(intent);
            finish();

            //弹窗
            Toast toast=Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT);
            toast.show();
            }
        });


        //处理点击注册按钮
        registerButton = (Button)findViewById(R.id.button_registered);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                //intent.putExtra("fragid",1); //添加Extra
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}