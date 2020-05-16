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

public class PersonalDataChangeActivity extends AppCompatActivity {

    private EditText editText_name;
    private RadioGroup radioGroup_sex;
    private RadioButton radioButton_male;
    private RadioButton radioButton_female;
    private EditText editText_age;
    private EditText editText_signature;
    private Button button;
    private String sex1;
    private SharedPreferences sharedPreferences;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            String name = editText_name.getText().toString();
            String age = editText_age.getText().toString();
            String signature = editText_signature.getText().toString();
            String loginURL = "http://www.mocky.io/v2/5eb7a69f3100000d00c8a200";

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        Intent intent = new Intent(PersonalDataChangeActivity.this,PersonalDataActivity.class);
                        //intent.putExtra("fragid",1); //添加Extra
                        startActivity(intent);
                        finish();
                    }
                }
            };
            try {
                URL url = new URL(loginURL);
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

                String data = "sessionID"+URLEncoder.encode(sessionID,"UTF-8")+
                        "&name="+ URLEncoder.encode(name,"UTF-8")+
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
                            editor.putString("name",name);
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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };


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
        String name = sharedPreferences.getString("name","");
        String age = sharedPreferences.getString("age","");
        final String sex = sharedPreferences.getString("sex","");
        String signature = sharedPreferences.getString("signature","");


        MyApplication application = (MyApplication) getApplicationContext();
        String sessionID = application.getSessionID();

        if(sessionID.equals(""))
        {
            editText_name.setText("请登录");
            editText_age.setText("请登录");
            radioButton_male.setChecked(true);
            editText_signature.setText("请登录");
        }
        else {
            if (name.equals(""))
                editText_name.setText("李四");
            else
                editText_name.setText(name);

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
                editText_signature.setText("请输入签名档");
            else
                editText_signature.setText(signature);
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
