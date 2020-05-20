package com.example.findmentorapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmentorapp.R;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    String s_tilte[] = {"内容一", "内容一", "内容一"};
    String s_text[] = {"内容二", "内容二", "内容二"};
    String s_name[] = {"内容", "内容", "内容"};
    String s_time[] = {"内容1", "内容1", "内容1"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        final TextView textView = root.findViewById(R.id.text_search);
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final RecyclerView recyclerView_search = root.findViewById(R.id.recyclerView_search_forSerach);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        MyAdapter myAdapter = new MyAdapter();

        recyclerView_search.setAdapter(myAdapter);



        return root;
    }

    class MyAdapter extends RecyclerView.Adapter{


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyHolder myHolder = new MyHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_recyclerview_forsearch, null));
            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            MyHolder mm = (MyHolder) holder;

            //将数据映射到控件中
            mm.title.setText(s_tilte[position]);
            mm.text.setText(s_text[position]);
            mm.name.setText(s_name[position]);
            mm.time.setText(s_time[position]);
        }

        @Override
        public int getItemCount() {
            return s_tilte.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView title, text, name, time;

            public MyHolder(View itemView) {
                super(itemView);

                //实例化子对象，把对象和列表项布局文件中的id绑定
                title = itemView.findViewById(R.id.textView_item_forSearch_title);
                text = itemView.findViewById(R.id.textView_item_forSearch_text);
                name = itemView.findViewById(R.id.textView_item_forSearch_name);
                time = itemView.findViewById(R.id.textView_item_forSearch_time);

            }

        }
    }






}
