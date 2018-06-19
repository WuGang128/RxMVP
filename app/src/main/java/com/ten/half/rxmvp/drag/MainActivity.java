package com.ten.half.rxmvp.drag;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ten.half.rxmvp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private DaAdapter daAdapter;
    private List<DaBean> mList = new ArrayList<>();
    private DaItemTouchHelpCallback daItemTouchHelpCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerview);
        for (int i = 0; i < 20; i++) {
            DaBean daBean = new DaBean();
            daBean.setLocation(i);
            mList.add(daBean);
        }
        mRecyclerView.setLayoutManager(new DaLinearLayoutManager(this));
        daItemTouchHelpCallback = new DaItemTouchHelpCallback(new DaItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                mList.remove(adapterPosition);
                daAdapter.notifyItemRemoved(adapterPosition);
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                // 更换数据源中的数据Item的位置
                Collections.swap(mList, srcPosition, targetPosition);
                // 更新UI中的Item的位置，主要是给用户看到交互效果
                daAdapter.notifyItemMoved(srcPosition, targetPosition);
                return true;
                //return false;
            }

            @Override
            public void onEnd() {
                daAdapter.notifyItemRangeChanged(0, mList.size());
            }
        });
        final DaItemTouchHelper daItemTouchHelper = new DaItemTouchHelper(daItemTouchHelpCallback);

        daAdapter = new DaAdapter(this, mList,daItemTouchHelper);
        mRecyclerView.setAdapter(daAdapter);
        // 更换数据源中的数据Item的位置
        // 更新UI中的Item的位置，主要是给用户看到交互效果
        //return false;
        // daItemTouchHelpCallback.onMove()
        daItemTouchHelper.attachToRecyclerView(mRecyclerView);
        daAdapter.setDaCallBack(new DaAdapter.DaCallBack() {
            @Override
            public void del(int postion) {

                mList.remove(postion);//删除数据源
                daAdapter.notifyItemRemoved(postion);//刷新被删除的地方
                //  daAdapter.notifyDataSetChanged();
                if (postion < mList.size()) {
                    daAdapter.notifyItemRangeChanged(postion, mList.size());
                }
            }

        });

    }

    class DaLinearLayoutManager extends LinearLayoutManager {
        public DaLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}
