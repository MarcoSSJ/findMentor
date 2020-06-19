package com.example.findmentorapp.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmentorapp.InfoDetailActivity;
import com.example.findmentorapp.MyApplication;
import com.example.findmentorapp.OthersDataActivity;
import com.example.findmentorapp.PersonalDataChangeSActivity;
import com.example.findmentorapp.PersonalDataTActivity;
import com.example.findmentorapp.R;
import com.example.findmentorapp.Urls;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private MyAdapter myAdapter;

    String s_name[] = {};
    String s_text[] = {};
    String s_grade[] = {};
    //存放用户id
    String s_id[] = {};
    Bitmap s_pic[] = {};

    String searchText;
    String searchSelect; //标识搜索选项，换成int也无所谓


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        final TextView textView = root.findViewById(R.id.textView_search);
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //获取登陆状态
        MyApplication application = (MyApplication) getActivity().getApplication();
        String sessionID = application.getSessionID();

        textView.setText("未登录，请登录");
        final EditText searchEditText = root.findViewById(R.id.editText_search_searchByWord);
        final Button searchButton = root.findViewById(R.id.button_search_searchByText);
        final RecyclerView recyclerView_search = root.findViewById(R.id.recyclerView_search_forSerach);
        final RadioGroup radioGroup_search=root.findViewById(R.id.radioGroup_search);
        final RadioButton radioButton_byname=root.findViewById(R.id.radioButton_search_name);
        final RadioButton radioButton_byschool=root.findViewById(R.id.radioButton_search_school);
        final RadioButton radioButton_byrange=root.findViewById(R.id.radioButton_search_range);
        final ConstraintLayout search_layout = root.findViewById(R.id.searchLayout);
        final ImageView imageView_lock = (ImageView)root.findViewById(R.id.imageView_lock);
        searchSelect = "name";

        if(sessionID.equals("")) {
            textView.setVisibility(View.VISIBLE);
            imageView_lock.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.INVISIBLE);
            recyclerView_search.setVisibility(View.INVISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            imageView_lock.setVisibility(View.INVISIBLE);
            search_layout.setVisibility(View.VISIBLE);
            recyclerView_search.setVisibility(View.VISIBLE);
        }



        radioGroup_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String temp="name";
                if(radioButton_byname.getId()==checkedId){
                    temp="name";
                }
                if(radioButton_byschool.getId()==checkedId){
                    temp="school";
                }
                if(radioButton_byrange.getId()==checkedId){
                    temp="range";
                }
                searchSelect = temp;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                searchText = searchEditText.getText().toString();
                new Thread(runnable).start();
            }
        });

        //RecyclerView相关函数，暂时放在下面。以后可能需要放入ViewModel

        recyclerView_search.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();

        recyclerView_search.setAdapter(myAdapter);

        return root;
    }

    class MyAdapter extends RecyclerView.Adapter{

        private Context mcontent;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyHolder myHolder = new MyHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_recyclerview_forsearch, null));


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
                    Intent intent = new Intent(getActivity(), OthersDataActivity.class);
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

            MyHolder(View itemView) {
                super(itemView);

                //实例化子对象，把对象和列表项布局文件中的id绑定
                text = itemView.findViewById(R.id.textView_item_forSearch_text);
                name = itemView.findViewById(R.id.textView_item_forSearch_name);
                grade = itemView.findViewById(R.id.textView_item_forSearch_grade);

            }

        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        String personal_data_change_url = Urls.api_url;
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    //不成功，弹窗
                    Toast toast = Toast.makeText(MyApplication.getContext(), "搜索失败", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (msg.what == 1) {
                    //成功
                    myAdapter.notifyDataSetChanged();
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

            String data = "action=Search"+
                    "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                    "&searchText=" + URLEncoder.encode(searchText,"UTF-8")+
                    "&searchSelect=" + URLEncoder.encode(searchSelect,"UTF-8");

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
                    ArrayList<String> nameList = new ArrayList<String>();
                    ArrayList<String> textList = new ArrayList<String>();
                    ArrayList<String> gradeList = new ArrayList<String>();
                    ArrayList<String> idList = new ArrayList<String>();
                    for (int i = 0;i<dataArray.length();i++)
                    {
                        JSONObject personData = dataArray.getJSONObject(i);
                        String name = personData.getString("name");
                        String text = personData.getString("text");
                        String grade = personData.getString("grade");
                        String id = personData.getString("id");

                        nameList.add(name);
                        textList.add(text);
                        gradeList.add(grade);
                        idList.add(id);

                    }
                    s_name = nameList.toArray(new String[0]);
                    s_text = textList.toArray(new String[0]);
                    s_grade = gradeList.toArray(new String[0]);
                    s_id = idList.toArray(new String[0]);

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

            int count = 0;
            s_pic = new Bitmap[]{};
            ArrayList<Bitmap> picList = new ArrayList<Bitmap>(s_id.length);

            while (count <= s_id.length) {
                try {
                    String api_url = Urls.api_url;
                    URL url = new URL(api_url);
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

                    String data = "action=DownloadPic" +
                            "&sessionID=" + URLEncoder.encode(sessionID, "UTF-8")+
                            "&id=" + URLEncoder.encode(s_id[count], "UTF-8");
                    count++;
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
                            Bitmap bitmap = null;
                            String imgStr = obj.getString("picture");
                            byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            picList.add(bitmap);

                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            s_pic = picList.toArray(new Bitmap[0]);
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);
        }

    };
}
