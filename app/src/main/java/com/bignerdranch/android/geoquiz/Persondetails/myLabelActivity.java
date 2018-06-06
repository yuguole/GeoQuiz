package com.bignerdranch.android.geoquiz.Persondetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.LabelAdapter;
import com.bignerdranch.android.geoquiz.Add.add_asklabelActivity;
import com.bignerdranch.android.geoquiz.all_labelActivity;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.The_details.thelabel_askActivity;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Models.LabelBean;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class myLabelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private RecyclerView mylabel_Recycler;
    private SwipeRefreshLayout mylabel_SwipeRefreshLayout;
    private LabelAdapter mylabel_Adapter;
    private List<LabelBean> mylabel_Datas;
    private Toolbar mylabel_toolbar;
    private SearchView mylabel_SearchView;
    private TextView mylabel_text;

    FullyLinearLayoutManager mylabel_LayoutManager = new FullyLinearLayoutManager(this);

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/mylabel";
    private static final String deluserlabelurl = "http://yuguole.pythonanywhere.com/Iknow/delete_userlabel";


    //public static String TheLabelStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_label);

        initView();
    }

    //初始化
    private void initView() {
        mylabel_Recycler = (RecyclerView) findViewById(R.id.recyclerview_mylabel);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        mylabel_Recycler.setHasFixedSize(true);
        mylabel_text=(TextView)findViewById(R.id.mylabel_text);

        mylabel_toolbar = (Toolbar) findViewById(R.id.toolbar_mylabel);
        mylabel_toolbar.setTitle("我关注的标签");//标题
        mylabel_toolbar.inflateMenu(R.menu.menu_mylabel);
        mylabel_toolbar.setOnMenuItemClickListener(this);
        mylabel_toolbar.setNavigationIcon(R.drawable.ic_back2);
        mylabel_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
        initListener();//刷新
        initClickitem();//点击事件
    }

    //点击item跳转
    private void initClickitem() {
        //点击跳转
        mylabel_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(myLabelActivity.this, mylabel_Recycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //Toast.makeText(myLabelActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView lbtitle=(TextView)view.findViewById(R.id.item_label_title);
                        all_labelActivity.The_LabelStr=lbtitle.getText().toString();
                        //Toast.makeText(myLabelActivity.this, all_labelActivity.The_LabelStr, Toast.LENGTH_SHORT).show();
                        startLabel_askActivity();
                    }

                    @Override public void onLongItemClick(final View view, int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(myLabelActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("确定取消关注该标签？");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // int ret = noteDao.deleteNote(note.getId());
                                //deleteNote(note.getId());
                                //refreshNoteList();
                                final TextView deltitle=(TextView)view.findViewById(R.id.item_label_title);
                                String del=deltitle.getText().toString();
                                dellikelabel(del);
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();
                    }
                })
        );
    }

    private void dellikelabel(String deltitle) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);
        map.put("dellabel",deltitle);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, deluserlabelurl, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            int status=response.optInt("status");
                            if (status==200){
                                Toast.makeText(myLabelActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                init();
                            }

                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(myLabelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    //点击标签跳转到该标签下所有问题页面
    private void startLabel_askActivity() {

        Intent intent=new Intent(myLabelActivity.this, thelabel_askActivity.class);
        startActivity(intent);
    }

    //显示我关注的标签
    private void init() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            JSONObject content=response.optJSONObject("content");

                                JSONArray label = content.optJSONArray("user_label");

                                //String dataString = ask.toString();
                                Log.d(TAG, String.valueOf(response));

                                mylabel_Datas = new ArrayList<LabelBean>();
                                if (label.length()!=0){
                                    for (int i = 0; i < label.length(); i++) {
                                        JSONObject jsonData = label.optJSONObject(i);

                                        LabelBean data = new LabelBean();
                                        data.setLb_title(jsonData.optString("lb_title"));


                                        mylabel_Datas.add(data);
                                        //test.setText(ask.toString());
                                        //Log.i(TAG,e();getMessage(),volleyError);
                                    }

                                    Toast.makeText(myLabelActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                                    mylabel_Adapter = new LabelAdapter(myLabelActivity.this,mylabel_Datas);
                                    mylabel_Recycler.setAdapter(mylabel_Adapter);

                                    mylabel_Recycler.setLayoutManager(mylabel_LayoutManager);
                                    //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                    //使用系统默认分割线
                                    mylabel_Recycler.addItemDecoration(new DividerItemDecoration(myLabelActivity.this, DividerItemDecoration.VERTICAL));
                                    //mAdapter.notifyDataSetChanged();
                                }else {
                                    mylabel_text.setText("暂没有关注标签，请点击右上角关注标签");
                                }



                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(myLabelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

    }

    private void initListener() {
        //下拉刷新
        mylabel_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_mylabel);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        mylabel_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    private void initPullRefresh() {
        mylabel_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        mylabel_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(myLabelActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 1000);

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mylabel_add_likelabel:
                Toast.makeText(myLabelActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                Intent i = new Intent(myLabelActivity.this, all_labelActivity.class);
                startActivity(i);
                break;

        }
        return false;
    }
}
