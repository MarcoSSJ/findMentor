package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText username_text;
    private EditText password_text1;
    private EditText password_text2;
    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //用户名与密码、确认密码
        username_text = (EditText)findViewById(R.id.editText_register_username);
        password_text1 = (EditText)findViewById(R.id.editText_register_password1);
        password_text2 = (EditText)findViewById(R.id.editText_register_password2);

        //注册按钮
        button = (Button)findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {



                //以下为注册成功跳转代码
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                //intent.putExtra("fragid",1); //添加Extra
                startActivity(intent);
                finish();

                //弹窗
                Toast toast=Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT);
                toast.show();
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
