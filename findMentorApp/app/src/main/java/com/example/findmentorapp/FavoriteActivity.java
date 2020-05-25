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

import com.example.findmentorapp.ui.search.SearchFragment;

public class FavoriteActivity extends AppCompatActivity {

    String s_name[] = {"内容一", "内容一", "内容一"};
    String s_text[] = {"内容二", "内容二", "内容二"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

            MyHolder viewHolder = (MyHolder) holder;

            //将数据映射到控件中
            viewHolder.text.setText(s_text[position]);
            viewHolder.name.setText(s_name[position]);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo 点击item事件,以后需要从这里跳到详情页，需要传去参数（position表示所在位置‘下面类中加了个id可以用来存id用于跳转）
                    Intent intent = new Intent(FavoriteActivity.this, OthersDataActivity.class);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return s_name.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView text, name;
            //标识号
            int id;

            public MyHolder(View itemView) {
                super(itemView);

                //实例化子对象，把对象和列表项布局文件中的id绑定

                text = itemView.findViewById(R.id.textView_item_favorite_info);
                name = itemView.findViewById(R.id.textView_item_favorite_name);


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
