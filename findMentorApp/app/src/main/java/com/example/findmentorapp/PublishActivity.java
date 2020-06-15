package com.example.findmentorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PublishActivity extends AppCompatActivity {


    //todo 1.动态列表界面的recyView中内容修改，分别为标题、姓名、内容摘要、时间
    //todo 2.获取传进来的用户id从而穿给后端获取对应列表
    String s_title[] = {"内容一", "内容一", "内容一"};
    String s_name[] = {"内容一", "内容一", "内容一"};
    String s_text[] = {"内容二", "内容二", "内容二"};
    String s_time[] = {"内容1", "内容1", "内容1"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //RecyclerView相关函数
        final RecyclerView recyclerView_search = findViewById(R.id.recyclerView_publish);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        PublishActivity.MyAdapter myAdapter = new PublishActivity.MyAdapter();

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

            PublishActivity.MyAdapter.MyHolder viewHolder = (PublishActivity.MyAdapter.MyHolder) holder;

            //将数据映射到控件中
            viewHolder.title.setText(s_title[position]);
            viewHolder.text.setText(s_text[position]);
            viewHolder.name.setText(s_name[position]);
            viewHolder.time.setText(s_time[position]);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo 点击item事件,以后需要从这里跳到详情页，需要传去参数（position表示所在位置‘下面类中加了个id可以用来存id用于跳转）
                    Intent intent = new Intent(PublishActivity.this, InfoDetailActivity.class);
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
            int id;

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


}
