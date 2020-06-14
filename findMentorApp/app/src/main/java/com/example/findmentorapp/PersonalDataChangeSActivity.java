package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PersonalDataChangeSActivity extends AppCompatActivity {
    private RadioGroup radioGroup_sex;
    private RadioButton radioButton_male;
    private RadioButton radioButton_female;
    private EditText editText_age;
    private EditText editText_inTro;
    private EditText editText_range;
    private EditText editText_grade;
    private Button button;
    private String sex1;
    private SharedPreferences sharedPreferences;

    //我是直接复制粘贴过来的
    //todo 学生个人信息修改，删除了姓名，加入了研究领域（range),签名变作简介，增加了年级

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String age = editText_age.getText().toString();
            String signature = editText_inTro.getText().toString();
            String personal_data_change_url = Urls.personal_data_change_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        Intent intent = new Intent(PersonalDataChangeSActivity.this, PersonalDataTActivity.class);
                        //intent.putExtra("fragid",1); //添加Extra
                        startActivity(intent);
                        finish();
                    }
                }
            };
            try {
                URL url = new URL(personal_data_change_url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                MyApplication application = (MyApplication) getApplicationContext();
                String sessionID = application.getSessionID();

                String data = "sessionID"+ URLEncoder.encode(sessionID,"UTF-8")+
                        "&age="+URLEncoder.encode(age,"UTF-8")+
                        "&sex="+URLEncoder.encode(sex1,"UTF-8")+
                        "&signature="+URLEncoder.encode(signature,"UTF-8");

                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();

                InputStream is = conn.getInputStream();
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    byte[] b = new byte[1024];
                    int len ;
                    while((len = is.read(b))!=-1){
                        response.append(new String(b, 0, len));
                    }
                    is.close();
                    conn.disconnect();

                    String res = new String(response);
                    System.out.println(res);
                    JSONObject obj = new JSONObject(res);
                    String isconnect = obj.getString("result");
                    if(isconnect.equals("true")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        {
                            editor.putString("sex",sex1);
                            editor.putString("age",age);
                            editor.putString("signature",signature);
                            editor.apply();
                        }
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    else
                    {
                        Message message = Message.obtain();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }
                else {
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            } catch (MalformedURLException e) {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
                e.printStackTrace();
            } catch (IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
                e.printStackTrace();
            } catch (JSONException e) {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
                e.printStackTrace();
            }
        }

    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_changedata_student);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //获取输入的性别等
        //todo 删除了姓名（我删了能找见的，检查一下），加入了研究领域与年级（这个得加入处理）

        radioGroup_sex=(RadioGroup)findViewById(R.id.radioGroup_personalDataChange);
        radioButton_male=(RadioButton)findViewById(R.id.radioButton_personalDataChange_male);
        radioButton_female=(RadioButton)findViewById(R.id.radioButton_personalDataChange_female);
        editText_age = (EditText)findViewById(R.id.editText_personalDataChange_age);
        editText_inTro = (EditText)findViewById(R.id.editText_personalDataChange_inTro);
        editText_range = (EditText)findViewById(R.id.editText_personalDataChange_range);
        editText_grade = (EditText)findViewById(R.id.editText_personalDataChange_grade);

        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String age = sharedPreferences.getString("age","");
        final String sex = sharedPreferences.getString("sex","");
        String signature = sharedPreferences.getString("signature","");


        MyApplication application = (MyApplication) getApplicationContext();
        String sessionID = application.getSessionID();

        if(sessionID.equals(""))
        {
            editText_age.setText("请登录");
            radioButton_male.setChecked(true);
            editText_inTro.setText("请登录");
        }
        else {

            if (age.equals(""))
                editText_age.setText("0");
            else
                editText_age.setText(age);

            if(sex.equals(""))
                radioButton_male.setChecked(true);
            else if(sex.equals("male"))
                radioButton_male.setChecked(true);
            else
                radioButton_female.setChecked(true);

            if (signature.equals(""))
                editText_inTro.setText("请输入个人信息");
            else
                editText_inTro.setText(signature);
        }

        //以下为sex的获取代码
        radioGroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String temp=null;
                if(radioButton_male.getId()==checkedId){
                    temp="male";
                }
                if(radioButton_female.getId()==checkedId){
                    temp="female";
                }
                sex1 = temp;
            }
        });


        button = (Button)findViewById(R.id.button_personalDataChange);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new Thread(runnable).start();
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
