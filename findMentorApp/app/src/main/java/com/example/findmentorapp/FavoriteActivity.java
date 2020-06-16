package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmentorapp.ui.search.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class FavoriteActivity extends AppCompatActivity {

    String s_name[] = {};
    String s_text[] = {};
    String s_grade[] = {};
    String s_id[] = {};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new Thread(runnable).start();

        //RecyclerView相关函数
        final RecyclerView recyclerView_search = findViewById(R.id.recyclerView_favorite_mine);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        MyAdapter myAdapter = new MyAdapter();

        recyclerView_search.setAdapter(myAdapter);


    }

    class MyAdapter extends RecyclerView.Adapter {

        private Context mcontent;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyHolder myHolder = new MyHolder(LayoutInflater.from(FavoriteActivity.this).inflate(R.layout.item_recyclerview_favorite, null));
            //返回MyViewHolder的对象
            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            final MyHolder viewHolder = (MyHolder) holder;

            //将数据映射到控件中
            viewHolder.text.setText(s_text[position]);
            viewHolder.name.setText(s_name[position]);
            viewHolder.grade.setText(s_grade[position]);
            //用户id
            viewHolder.id = s_id[position];

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FavoriteActivity.this, OthersDataActivity.class);
                    intent.putExtra("id",viewHolder.id);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return s_name.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView text, name, grade;
            //标识号
            String id;

            public MyHolder(View itemView) {
                super(itemView);

                //实例化子对象，把对象和列表项布局文件中的id绑定

                text = itemView.findViewById(R.id.textView_item_favorite_text);
                name = itemView.findViewById(R.id.textView_item_favorite_name);
                grade = itemView.findViewById(R.id.textView_item_favorite_grade);

            }

        }
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
        @Override
        public void run() {
        String favorite_url = Urls.favorite_url;
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    //不成功，弹窗
                    Toast toast = Toast.makeText(MyApplication.getContext(), "加载失败", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (msg.what == 1) {
                }
            }
        };
        try {
            URL url = new URL(favorite_url);
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

            String data = "sessionID="+ URLEncoder.encode(sessionID,"UTF-8");

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

                    s_name = new String[]{};
                    s_text = new String[]{};
                    s_grade = new String[]{};
                    s_id = new String[]{};

                    JSONArray dataArray = obj.getJSONArray("data");
                    for (int i = 0;i<dataArray.length();i++)
                    {
                        JSONObject personData = dataArray.getJSONObject(i);
                        String text = personData.getString("text");
                        String name = personData.getString("name");
                        String grade = personData.getString("grade");
                        String id = personData.getString("id");

                        ArrayList<String> textList = new ArrayList<String>(s_text.length);
                        Collections.addAll(textList, s_text);
                        textList.add(text);
                        s_text = textList.toArray(new String[0]);

                        ArrayList<String> nameList = new ArrayList<String>(s_name.length);
                        Collections.addAll(nameList, s_name);
                        nameList.add(name);
                        s_name = nameList.toArray(new String[0]);

                        ArrayList<String> gradeList = new ArrayList<String>(s_grade.length);
                        Collections.addAll(gradeList, s_grade);
                        gradeList.add(id);
                        s_grade = gradeList.toArray(new String[0]);

                        ArrayList<String> idList = new ArrayList<String>(s_id.length);
                        Collections.addAll(idList, s_id);
                        idList.add(id);
                        s_id = idList.toArray(new String[0]);
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
}
