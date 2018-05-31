package com.bignerdranch.android.geoquiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.geoquiz.Models.ReplyBean;
import com.bignerdranch.android.geoquiz.R;

import java.util.List;

public class myReplyAdapter extends RecyclerView.Adapter<myReplyViewHolder>  {
    private LayoutInflater mInflater;
    private Context mcontext;
    private List<ReplyBean> mdatas;



    public myReplyAdapter(Context context, List<ReplyBean> mDatas) {
        this.mdatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
        this.mcontext=context;

    }
    /**
     * @param parent   其实就是RecyclerView
     * @param viewType 据我看到，模仿ListView底部加载更多会用到。
     * @return 创建的新的ViewHolder对象
     */
    @Override
    public myReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view=mInflater.inflate(R.layout.item,parent,false);
        DataViewHolder dataViewHolder=new DataViewHolder(view,mClickListener);
        return dataViewHolder;*/
        return new myReplyViewHolder(mInflater.inflate(R.layout.item_myreply,parent,false));
        // return new DataViewHolder(mInflater.inflate(R.layout.item_allask,parent,false));//mInflater.inflate(R.layout.item_allask, parent, false));
    }

    @Override
    public void onBindViewHolder(myReplyViewHolder holder, int position) {
        holder.myreaskView.setText(mdatas.get(position).getRe_ask());
        holder.myredetailView.setText(mdatas.get(position).getRe_details());
        holder.myretimeView.setText(mdatas.get(position).getRe_time());
        holder.myreidView.setText(mdatas.get(position).getRe_id()+"");
    }



    /**
     * @return item的个数，最好加个为空判断。
     */
    @Override
    public int getItemCount() {
        return mdatas.size();
        // return mdatas != null ? mdatas.size() : 0;
    }
}
/**
 * ViewHolder的创建
 *
 */
class myReplyViewHolder extends RecyclerView.ViewHolder {
    TextView myreaskView;
    TextView myretimeView;
    TextView myredetailView;
    TextView myreidView;

    public myReplyViewHolder(View itemView) {
        super(itemView);
        //完成Item View 和ViewHolder里属性的绑定，begin
        myreaskView = (TextView) itemView.findViewById(R.id.item_myreply_ask);
        myredetailView = (TextView) itemView.findViewById(R.id.item_myreply_detail);
        myretimeView = (TextView) itemView.findViewById(R.id.item_myreply_time);
        myreidView = (TextView) itemView.findViewById(R.id.item_myreply_reid);
    }

}
