package com.bignerdranch.android.geoquiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.geoquiz.Models.LabelBean;
import com.bignerdranch.android.geoquiz.R;

import java.util.List;

public class LabelnowAdapter extends RecyclerView.Adapter<LabelnowViewHolder>{
private LayoutInflater mInflater;
private Context mcontext;
private List<LabelBean> mdatas;



public LabelnowAdapter(Context context, List<LabelBean> mDatas) {
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
public LabelnowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view=mInflater.inflate(R.layout.item,parent,false);
        DataViewHolder dataViewHolder=new DataViewHolder(view,mClickListener);
        return dataViewHolder;*/
        return new LabelnowViewHolder(mInflater.inflate(R.layout.item_addnowlabel,parent,false));
        // return new DataViewHolder(mInflater.inflate(R.layout.item_allask,parent,false));//mInflater.inflate(R.layout.item_allask, parent, false));
        }

        @Override
        public void onBindViewHolder(LabelnowViewHolder holder, int position) {
                holder.lbtitleView.setText(mdatas.get(position).getLb_title());
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

class LabelnowViewHolder extends RecyclerView.ViewHolder  {
        TextView lbtitleView;

        public LabelnowViewHolder(View itemView) {
                super(itemView);
                //完成Item View 和ViewHolder里属性的绑定，begin
                lbtitleView = (TextView) itemView.findViewById(R.id.item_nowlabel_title);

        }


}
/**
 * ViewHolder的创建implements OnCreateContextMenuListener
 *
 */


