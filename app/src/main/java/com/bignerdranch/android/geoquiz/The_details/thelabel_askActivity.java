package com.bignerdranch.android.geoquiz.The_details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.DataAdapter;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Fragment.Homepage;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.Models.AskBean;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.all_labelActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class thelabel_askActivity extends AppCompatActivity {
    private RecyclerView labelask_Recycler;
    private SwipeRefreshLayout labelask_SwipeRefreshLayout;
    private DataAdapter labelask_Adapter;
    private List<AskBean> labelask_Datas;
    private Toolbar labelask_toolbar;
    private SearchView labelask_SearchView;
    private TextView label_textview;
    FullyLinearLayoutManager labelask_LayoutManager = new FullyLinearLayoutManager(this);

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/label_ask";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thelabel_ask);

        initView();
    }

    private void initView() {

        label_textview=(TextView)findViewById(R.id.thelabel_ask) ;
        labelask_Recycler = (RecyclerView) findViewById(R.id.recyclerview_labelask);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        labelask_Recycler.setHasFixedSize(true);
        labelask_toolbar = (Toolbar) findViewById(R.id.toolbar_labelask);
        labelask_toolbar.setTitle(all_labelActivity.The_LabelStr);//标题
        //setSupportActionBar(myask_toolbar);

        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //myask_toolbar.setNavigationIcon(R.mipmap.ic_search);
        labelask_toolbar.inflateMenu(R.menu.menu_labelask);
        labelask_toolbar.setNavigationIcon(R.drawable.ic_back2);
        labelask_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
        initListener();//刷新

        //点击跳转
        initClickitem();//点击事件

    }

    //点击跳转事件
    private void initClickitem() {
        labelask_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(thelabel_askActivity.this, labelask_Recycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(thelabel_askActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView title=(TextView)view.findViewById(R.id.item_title);
                        Homepage.asktitleStr=title.getText().toString();
                        Toast.makeText(thelabel_askActivity.this, title.getText(), Toast.LENGTH_SHORT).show();
                        startAsk_homeActivity();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toast.makeText(thelabel_askActivity.this, "长按", Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

    }

    private void startAsk_homeActivity() {
        Intent intent=new Intent(thelabel_askActivity.this,theask_detailsActivity.class);
        startActivity(intent);
    }

    private void init() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("label", all_labelActivity.The_LabelStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int status=response.optInt("status");
                            if(status==200)
                            {
                                JSONArray ask = response.optJSONArray("label_ask");

                                //String dataString = ask.toString();
                                Log.d(TAG, String.valueOf(response));

                                labelask_Datas = new ArrayList<AskBean>();
                                for (int i = 0; i < ask.length(); i++) {
                                    JSONObject jsonData = ask.optJSONObject(i);
                                    //JSONObject jsonData1 = jsonData.optJSONObject("fields");

                                    AskBean data = new AskBean();
                                    data.setTitle(jsonData.optString("asktitle"));
                                    data.setDetails(jsonData.optString("askdetails"));
                                    data.setAsktime(jsonData.optString("asktime"));
                                    data.setAskuser(jsonData.optString("askuser"));

                                    labelask_Datas.add(data);
                                    //test.setText(ask.toString());
                                    //Log.i(TAG,e();getMessage(),volleyError);
                                }

                                labelask_Adapter = new DataAdapter(thelabel_askActivity.this, labelask_Datas);
                                labelask_Recycler.setAdapter(labelask_Adapter);

                                labelask_Recycler.setLayoutManager(labelask_LayoutManager);
                                //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                //使用系统默认分割线
                                labelask_Recycler.addItemDecoration(new DividerItemDecoration(thelabel_askActivity.this, DividerItemDecoration.VERTICAL));

                            }else if(status==300){
                                label_textview.setText("该标签下暂没有问题");
                                //Toast.makeText(thelabel_askActivity.this, "该标签下暂没有问题", Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(thelabel_askActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(thelabel_askActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

    }

    private void initListener() {
        //下拉刷新
        labelask_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_labelask);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        labelask_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    private void initPullRefresh() {
        labelask_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        labelask_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(thelabel_askActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

}
