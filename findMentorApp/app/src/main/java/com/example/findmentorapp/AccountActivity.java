package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button deleteButton;
    private Button fixPasswordButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        logoutButton = (Button)findViewById(R.id.button_account_logout);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //Todo 登出操作


                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteButton = (Button)findViewById(R.id.button_account_delete);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //Todo 注销账户操作


                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fixPasswordButton = (Button)findViewById(R.id.button_account_fix);
        fixPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //Todo 修改密码

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
