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

public class InformAdapter extends RecyclerView.Adapter<InformViewHolder>  {
    private LayoutInflater mInflater;
    private Context mcontext;
    private List<ReplyBean> mdatas;



    public InformAdapter(Context context, List<ReplyBean> mDatas) {
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
    public InformViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view=mInflater.inflate(R.layout.item,parent,false);
        DataViewHolder dataViewHolder=new DataViewHolder(view,mClickListener);
        return dataViewHolder;*/
        return new InformViewHolder(mInflater.inflate(R.layout.item_inform,parent,false));
        // return new DataViewHolder(mInflater.inflate(R.layout.item_allask,parent,false));//mInflater.inflate(R.layout.item_allask, parent, false));
    }

    @Override
    public void onBindViewHolder(InformViewHolder holder, int position) {
        holder.reinfoView.setText(mdatas.get(position).getRe_user()+" 回答了你提出的问题 "+mdatas.get(position).getRe_ask());
        holder.reinfoidView.setText(mdatas.get(position).getRe_id()+"");
        holder.reinfotimeView.setText(mdatas.get(position).getRe_time());
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
class InformViewHolder extends RecyclerView.ViewHolder {
    TextView reinfoView;
    TextView reinfotimeView;

    TextView reinfoidView;

    public InformViewHolder(View itemView) {
        super(itemView);
        //完成Item View 和ViewHolder里属性的绑定，begin
        reinfoView = (TextView) itemView.findViewById(R.id.item_inform_info);
        reinfotimeView = (TextView) itemView.findViewById(R.id.item_infrom_time);

        reinfoidView=(TextView) itemView.findViewById(R.id.item_inform_reid);
    }

}
