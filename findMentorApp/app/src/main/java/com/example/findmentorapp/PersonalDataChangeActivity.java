package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalDataChangeActivity extends AppCompatActivity {

    private EditText editText_name;
    private RadioGroup radioGroup_sex;
    private RadioButton radioButton_male;
    private RadioButton radioButton_female;
    private EditText editText_age;
    private EditText editText_signature;
    private Button button;
    private SharedPreferences sharedPreferences;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_changedata);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //获取输入的姓名等

        editText_name = (EditText)findViewById(R.id.editText_personalDataChange_name);
        radioGroup_sex=(RadioGroup)findViewById(R.id.radioGroup_personalDataChange);
        radioButton_male=(RadioButton)findViewById(R.id.radioButton_personalDataChange_male);
        radioButton_female=(RadioButton)findViewById(R.id.radioButton_personalDataChange_female);
        editText_age = (EditText)findViewById(R.id.editText_personalDataChange_age);
        editText_signature = (EditText)findViewById(R.id.editText_personalDataChange_signature);

        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);

        //todo：预先设置name等，然后获取用户输入的
        editText_name.setText("李四");

        //以下为sex的获取代码
        String temp= null; //temp即为最后结果
        radioGroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                String temp=null;
                if(radioButton_male.getId()==checkedId){
                    temp=radioButton_male.getText().toString();
                }
                if(radioButton_female.getId()==checkedId){
                    temp=radioButton_female.getText().toString();
                }
            }
        });


        //todo:修改完成按钮处理
        button = (Button)findViewById(R.id.button_personalDataChange);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(PersonalDataChangeActivity.this,PersonalDataActivity.class);
                //intent.putExtra("fragid",1); //添加Extra
                startActivity(intent);
                finish();
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
