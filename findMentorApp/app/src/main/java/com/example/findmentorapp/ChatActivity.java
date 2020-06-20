package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class ChatActivity extends AppCompatActivity {

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    Bitmap bitmap1;//对方头像
    Bitmap bitmap2;//自己头像
    private List<Msg> msgList = new ArrayList<Msg>();

    private String id;//在和谁聊天
    private Msg msg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

//        Msg msg1 = new Msg("Hello, how are you?", Msg.TYPE_RECEIVED);
//        msgList.add(msg1);
//        Msg msg2 = new Msg("Fine, thank you, and you?", Msg.TYPE_SEND);
//        msgList.add(msg2);
//        Msg msg3 = new Msg("I am fine, too!", Msg.TYPE_RECEIVED);
//        msgList.add(msg3);


        bitmap1 = BitmapFactory.decodeFile("@drawable/head1");
        bitmap2 = BitmapFactory.decodeFile("@drawable/head2");

        //todo 在这底下添加线程获取头像bitmap放在bitmap1，bitmap2中，2为自己头像

        adapter = new MsgAdapter(ChatActivity.this, R.layout.item_message_detail, msgList);
        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
        msgListView = (ListView)findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);

        new Thread(getMsgList).start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    msg = new Msg(content, Msg.TYPE_SEND);

                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                    new Thread(sendMessage).start();
                }
            }
        });

    }

    class MsgAdapter extends ArrayAdapter<Msg> {
        private int resourceId;

        public MsgAdapter(Context context, int textViewResourceId, List<Msg> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Msg msg = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
                viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
                viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
                viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
                viewHolder.head1 = (ImageView) view.findViewById(R.id.head_left);
                viewHolder.head2 = (ImageView) view.findViewById(R.id.head_right);
                viewHolder.head1.setImageBitmap(bitmap1);
                viewHolder.head2.setImageBitmap(bitmap2);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (msg.getType() == Msg.TYPE_RECEIVED) {
                //收到的消息
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.head1.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.head2.setVisibility(View.GONE);
                viewHolder.leftMsg.setText(msg.getContent());
            } else if (msg.getType() == Msg.TYPE_SEND) {
                //没收到的消息
                viewHolder.rightLayout.setVisibility(View.VISIBLE);
                viewHolder.head2.setVisibility(View.VISIBLE);
                viewHolder.leftLayout.setVisibility(View.GONE);
                viewHolder.head1.setVisibility(View.GONE);
                viewHolder.rightMsg.setText(msg.getContent());
            }
            return view;
        }

        class ViewHolder {
            LinearLayout leftLayout;
            LinearLayout rightLayout;
            TextView leftMsg;
            TextView rightMsg;
            ImageView head1;
            ImageView head2;
        }
    }


    class Msg {
        public static final int TYPE_RECEIVED = 0;
        public static final int TYPE_SEND = 1;

        private String content;
        private int type;

        public Msg(String content, int type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public int getType() {
            return type;
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

    private Runnable sendMessage = new Runnable() {
        @Override
        public void run() {
            String personal_data_change_url = Urls.api_url;
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    if (message.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(MyApplication.getContext(), "发送失败", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            };
            try {
                URL url = new URL(personal_data_change_url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setChunkedStreamingMode(0);
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
                String senderID = application.getID();

                String data = "action=SendMessage"+
                        "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                        "&sendText=" + URLEncoder.encode(msg.content,"UTF-8")+
                        "&receiverID=" + URLEncoder.encode(id,"UTF-8")+
                        "&senderID=" + URLEncoder.encode(senderID,"UTF-8");

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

    private Runnable getMsgList = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String personal_data_change_url = Urls.api_url;
                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        super.handleMessage(message);
                        if (message.what == 0) {
                            //不成功，弹窗
                            Toast toast = Toast.makeText(MyApplication.getContext(), "发送失败", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (message.what == 1) {
                            //成功
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
                try {
                    URL url = new URL(personal_data_change_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setChunkedStreamingMode(0);
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

                    String data = "action=GetMessageList" +
                            "&sessionID=" + URLEncoder.encode(sessionID, "UTF-8") +
                            "&chatWith=" + URLEncoder.encode(id, "UTF-8");

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
                        msgList.clear();
                        if (isconnect.equals("true")) {
                            JSONArray dataArray = obj.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject personData = dataArray.getJSONObject(i);
                                String text = personData.getString("text");
                                String senderID = personData.getString("senderID");
                                if (senderID.equals(id))//别人发来的
                                    msgList.add(new Msg(text, Msg.TYPE_RECEIVED));
                                else//自己发出去的
                                    msgList.add(new Msg(text, Msg.TYPE_SEND));
                            }

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
