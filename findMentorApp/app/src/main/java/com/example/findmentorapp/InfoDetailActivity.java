package com.example.findmentorapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InfoDetailActivity extends AppCompatActivity{

    private TextView textView_title;
    private TextView textView_name;
    private TextView textView_personInfo;
    private TextView textView_detailInfo;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infodetail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //各种信息
        //todo 本页面暂时先不管等动态能提交了再处理
        textView_title = (TextView)findViewById(R.id.textView_infoDetail_title);
        textView_name = (TextView)findViewById(R.id.textView_infoDetail_name);
        textView_personInfo = (TextView)findViewById(R.id.textView_infoDetail_personInfo);
        textView_detailInfo = (TextView)findViewById(R.id.textView_infoDetail_detailInfo);
        textView_detailInfo.setMovementMethod(ScrollingMovementMethod.getInstance());




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
