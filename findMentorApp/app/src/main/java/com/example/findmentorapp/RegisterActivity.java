package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText username_text;
    private EditText password_text1;
    private EditText password_text2;
//    private SharedPreferences sharedPreferences;

    private EditText emailAddress_text;
    private EditText name_text;
    private EditText school_text;
    private EditText department_text;


    private Button button_tearcher;
    private Button button_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //用户名与密码、确认密码
        username_text = (EditText)findViewById(R.id.editText_register_username);
        password_text1 = (EditText)findViewById(R.id.editText_register_password1);
        password_text2 = (EditText)findViewById(R.id.editText_register_password2);

        //各种信息
        emailAddress_text = (EditText)findViewById(R.id.editText_register_email);
        name_text = (EditText)findViewById(R.id.editText_register_name);
        school_text = (EditText)findViewById(R.id.editText_register_school);
        department_text = (EditText)findViewById(R.id.editText_register_department);

        //注册为老师
        button_tearcher = (Button)findViewById(R.id.button_register_teacher);
        button_tearcher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new Thread(runnable1).start();
            }
        });

        //注册为学生
        button_student = (Button)findViewById(R.id.button_register_student);
        button_student.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new Thread(runnable2).start();
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

    //注册为老师
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            String username = username_text.getText().toString();
            String password = password_text1.getText().toString();
            String password2 = password_text2.getText().toString();
            String email = emailAddress_text.getText().toString();
            String name = name_text.getText().toString();
            String school = school_text.getText().toString();
            String department = department_text.getText().toString();
            String register_url = Urls.api_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        //以下为注册成功跳转代码
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        //intent.putExtra("fragid",1); //添加Extra
                        startActivity(intent);
                        finish();
                    } else if (msg.what == 2) {
                        //弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "两次密码不一致！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            };

            if (!password.equals(password2)) {
                //两次输入的密码不对头
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            } else {
                try {
                    URL url = new URL(register_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);

                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded;charset=UTF-8");

                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);

                    String data = "action=Register"+"&type=" + URLEncoder.encode("teacher","UTF-8") +
                            "&username=" + URLEncoder.encode(username, "UTF-8") +
                            "&password=" + URLEncoder.encode(password, "UTF-8") +
                            "&email=" + URLEncoder.encode(email, "UTF-8") +
                            "&name=" + URLEncoder.encode(name, "UTF-8") +
                            "&school=" + URLEncoder.encode(school, "UTF-8") +
                            "&department=" + URLEncoder.encode(department, "UTF-8");

                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    out.close();

                    InputStream is = conn.getInputStream();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        byte[] b = new byte[1024];
                        int len;
                        while ((len = is.read(b)) != -1) {
                            response.append(new String(b, 0, len));
                        }
                        is.close();
                        conn.disconnect();

                        String res = new String(response);
                        System.out.println(res);
                        JSONObject obj = new JSONObject(res);
                        String login = obj.getString("result");
                        if (login.equals("true")) {
                            Message message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            Message message = Message.obtain();
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                    } else {
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
        }
    };

    //注册为学生
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            String username = username_text.getText().toString();
            String password = password_text1.getText().toString();
            String password2 = password_text2.getText().toString();
            String email = emailAddress_text.getText().toString();
            String name = name_text.getText().toString();
            String school = school_text.getText().toString();
            String department = department_text.getText().toString();
            String register_url = Urls.api_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        //以下为注册成功跳转代码
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        //intent.putExtra("fragid",1); //添加Extra
                        startActivity(intent);
                        finish();
                    } else if (msg.what == 2) {
                        //弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "两次密码不一致！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            };

            if (!password.equals(password2)) {
                //两次输入的密码不对头
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            } else {
                try {
                    URL url = new URL(register_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);

                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded;charset=UTF-8");

                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);

                    String data = "action=Register"+"&type=" + URLEncoder.encode("student","UTF-8")+
                            "&username=" + URLEncoder.encode(username, "UTF-8") +
                            "&password=" + URLEncoder.encode(password, "UTF-8") +
                            "&email=" + URLEncoder.encode(email, "UTF-8") +
                            "&name=" + URLEncoder.encode(name, "UTF-8") +
                            "&school=" + URLEncoder.encode(school, "UTF-8") +
                            "&department=" + URLEncoder.encode(department, "UTF-8")
                    ;

                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();
                    out.close();

                    InputStream is = conn.getInputStream();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        byte[] b = new byte[1024];
                        int len;
                        while ((len = is.read(b)) != -1) {
                            response.append(new String(b, 0, len));
                        }
                        is.close();
                        conn.disconnect();

                        String res = new String(response);
                        System.out.println(res);
                        JSONObject obj = new JSONObject(res);
                        String login = obj.getString("result");
                        if (login.equals("true")) {
                            Message message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            Message message = Message.obtain();
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                    } else {
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
        }
    };

}
