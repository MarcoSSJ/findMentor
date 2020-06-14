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
    private TextView textView_grade;
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

        //姓名、年龄、性别、简介、学校、院系、研究方向
        textView_name = (TextView)findViewById(R.id.textView_personalData_name);
        textView_age = (TextView)findViewById(R.id.textView_personalData_age);
        textView_sex = (TextView)findViewById(R.id.textView_personalData_sex);
        textView_intro = (TextView)findViewById(R.id.textView_personalData_intro);
        textView_school = (TextView)findViewById(R.id.textView_personalData_school);
        textView_department = (TextView)findViewById(R.id.textView_personalData_department);
        textView_range = (TextView)findViewById(R.id.textView_personalData_range);
        textView_grade = (TextView)findViewById(R.id.textView_personalData_grade);

        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String age = sharedPreferences.getString("age","");
        String sex = sharedPreferences.getString("sex","");
        String intro = sharedPreferences.getString("intro","");
        String school = sharedPreferences.getString("school","");
        String department = sharedPreferences.getString("department","");
        String range = sharedPreferences.getString("range","");
        String grade = sharedPreferences.getString("grade","");

        MyApplication application = (MyApplication) getApplicationContext();
        String sessionID = application.getSessionID();

        if(sessionID.equals(""))
        {
            textView_name.setText("请登录");
            textView_age.setText("请登录");
            textView_sex.setText("请登录");
            textView_intro.setText("请登录");
            textView_school.setText("请登录");
            textView_department.setText("请登录");
            textView_range.setText("请登录");
            textView_grade.setText("请登录");
        }
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

            if (intro.equals(""))
                textView_intro.setText("未设置");
            else
                textView_intro.setText(intro);

            if (school.equals(""))
                textView_school.setText("未设置");
            else
                textView_school.setText(school);

            if (department.equals(""))
                textView_department.setText("未设置");
            else
                textView_department.setText(department);

            if (range.equals(""))
                textView_range.setText("未设置");
            else
                textView_range.setText(range);

            if (grade.equals(""))
                textView_grade.setText("未设置");
            else
                textView_grade.setText(grade);
        }
        //开启修改界面
        button = (Button)findViewById(R.id.button_personalData);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(PersonalDataTActivity.this, PersonalDataChangeTActivity.class);
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
