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
import android.widget.ImageView;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password_old;
    private EditText password_new1;
    private EditText password_new2;
    private ImageView imageView_data;
    private SharedPreferences sharedPreferences;
    private Button button;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Urls u = new Urls();
            String password1 = password_new1.getText().toString();
            String password2 = password_new2.getText().toString();
            String password = password_old.getText().toString();
            String fix_pwd_url = u.fix_pwd_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        finish();
                    } else if (msg.what == 2) {
                        Toast toast = Toast.makeText(getApplicationContext(), "与旧密码不一致", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            };

            if(!password1.equals(password2))
            {
                //两次输入的密码不对头
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
            else {
                try {
                    URL url = new URL(fix_pwd_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                    String data = "sessionID=" + URLEncoder.encode(sessionID, "UTF-8") +
                            "&password=" + URLEncoder.encode(password1, "UTF-8") +
                            "&password_old=" + URLEncoder.encode(password, "UTF-8");

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
                        String isconnect = obj.getString("result");
                        if (isconnect.equals("true")) {
                            String checked = obj.getString("checked");
                            if(checked.equals("true")){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                {
                                    editor.putString("password", password1);
                                    editor.apply();
                                }
                                Message message = Message.obtain();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                            else
                            {
                                Message message = Message.obtain();
                                message.what = 2;
                                handler.sendMessage(message);
                            }
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);

        password_old = (EditText)findViewById(R.id.editText_changePassword_lastpassword);
        password_new1 = (EditText)findViewById(R.id.editText_changePassword_newpassword1);
        password_new2 = (EditText)findViewById(R.id.editText_changePassword_newpassword2);

        imageView_data = (ImageView) findViewById(R.id.imageView_changePassword);
        //todo 设置头像
        //设置图片如imageView.setImageBitmap(photo)，photo为bitmap格式

        button = (Button)findViewById(R.id.button_changePassword);
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
