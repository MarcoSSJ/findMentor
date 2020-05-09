package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.Instant;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    String username;
    String password;
    private Button loginButton;
    private Button registerButton;
    private EditText username_text;
    private EditText password_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //用户名与密码输入
        username_text = (EditText)findViewById(R.id.editText_log_username);
        password_text = (EditText)findViewById(R.id.editText_log_password);

        //处理点击登录按钮
        loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
            String username = username_text.getText().toString();
            String password = password_text.getText().toString();
            String loginURL = "x.x.x.x:xx";//后面再确定
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

                /*String data = "username="+ URLEncoder.encode(username,"UTF-8")+
                        "&password="+URLEncoder.encode(password,"UTF-8");*/
                String data = "";
                JSONObject obj = new JSONObject();
                obj.put("username",username);
                obj.put("password",password);
                data = obj.toString();

                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();

                InputStream is = conn.getInputStream();
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {

                    ByteArrayOutputStream message = new ByteArrayOutputStream();
                    String response = "";
                    byte[] b = new byte[1024];
                    int len ;
                    while((len = is.read(b))!=-1){
                        response += new String(b,0,len);
                    }
                    is.close();
                    System.out.println(response);
                    //需要根据response的形式来决定怎么处理：json？或者其他什么形式？

                    //以下为登陆成功跳转代码
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    //intent.putExtra("fragid",1); //添加Extra
                    startActivity(intent);
                    finish();


                    //不成功，弹窗
                    Toast toast=Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        });


        //处理点击注册按钮
        registerButton = (Button)findViewById(R.id.button_registered);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
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