package com.example.findmentorapp;

import android.content.Context;
import android.os.Bundle;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //todo message添加方式如下，注意区分发送方，请按时间顺序排序，如9点的消息位置在8点之后
        Msg msg1 = new Msg("Hello, how are you?", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Fine, thank you, and you?", Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("I am fine, too!", Msg.TYPE_RECEIVED);
        msgList.add(msg3);

        adapter = new MsgAdapter(ChatActivity.this, R.layout.item_message_detail, msgList);
        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
        msgListView = (ListView)findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                    //todo 上传服务器（如果失败记得把List里最后一项删了）

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
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            if (msg.getType() == Msg.TYPE_RECEIVED) {
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.head1.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.head2.setVisibility(View.GONE);
                viewHolder.leftMsg.setText(msg.getContent());
            } else if (msg.getType() == Msg.TYPE_SEND) {
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

}
