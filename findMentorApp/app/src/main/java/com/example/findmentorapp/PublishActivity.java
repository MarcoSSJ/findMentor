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

public class PublishActivity extends AppCompatActivity {

    String s_title[] = {};
    String s_name[] = {};
    String s_text[] = {};
    String s_time[] = {};
    String s_infoID[] = {};
    String id;//用户id

    private MyAdapter myAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        new Thread(runnable).start();

        //RecyclerView相关函数
        final RecyclerView recyclerView_search = findViewById(R.id.recyclerView_publish);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        myAdapter = new PublishActivity.MyAdapter();

        recyclerView_search.setAdapter(myAdapter);

    }


    class MyAdapter extends RecyclerView.Adapter {

        private Context mcontent;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            PublishActivity.MyAdapter.MyHolder myHolder = new PublishActivity.MyAdapter.MyHolder(LayoutInflater.from(PublishActivity.this).inflate(R.layout.item_recyclerview_publish, null));
            //返回MyViewHolder的对象
            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            final PublishActivity.MyAdapter.MyHolder viewHolder = (PublishActivity.MyAdapter.MyHolder) holder;

            //将数据映射到控件中
            viewHolder.title.setText(s_title[position]);
            viewHolder.text.setText(s_text[position]);
            viewHolder.name.setText(s_name[position]);
            viewHolder.time.setText(s_time[position]);
            //活动id
            viewHolder.infoID = s_infoID[position];

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PublishActivity.this, InfoDetailActivity.class);
                    intent.putExtra("infoID",viewHolder.infoID);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return s_name.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView text, name, time, title;
            //标识号
            String infoID;

            public MyHolder(View itemView) {
                super(itemView);

                //实例化子对象，把对象和列表项布局文件中的id绑定
                title = itemView.findViewById(R.id.textView_item_publish_title);
                text = itemView.findViewById(R.id.textView_item_publish_text);
                name = itemView.findViewById(R.id.textView_item_publish_name);
                time = itemView.findViewById(R.id.textView_item_publish_time);

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
        String publish_url = Urls.api_url;
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    //不成功，弹窗
                    Toast toast = Toast.makeText(MyApplication.getContext(), "加载失败", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (msg.what == 1) {
                    //成功
                    myAdapter.notifyDataSetChanged();
                }
            }
        };
        try {
            URL url = new URL(publish_url);
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

            String data = "sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
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
                    s_title = new String[]{};
                    s_name = new String[]{};
                    s_text = new String[]{};
                    s_time = new String[]{};
                    s_infoID = new String[]{};
                    JSONArray dataArray = obj.getJSONArray("data");
                    for (int i = 0;i<dataArray.length();i++)
                    {
                        JSONObject personData = dataArray.getJSONObject(i);
                        String title = personData.getString("title");
                        String text = personData.getString("text");
                        String name = personData.getString("name");
                        String time = personData.getString("time");
                        String infoID = personData.getString("infoID");

                        ArrayList<String> titleList = new ArrayList<String>(s_title.length);
                        Collections.addAll(titleList, s_name);
                        titleList.add(title);
                        s_title = titleList.toArray(new String[0]);

                        ArrayList<String> textList = new ArrayList<String>(s_text.length);
                        Collections.addAll(textList, s_text);
                        textList.add(text);
                        s_text = textList.toArray(new String[0]);

                        ArrayList<String> nameList = new ArrayList<String>(s_name.length);
                        Collections.addAll(nameList, s_name);
                        nameList.add(name);
                        s_name = nameList.toArray(new String[0]);

                        ArrayList<String> timeList = new ArrayList<String>(s_time.length);
                        Collections.addAll(timeList, s_time);
                        timeList.add(time);
                        s_time = timeList.toArray(new String[0]);

                        ArrayList<String> infoIDList = new ArrayList<String>(s_infoID.length);
                        Collections.addAll(infoIDList, s_infoID);
                        infoIDList.add(infoID);
                        s_infoID = infoIDList.toArray(new String[0]);
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
