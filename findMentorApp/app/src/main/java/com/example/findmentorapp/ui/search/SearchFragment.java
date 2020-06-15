package com.example.findmentorapp.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmentorapp.InfoDetailActivity;
import com.example.findmentorapp.MyApplication;
import com.example.findmentorapp.R;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    //todo 搜索界面的recyView中内容修改，分别为姓名、年级（职称）、text（先设为研究方向（兴趣））
    String s_name[] = {"内容一", "内容一", "内容一"};
    String s_text[] = {"内容二", "内容二", "内容二"};
    String s_grade[] = {"内容", "内容", "内容"};
    //存放用户id
    String s_id[] = {"1","2","3"};



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

        if(sessionID.equals("")) {
            textView.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
            recyclerView_search.setVisibility(View.INVISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            recyclerView_search.setVisibility(View.VISIBLE);
        }

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                //Todo 点击搜索按钮后操作
                String searchText = searchEditText.getText().toString();
            }
        });

        //RecyclerView相关函数，暂时放在下面。以后可能需要放入ViewModel

        recyclerView_search.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();

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

            MyHolder viewHolder = (MyHolder) holder;

            //将数据映射到控件中
            viewHolder.text.setText(s_text[position]);
            viewHolder.name.setText(s_name[position]);
            viewHolder.grade.setText(s_grade[position]);
            //用户id
            viewHolder.id = s_id[position];
            
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo 点击item事件,以后需要从这里跳到详情页，需要传去参数（position表示所在位置‘下面类中加了个id可以用来存id用于跳转）
                    Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
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
                text = itemView.findViewById(R.id.textView_item_forSearch_text);
                name = itemView.findViewById(R.id.textView_item_forSearch_name);
                grade = itemView.findViewById(R.id.textView_item_forSearch_grade);

            }

        }
    }






}
