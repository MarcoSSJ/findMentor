package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PersonalDataSActivity extends AppCompatActivity {


    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_age;
    private TextView textView_intro;
    private TextView textView_range;
    private TextView textView_school;
    private TextView textView_department;
    private TextView textView_grade;
    private Button button;
    private ImageView imageView_data;
    private SharedPreferences sharedPreferences;

    String name = "";
    String age = "";
    String sex = "";
    String intro = "";
    String school = "";
    String department = "";
    String range = "";
    String grade = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_student);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //姓名、年龄、性别、简介、学校、院系、研究方向
        textView_name = findViewById(R.id.textView_personalData_name);
        textView_age = findViewById(R.id.textView_personalData_age);
        textView_sex = findViewById(R.id.textView_personalData_sex);
        textView_intro = findViewById(R.id.textView_personalData_intro);
        textView_school = findViewById(R.id.textView_personalData_school);
        textView_department = findViewById(R.id.textView_personalData_department);
        textView_range = findViewById(R.id.textView_personalData_range);
        textView_grade = findViewById(R.id.textView_personalData_grade);
        textView_intro.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_range.setMovementMethod(ScrollingMovementMethod.getInstance());

        imageView_data = (ImageView) findViewById(R.id.imageView_personalData);
        //设置图片如imageView.setImageBitmap(photo)，photo为bitmap格式
//        String filepath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
//        filepath += "/myhead.png";
//        try {
//            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filepath));
//            Bitmap bitmap = BitmapFactory.decodeStream(bis);
//            imageView_data.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            System.out.println("目前未设置头像");
//        }
        //new Thread(getHead).start();


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
        //在此处设置显示
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

        //new Thread(runnable).start();

        //开启修改简介的activity
        button = (Button)findViewById(R.id.button_personalData);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(PersonalDataSActivity.this, PersonalDataChangeSActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        new Thread(getHead).start();
        new Thread(runnable).start();


        super.onResume();

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

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
        String personal_data_change_url = Urls.api_url;

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    //不成功，弹窗
                    Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (msg.what == 1) {
                    textView_name.setText(name);
                    textView_age.setText(age);
                    textView_sex.setText(sex);
                    textView_intro.setText(intro);
                    textView_school.setText(school);
                    textView_department.setText(department);
                    textView_range.setText(range);
                    textView_grade.setText(grade);
                }
            }
        };
        try {

            URL url = new URL(personal_data_change_url);
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

            String data = "action=GetPersonalData" +
                    "&sessionID=" + URLEncoder.encode(sessionID, "UTF-8");

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
                    name = obj.getString("name");
                    age = obj.getString("age");
                    sex = obj.getString("sex");
                    intro = obj.getString("intro");
                    school = obj.getString("school");
                    department = obj.getString("department");
                    range = obj.getString("range");
                    grade = obj.getString("grade");

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

    };

    final Runnable getHead = new Runnable() {
        Bitmap bitmap = null;
        @Override
        public void run() {
            String personal_data_change_url = Urls.api_url;
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        imageView_data.setImageBitmap(bitmap);
                    }
                }
            };
            try {

                URL url = new URL(personal_data_change_url);
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
                String id = application.getID();

                String data = "action=DownloadPic" +
                        "&sessionID=" + URLEncoder.encode(sessionID, "UTF-8")+
                        "&id=" + URLEncoder.encode(id, "UTF-8");

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

                        String imgStr = obj.getString("picture");
                        byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //s_pic.add(bitmap);
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

    };
}
