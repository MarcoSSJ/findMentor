package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalDataActivity extends AppCompatActivity {


    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_age;
    private TextView textView_signature;
    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //姓名、年龄、性别、签名
        textView_name = (TextView)findViewById(R.id.textView_personalData_name);
        textView_age = (TextView)findViewById(R.id.textView_personalData_age);
        textView_sex = (TextView)findViewById(R.id.textView_personalData_sex);
        textView_signature = (TextView)findViewById(R.id.textView_personalData_signature);

        //todo:在此处设置显示
        textView_name.setText("李四");

        //todo:修改按钮处理
        button = (Button)findViewById(R.id.button_personalData);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(PersonalDataActivity.this, PersonalDataChangeActivity.class);
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
