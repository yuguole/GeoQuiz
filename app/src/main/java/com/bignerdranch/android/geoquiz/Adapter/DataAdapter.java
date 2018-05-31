package com.bignerdranch.android.geoquiz.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bignerdranch.android.geoquiz.Models.AskBean;
import com.bignerdranch.android.geoquiz.R;

import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView 的Adapter强制我们使用ViewHolder模式，对于我这种熟读徐老师《文艺Adapter》的青年来说，也是很轻松就接受了，看来Google也是强制我们每个程序员都成为一个文艺的青年。
 * 这里回想一下以前ListView的BaseAdapter，在getView()里还要判断convertView是否为空来判断是否是复用的，通过setTag，getTag放入ViewHolder对象。
 * 现在不用了，RecyclerView.Adapter<DataViewHolder>，将getView方法分解成两个过程，
 * 一个是public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType)，它专注于ViewHolder的创建。
 * 另一个是 public void onBindViewHolder(final DataViewHolder holder, int position)，它专注于ViewHolder的绑定，数据的显示，通过LOG查看，该方法时在每次显示item都被调用的(废话，但是我比较喜欢验证:))
 */
public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> implements Filterable {
    //private List<AskBean> mDatas;
    private LayoutInflater mInflater;
    private Context mcontext;
    //利用Volley 加载网络图片用到
    //private RequestQueue mQueue;
    private List<AskBean> mdatas;
    private List<AskBean> mFilteredList;



    public DataAdapter(Context context, List<AskBean> mDatas) {
        this.mdatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
        this.mcontext=context;
        //this.context=context;
        //Toast.makeText(getClass(),"<clink, clink>", Toast.LENGTH_SHORT).show();
        //this.mQueue = mQueue;

    }




    /**
     * @param parent   其实就是RecyclerView
     * @param viewType 据我看到，模仿ListView底部加载更多会用到。
     * @return 创建的新的ViewHolder对象
     */
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view=mInflater.inflate(R.layout.item_allask,parent,false);
        DataViewHolder dataViewHolder=new DataViewHolder(view,mClickListener);


        return dataViewHolder;*/
        return new DataViewHolder(mInflater.inflate(R.layout.item_allask,parent,false));
       // return new DataViewHolder(mInflater.inflate(R.layout.item_allask,parent,false));//mInflater.inflate(R.layout.item_allask, parent, false));
    }



    //绑定
    @Override
    public void onBindViewHolder(final DataViewHolder holder, final int position) {
        //holder.titleView.setText(mdatas.get(position));
        holder.titleView.setText(mdatas.get(position).getTitle());
        holder.detailView.setText(mdatas.get(position).getDetails());
        holder.askuserView.setText(mdatas.get(position).getAskuser());
        holder.timeView.setText(mdatas.get(position).getAsktime());
        //DateToStringUtils.StringToDate(mdatas.get(position).getAsktime(),)
        //holder.askItem.setOnClickListener(new View.OnClickListener() {


    }

    /**
     * @return item的个数，最好加个为空判断。
     */
    @Override
    public int getItemCount() {
        return mdatas.size();
       // return mdatas != null ? mdatas.size() : 0;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mdatas;
                } else {

                    ArrayList<AskBean> filteredList = new ArrayList<>();

                    for (AskBean androidVersion : mdatas) {

                        if (androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getDetails().toLowerCase().contains(charString) || androidVersion.getAskuser().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<AskBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}


/**
 * ViewHolder的创建
 *
 */
class DataViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView detailView;
    TextView timeView;
    TextView askuserView;

    //private AdapterView.OnItemClickListener mListener;// 声明自定义的接口


    //LinearLayout itemLinear;
    //ConstraintLayout askItem ;//点击事件

    public DataViewHolder(View itemView) {
        super(itemView);
        //完成Item View 和ViewHolder里属性的绑定，begin
        titleView = (TextView) itemView.findViewById(R.id.item_title);
        detailView = (TextView) itemView.findViewById(R.id.item_detail);
        timeView = (TextView) itemView.findViewById(R.id.item_datetime);
        askuserView = (TextView) itemView.findViewById(R.id.item_username);

        //mListener = listener;
        // 为ItemView添加点击事件


        //itemLinear = (LinearLayout) itemView.findViewById(R.id.askitem);
        //askItem= (ConstraintLayout) itemView.findViewById(R.id.askitem);


    }

}




