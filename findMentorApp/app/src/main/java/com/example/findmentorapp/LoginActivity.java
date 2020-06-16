package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
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
    private SharedPreferences sharedPreferences;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String username = username_text.getText().toString();
            String password = password_text.getText().toString();
            String log_in_url = Urls.api_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        //以下为登陆成功跳转代码
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //intent.putExtra("fragid",1); //添加Extra
                        startActivity(intent);
                        finish();
                    }

                }
            };
            try {
                URL url = new URL(log_in_url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                String data = "action=Login"+"&username="+ URLEncoder.encode(username,"UTF-8")+
                        "&password="+URLEncoder.encode(password,"UTF-8");

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
                    String login = obj.getString("result");
                    if(login.equals("true")) {

                        String sessionID = obj.getString("sessionID");
                        String type = obj.getString("type");
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String school = obj.getString("school");
                        String department = obj.getString("department");

                        MyApplication application = (MyApplication) getApplicationContext();
                        application.setSessionID(sessionID);
                        //登录成功后在sharedpreference存入用户名和密码
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        {
                            editor.putBoolean("remenberpass",true);
                            editor.putString("username",username);
                            editor.putString("password",password);
//                            editor.putString("type",type);
//                            editor.putString("id",id);
//                            editor.putString("name",name);
//                            editor.putString("school",school);
//                            editor.putString("department",department);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //用户名与密码输入
        username_text = (EditText)findViewById(R.id.editText_log_username);
        password_text = (EditText)findViewById(R.id.editText_log_password);

//        如果有存储好的用户名和密码就直接取出来
        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);
        boolean isRemenber=sharedPreferences.getBoolean("remenberpass",false);
        if(isRemenber)
        {
            username_text.setText(sharedPreferences.getString("username",""));
            password_text.setText(sharedPreferences.getString("password",""));
        }

        //处理点击登录按钮
        loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new Thread(runnable).start();
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