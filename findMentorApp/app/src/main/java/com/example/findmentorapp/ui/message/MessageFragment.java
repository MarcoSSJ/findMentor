package com.example.findmentorapp.ui.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmentorapp.ChatActivity;
import com.example.findmentorapp.MyApplication;
import com.example.findmentorapp.OthersDataActivity;
import com.example.findmentorapp.R;
import com.example.findmentorapp.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private MessageViewModel messageViewModel;
    private MyAdapter myAdapter;

    //存放消息数据的ArrayList
    private List<MessageBrief> s_message;
    private List<MessageBrief> mFilterList;
    String s_name[] = {"test1","test2","test11","test12"};
    String s_text[] = {"test1","test2","test11","test12"};
    String s_time[] = {"test1","test2","test11","test12"};
    //存放用户id
    String s_id[] = {"1","2","3","4"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messageViewModel =
                ViewModelProviders.of(this).get(MessageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_massage, container, false);
        final TextView textView = root.findViewById(R.id.text_message);
        messageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //获取登陆状态
        MyApplication application = (MyApplication) getActivity().getApplication();
        String sessionID = application.getSessionID();

        textView.setText("未登录，请登录");

        final ImageView imageView_lock = (ImageView)root.findViewById(R.id.imageView_lock);
        final RecyclerView recyclerView_message = root.findViewById(R.id.recyclerView_message);
        final EditText editText = root.findViewById(R.id.editText_message_byname);
        final ConstraintLayout constraintLayout = root.findViewById(R.id.messageLayout);

        s_message = new ArrayList<>();
        mFilterList = new ArrayList<>();


        if(sessionID.equals("")) {
            textView.setVisibility(View.VISIBLE);
            imageView_lock.setVisibility(View.VISIBLE);
            recyclerView_message.setVisibility(View.INVISIBLE);
            constraintLayout.setVisibility(View.INVISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            imageView_lock.setVisibility(View.INVISIBLE);
            recyclerView_message.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
        }

        //todo arrayList赋值
        s_message.add(new MessageBrief("test1","test1","test1","1"));
        s_message.add(new MessageBrief("test2","test2","test2","2"));
        s_message.add(new MessageBrief("test12","test12","test12","3"));
        s_message.add(new MessageBrief("test11","test11","test11","4"));

        mFilterList = s_message;

        recyclerView_message.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();

        recyclerView_message.setAdapter(myAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                myAdapter.getFilter().filter(sequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return root;


    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> implements Filterable {

        private Context mcontent;


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyAdapter.MyHolder myHolder = new MyAdapter.MyHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_chat_brief, parent, false));


            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {

            final MyAdapter.MyHolder viewHolder = (MyAdapter.MyHolder) holder;

            //将数据映射到控件中
            viewHolder.text.setText(mFilterList.get(position).text);
            viewHolder.name.setText(mFilterList.get(position).name);
            viewHolder.time.setText(mFilterList.get(position).time);
            //用户id
            viewHolder.id = mFilterList.get(position).id;

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo 设置聊天跳转
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    //intent.putExtra("id",viewHolder.id);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mFilterList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                //执行过滤操作
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        //没有过滤的内容，则使用源数据
                        mFilterList = s_message;
                    } else {
                        List<MessageBrief> filteredList = new ArrayList<>();
                        for (MessageBrief str : s_message) {
                            //这里根据需求，添加匹配规则
                            if (str.name.contains(charString)) {
                                filteredList.add(str);
                            }
                        }

                        mFilterList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilterList;
                    return filterResults;
                }

                //把过滤后的值返回出来
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilterList = (ArrayList<MessageBrief>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView text, name, time;
            //标识号
            String id;

            MyHolder(View itemView) {
                super(itemView);

                //实例化子对象，把对象和列表项布局文件中的id绑定
                text = itemView.findViewById(R.id.textView_item_chatbrief_text);
                name = itemView.findViewById(R.id.textView_item_chatbrief_name);
                time = itemView.findViewById(R.id.textView_item_chatbrief_time);

            }

        }
    }

    //辅助类储存消息信息
    class MessageBrief{
        public String name;
        public String text;
        public String time;
        String id;

        //构造函数
        MessageBrief(String m_name, String m_text, String m_time,String m_id)
        {
            name = m_name;
            text = m_text;
            time = m_time;
            id = m_id;
        }
    }



}
