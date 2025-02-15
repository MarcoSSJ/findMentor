package com.example.findmentorapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class OthersDataActivity extends AppCompatActivity {


    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_age;
    private TextView textView_intro;
    private TextView textView_range;
    private TextView textView_school;
    private TextView textView_department;
    private TextView textView_toChat;
    private TextView textView_grade;
    private TextView textView6;
    private TextView textView7;
    private Button button_publish;
    private Button button_favorite;
    private ImageView imageView_data;
    private String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_data);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        //初始的是学生，若为老师设置如下：（被注释的代码）
        textView6 = findViewById(R.id.textView_othersData6);
        textView7 = findViewById(R.id.textView_othersData7);

        //姓名、年龄、性别、简介、学校、院系、研究方向、年级
        textView_name = (TextView)findViewById(R.id.textView_othersData_name);
        textView_age = (TextView)findViewById(R.id.textView_othersData_age);
        textView_sex = (TextView)findViewById(R.id.textView_othersData_sex);
        textView_intro = (TextView)findViewById(R.id.textView_othersData_intro);
        textView_school = (TextView)findViewById(R.id.textView_othersData_school);
        textView_department = (TextView)findViewById(R.id.textView_othersData_department);
        textView_range = (TextView)findViewById(R.id.textView_othersData_range);
        textView_grade = (TextView)findViewById(R.id.textView_othersData_grade);
        textView_toChat = (TextView)findViewById(R.id.textView_othersdata_tochat);
        textView_intro.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_range.setMovementMethod(ScrollingMovementMethod.getInstance());
        imageView_data = (ImageView) findViewById(R.id.imageView_othersData);
        button_favorite = (Button)findViewById(R.id.button_othersData_favorite);
        button_publish = (Button)findViewById(R.id.button_othersData_publish);

        //在这里进行网络访问取信息
        new Thread(runnable).start();
        new Thread(setHead).start();

        //按钮处理
        //查看他人动态
        button_publish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(OthersDataActivity.this, PublishActivity.class);
                //这里的id是别人的id
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        //关注与取关
        button_favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //button_favorite.setText("关注");
                new Thread(runFollow).start();
            }
        });

        textView_toChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(OthersDataActivity.this, ChatActivity.class);
                intent.putExtra("id",id);
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

    private Runnable runnable = new Runnable() {
        private String type = "";
        private String name = "";
        private String age = "";
        private String sex = "";
        private String intro = "";
        private String grade = "";
        private String school = "";
        private String department = "";
        private String range = "";
        private String condition = "";
        @Override
        public void run() {
            String others_data_url = Urls.api_url;
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(MyApplication.getContext(), "加载失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        if(type.equals("student"))
                        {
                            textView_name.setText(name);
                            textView_age.setText(age);
                            textView_department.setText(department);
                            textView_grade.setText(grade);
                            textView_intro.setText(intro);
                            textView_range.setText(range);
                            textView_school.setText(school);
                            textView_sex.setText(sex);
                            if(condition.equals("follow")) {
                                button_favorite.setText("取消关注");
                                button_favorite.setBackgroundResource(R.drawable.button_drawable_white);
                                button_favorite.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                            else
                                button_favorite.setText("关注");
//                                button_favorite.setBackgroundResource(R.drawable.button_drawable_big);
//                                button_favorite.setTextColor(getResources().getColor(R.color.white));
                        }
                        else
                        {
                            textView6.setText("职称：");
                            textView7.setText("研究领域：");
                            textView_name.setText(name);
                            textView_age.setText(age);
                            textView_department.setText(department);
                            textView_grade.setText(grade);
                            textView_intro.setText(intro);
                            textView_range.setText(range);
                            textView_school.setText(school);
                            textView_sex.setText(sex);
                            if(condition.equals("follow")) {
                                button_favorite.setText("取消关注");
                                button_favorite.setBackgroundResource(R.drawable.button_drawable_white);
                                button_favorite.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                            else
                                button_favorite.setText("关注");
//                                button_favorite.setBackgroundResource(R.drawable.button_drawable_big);
//                                button_favorite.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            };
            try {
                URL url = new URL(others_data_url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                //MyApplication application = (MyApplication) getActivity().getApplicationContext();
                MyApplication application = MyApplication.getInstance();
                String sessionID = application.getSessionID();

                String data = "action=ViewOthersData"+
                        "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                        "&id=" + URLEncoder.encode(id,"UTF-8");

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
                        type = obj.getString("type");
                        name = obj.getString("name");
                        age = obj.getString("age");
                        sex = obj.getString("sex");
                        intro = obj.getString("text");
                        grade = obj.getString("grade");
                        school = obj.getString("school");
                        department = obj.getString("department");
                        range = obj.getString("range");
                        condition = obj.getString("condition");


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

    private Runnable runFollow = new Runnable() {
        @Override
        public void run() {
        String follow_url = Urls.api_url;
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    //不成功，弹窗
                    Toast toast = Toast.makeText(MyApplication.getContext(), "关注失败", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (msg.what == 1) {
                    Toast toast = Toast.makeText(MyApplication.getContext(), "关注成功", Toast.LENGTH_SHORT);
                    toast.show();
                    button_favorite.setText("取消关注");
                    button_favorite.setBackgroundResource(R.drawable.button_drawable_white);
                    button_favorite.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (msg.what == 2) {
                    Toast toast = Toast.makeText(MyApplication.getContext(), "取消关注", Toast.LENGTH_SHORT);
                    toast.show();
                    button_favorite.setText("关注");
                    button_favorite.setBackgroundResource(R.drawable.button_drawable_big);
                    button_favorite.setTextColor(getResources().getColor(R.color.white));
                }
            }
        };
        try {
            URL url = new URL(follow_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            MyApplication application = MyApplication.getInstance();
            String sessionID = application.getSessionID();
            String data = "";
            if(button_favorite.getText().toString().equals("关注"))
                data = "action=Follow"+
                    "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                    "&id=" + URLEncoder.encode(id,"UTF-8");
            else
                data = "action=Unfollow"+
                        "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                        "&id=" + URLEncoder.encode(id,"UTF-8");

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
                    String condition = obj.getString("condition");
                    if(condition.equals("follow"))
                    {
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    else if(condition.equals("unfollow"))
                    {
                        Message message = Message.obtain();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                    else {
                        Message message = Message.obtain();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
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

    private Runnable setHead = new Runnable() {
        Bitmap bitmap = null;
        @Override
        public void run() {
            String others_data_url = Urls.api_url;
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(MyApplication.getContext(), "查看失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        imageView_data.setImageBitmap(bitmap);
                    }
                }
            };
            try {
                URL url = new URL(others_data_url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                //MyApplication application = (MyApplication) getActivity().getApplicationContext();
                MyApplication application = MyApplication.getInstance();
                String sessionID = application.getSessionID();

                String data = "action=DownloadPic"+
                        "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                        "&id=" + URLEncoder.encode(id,"UTF-8");

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
                        String imgStr = obj.getString("picture");
                        byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
}
