package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalDataTActivity extends AppCompatActivity {


    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_age;
    private TextView textView_intro;
    private TextView textView_range;
    private TextView textView_school;
    private TextView textView_department;
    private Button button;
    private SharedPreferences sharedPreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_teacher);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //todo 补充教师的学校、院系、研究方向，并把签名换做简介
        //姓名、年龄、性别、简介、学校、院系、研究方向
        textView_name = (TextView)findViewById(R.id.textView_personalData_name);
        textView_age = (TextView)findViewById(R.id.textView_personalData_age);
        textView_sex = (TextView)findViewById(R.id.textView_personalData_sex);
        textView_intro = (TextView)findViewById(R.id.textView_personalData_intro);
        textView_school = (TextView)findViewById(R.id.textView_personalData_school);
        textView_department = (TextView)findViewById(R.id.textView_personalData_department);
        textView_range = (TextView)findViewById(R.id.textView_personalData_range);

        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String age = sharedPreferences.getString("age","");
        String sex = sharedPreferences.getString("sex","");
        String signature = sharedPreferences.getString("signature","");

        MyApplication application = (MyApplication) getApplicationContext();
        String sessionID = application.getSessionID();

        if(sessionID.equals(""))
        {
            textView_name.setText("请登录");
            textView_age.setText("请登录");
            textView_sex.setText("请登录");
            textView_intro.setText("请登录");
        }
        //todo:在此处设置显示
        else {
            if (name.equals(""))
                textView_name.setText("李四");
            else
                textView_name.setText(name);

            if (age.equals(""))
                textView_age.setText("0");
            else
                textView_age.setText(age);

            if (sex.equals(""))
                textView_sex.setText("男");
            else
                textView_sex.setText(sex);

            if (signature.equals(""))
                textView_intro.setText("请输入简介");
            else
                textView_intro.setText(signature);
        }
        //todo:修改按钮处理
        button = (Button)findViewById(R.id.button_personalData);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(PersonalDataTActivity.this, PersonalDataChangeTActivity.class);
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
