package com.bignerdranch.android.geoquiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.geoquiz.Models.LabelBean;
import com.bignerdranch.android.geoquiz.Models.ReplyBean;
import com.bignerdranch.android.geoquiz.R;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyViewHolder>  {
    private LayoutInflater mInflater;
    private Context mcontext;
    private List<ReplyBean> mdatas;



    public ReplyAdapter(Context context, List<ReplyBean> mDatas) {
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
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view=mInflater.inflate(R.layout.item,parent,false);
        DataViewHolder dataViewHolder=new DataViewHolder(view,mClickListener);
        return dataViewHolder;*/
        return new ReplyViewHolder(mInflater.inflate(R.layout.item_reply,parent,false));
        // return new DataViewHolder(mInflater.inflate(R.layout.item_allask,parent,false));//mInflater.inflate(R.layout.item_allask, parent, false));
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, int position) {
        holder.reuserView.setText(mdatas.get(position).getRe_user());
        holder.redetailView.setText(mdatas.get(position).getRe_details());
        holder.retimeView.setText(mdatas.get(position).getRe_time());
        holder.reidView.setText(mdatas.get(position).getRe_id()+"");
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
class ReplyViewHolder extends RecyclerView.ViewHolder {
    TextView reuserView;
    TextView retimeView;
    TextView redetailView;
    TextView reidView;

    public ReplyViewHolder(View itemView) {
        super(itemView);
        //完成Item View 和ViewHolder里属性的绑定，begin
        reuserView = (TextView) itemView.findViewById(R.id.item_reply_user);
        retimeView = (TextView) itemView.findViewById(R.id.item_reply_time);
        redetailView = (TextView) itemView.findViewById(R.id.item_reply_detail);
        reidView=(TextView) itemView.findViewById(R.id.item_reply_id);
    }

}


