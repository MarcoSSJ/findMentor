package com.example.findmentorapp.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.findmentorapp.LoginActivity;
import com.example.findmentorapp.MyApplication;
import com.example.findmentorapp.R;
import com.example.findmentorapp.RegisterActivity;
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

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel =
                ViewModelProviders.of(this).get(AddViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        final TextView textView = root.findViewById(R.id.textView_add);
        addViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //获取登陆状态
        MyApplication application = (MyApplication) getActivity().getApplication();
        String sessionID = application.getSessionID();

        textView.setText("未登录，请登录");
        textView.setVisibility(View.INVISIBLE);

        final TextView textView1 = root.findViewById(R.id.textView_add_1);
        final TextView textView2 = root.findViewById(R.id.textView_add_2);
        final ImageView imageView_lock = (ImageView)root.findViewById(R.id.imageView_lock);
        //标题与信息输入
        final EditText editText_title = root.findViewById(R.id.editText_add_title);
        final EditText editText_info = root.findViewById(R.id.editText_add_info);
        Button button_submit = (Button)root.findViewById(R.id.button_add_submit);


        if(sessionID.equals("")) {
            textView.setVisibility(View.VISIBLE);
            imageView_lock.setVisibility(View.VISIBLE);
            editText_title.setVisibility(View.INVISIBLE);
            editText_info.setVisibility(View.INVISIBLE);
            button_submit.setVisibility(View.INVISIBLE);
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            imageView_lock.setVisibility(View.INVISIBLE);
            editText_title.setVisibility(View.VISIBLE);
            editText_info.setVisibility(View.VISIBLE);
            button_submit.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        }


        button_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new Thread(runnable).start();
            }
        });

        return root;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String api_url = Urls.api_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //弹窗
                        Toast toast = Toast.makeText(MyApplication.getContext(), "发布失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        Toast toast = Toast.makeText(MyApplication.getContext(), "发布成功", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            };


            try {
                URL url = new URL(api_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                String data = "action=add"+"&type=" + URLEncoder.encode("teacher","UTF-8");

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

    };
}
