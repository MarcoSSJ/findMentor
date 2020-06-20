package com.example.findmentorapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.findmentorapp.AccountActivity;
import com.example.findmentorapp.FavoriteActivity;
import com.example.findmentorapp.LoginActivity;
import com.example.findmentorapp.MyApplication;
import com.example.findmentorapp.PersonalDataSActivity;
import com.example.findmentorapp.PersonalDataTActivity;
import com.example.findmentorapp.PublishActivity;
import com.example.findmentorapp.R;
import com.example.findmentorapp.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private Button toLogButton;
    private LinearLayout layout_toAccount;
    private LinearLayout layout_toFavorite;
    private LinearLayout layout_toPublish;
    private TextView toPersonalData;
    private ConstraintLayout layout1;
    private ImageView imageView_lock;
    private TextView textView;

    private ImageView imageView_user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final MyApplication application = MyApplication.getInstance();

        //跳转登录界面
        toLogButton = (Button) root.findViewById(R.id.toLogin);
        layout_toAccount = (LinearLayout)root.findViewById(R.id.layout_home_account);
        layout_toFavorite = (LinearLayout)root.findViewById(R.id.layout_home_favorite);
        layout_toPublish = (LinearLayout)root.findViewById(R.id.layout_home_publish);
        toPersonalData = (TextView)root.findViewById(R.id.textView_home_data);
        layout1 = (ConstraintLayout)root.findViewById(R.id.layout_home1);
        imageView_lock = (ImageView)root.findViewById(R.id.imageView_lock);

        imageView_user = (ImageView)root.findViewById(R.id.imageView_home_user);

        toPersonalData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                String type = application.getType();
                if(type.equals("teacher")) {
                    //老师跳转
                    Intent intent = new Intent(getActivity(), PersonalDataTActivity.class);
                    startActivity(intent);
                }
                else {
                    //学生跳转
                    Intent intent = new Intent(getActivity(), PersonalDataSActivity.class);
                    startActivity(intent);
                }
            }
        });

        toLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        layout_toAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        //跳转关注列表
        layout_toFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {

                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
            }
        });

        //跳转动态页面
        layout_toPublish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                String id = application.getID();
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication application = MyApplication.getInstance();
        String sessionID = application.getSessionID();


        if(sessionID.equals("")) {
            toLogButton.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            imageView_lock.setVisibility(View.VISIBLE);
            layout_toAccount.setVisibility(View.INVISIBLE);
            layout_toFavorite.setVisibility(View.INVISIBLE);
            layout_toPublish.setVisibility(View.INVISIBLE);
            toPersonalData.setVisibility(View.INVISIBLE);
            layout1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {
            layout_toAccount.setVisibility(View.VISIBLE);
            layout_toFavorite.setVisibility(View.VISIBLE);
            layout_toPublish.setVisibility(View.VISIBLE);
            toLogButton.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            toPersonalData.setVisibility(View.VISIBLE);
            imageView_lock.setVisibility(View.INVISIBLE);
            layout1.setBackgroundColor(Color.parseColor("#6200EE"));
            new Thread(getHead).start();
        }
    }

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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "头像获取失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        imageView_user.setImageBitmap(bitmap);
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

                MyApplication application = (MyApplication) getActivity().getApplicationContext();
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
