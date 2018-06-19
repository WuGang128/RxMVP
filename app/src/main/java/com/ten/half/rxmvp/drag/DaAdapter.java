package com.ten.half.rxmvp.drag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ten.half.rxmvp.R;

import java.util.List;

/**
 * Created by wugang on 2017/12/24.
 */

public class DaAdapter extends RecyclerView.Adapter<DaAdapter.MyViewHolde> {
    private Context mContext;
    List<DaBean> mList;
    private SwipeMenuView old_view;  //打开删除状态的view
    DaItemTouchHelper daItemTouchHelper;
    private boolean isCanDrag = true; //是否可以被拖拽

    public DaAdapter(Context context, List<DaBean> list, DaItemTouchHelper daItemTouchHelper) {
        this.mContext = context;
        this.mList = list;
        this.daItemTouchHelper = daItemTouchHelper;
    }

    @Override
    public DaAdapter.MyViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolde holder = new MyViewHolde(LayoutInflater.from(
                mContext).inflate(R.layout.item_view, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolde holder, final int position) {
        holder.tvLocation.setText("条目" + mList.get(position).getLocation());
        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_view == null) {
                    holder.swipeMenuView.smoothExpand();
                    old_view = holder.swipeMenuView;
                    isCanDrag = false;
                } else {
                    if (old_view == holder.swipeMenuView) {
                        if (holder.swipeMenuView.choose()) {
                            old_view = holder.swipeMenuView;
                            isCanDrag = false;
                        } else {
                            isCanDrag = true;
                            old_view = null;
                        }
                    } else {
                        old_view.smoothClose();
                        holder.swipeMenuView.smoothExpand();
                        old_view = holder.swipeMenuView;
                        isCanDrag = false;
                    }
                }
            }
        });
        holder.ll_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daCallBack.del(position);
                isCanDrag = true;
                old_view = null;

            }
        });
        holder.rl_root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanDrag) {
                    Toast.makeText(mContext, "点击条目---->" + position + "", Toast.LENGTH_SHORT).show();
                } else {
                    holder.swipeMenuView.smoothClose();
                    if (old_view != null) {
                        old_view.smoothClose();
                        isCanDrag = true;
                        old_view = null;
                    }
                }
            }
        });
        holder.iv_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && isCanDrag) {
                    daItemTouchHelper.startDrag(holder);
                    return true;
                }
                return false;
            }
        });
        holder.iv_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeMenuView.smoothClose();
                if (old_view != null) {
                    old_view.smoothClose();
                    isCanDrag = true;
                    old_view = null;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolde extends RecyclerView.ViewHolder {
        TextView tvLocation;
        LinearLayout ll_del;
        SwipeMenuView swipeMenuView;
        ImageView iv_del, iv_move;
        RelativeLayout rl_move, rl_root_view;

        public MyViewHolde(View itemView) {
            super(itemView);
            iv_del = (ImageView) itemView.findViewById(R.id.iv_del);
            iv_move = (ImageView) itemView.findViewById(R.id.iv_move);

            rl_root_view = (RelativeLayout) itemView.findViewById(R.id.rl_root_view);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_loca);
            ll_del = (LinearLayout) itemView.findViewById(R.id.ll_del);
            swipeMenuView = (SwipeMenuView) itemView.findViewById(R.id.swipeMenuView);

        }
    }

    private DaCallBack daCallBack;

    public void setDaCallBack(DaCallBack daCallBack) {
        this.daCallBack = daCallBack;
    }

    interface DaCallBack {
        void del(int postion);

    }
}
