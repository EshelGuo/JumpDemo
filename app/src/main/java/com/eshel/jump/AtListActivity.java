package com.eshel.jump;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.enums.IntentType;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class AtListActivity extends AppCompatActivity {

    public static final String AT_CALLBACK = "AtCallback";
    private RecyclerView rv;
    AtListActivity.AtCallback callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv = new RecyclerView(this);
        setContentView(rv);
        JumpHelper.parseIntent(this, getIntent());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter<AtViewHolder>() {
            String[] names = {"张三", "李四", "王五", "赵六", "小七"};
            int[] userId = {3, 4, 5, 6, 7};

            @NonNull
            @Override
            public AtViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new AtViewHolder(viewGroup.getContext());
            }

            @Override
            public void onBindViewHolder(@NonNull AtViewHolder viewHolder, final int i) {
                viewHolder.name.setText(names[i]);
                viewHolder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callback != null)
                            callback.onChooseResult(userId[i], names[i]);
                        finish();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return names.length;
            }
        });
    }

    @IntentParser(intentType = IntentType.MemoryIntent)
    public void parseIntent(@Params(AtListActivity.AT_CALLBACK) AtListActivity.AtCallback callback){
        this.callback = callback;
    }

    public static class AtViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public AtViewHolder(Context context) {
            super(View.inflate(context, android.R.layout.simple_list_item_1, null));
            name = (TextView) itemView;
        }
    }

    public interface AtCallback{
        void onChooseResult(int userId, String name);
    }
}
