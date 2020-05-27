package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password_old;
    private EditText password_new1;
    private EditText password_new2;
    //    private SharedPreferences sharedPreferences;
    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //todo 修改密码，下面三个控件为老密码、新密码、确认密码的控件
        password_old = (EditText)findViewById(R.id.editText_changePassword_lastpassword);
        password_new1 = (EditText)findViewById(R.id.editText_changePassword_newpassword1);
        password_new2 = (EditText)findViewById(R.id.editText_changePassword_newpassword2);


        button = (Button)findViewById(R.id.button_changePassword);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //todo 点击修改密码按钮
                finish();//结束activity退回上个界面
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
