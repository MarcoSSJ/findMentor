package com.example.findmentorapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OthersDataActivity extends AppCompatActivity {


    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_age;
    private TextView textView_intro;
    private TextView textView_range;
    private TextView textView_school;
    private TextView textView_department;
    private TextView textView_grade;
    private Button button_publish;
    private Button button_favorite;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_data);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //todo 对老师、学生设置不同标题
        //初始的是学生，若为老师设置如下：（被注释的代码）
//        TextView textView6 = findViewById(R.id.textView_othersData6);
//        TextView textView7 = findViewById(R.id.textView_othersData7);
//        textView6.setText("职称：");
//        textView7.setText("研究领域：");

        //姓名、年龄、性别、简介、学校、院系、研究方向、年级
        textView_name = (TextView)findViewById(R.id.textView_othersData_name);
        textView_age = (TextView)findViewById(R.id.textView_othersData_age);
        textView_sex = (TextView)findViewById(R.id.textView_othersData_sex);
        textView_intro = (TextView)findViewById(R.id.textView_othersData_intro);
        textView_school = (TextView)findViewById(R.id.textView_othersData_school);
        textView_department = (TextView)findViewById(R.id.textView_othersData_department);
        textView_range = (TextView)findViewById(R.id.textView_othersData_range);
        textView_grade = (TextView)findViewById(R.id.textView_othersData_grade);

        //todo 设置姓名等要素

        //按钮处理
        button_publish = (Button)findViewById(R.id.button_othersData_publish);
        button_publish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //todo 查看他人发布（先暂时搁置，页面还没写）

            }
        });

        button_favorite = (Button)findViewById(R.id.button_othersData_favorite);
        button_favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //todo 关注与取关,一开始可能也得处理一下文字便于复用
                button_favorite.setText("取消关注");
                //button_favorite.setText("关注");

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
