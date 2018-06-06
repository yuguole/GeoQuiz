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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.ReplyAdapter;
import com.bignerdranch.android.geoquiz.Add.addReplyActivity;
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Fragment.Homepage;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.Models.ReplyBean;
import com.bignerdranch.android.geoquiz.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class theask_detailsActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private TextView asktitle;
    private TextView askuser;
    private TextView asktime;
    private TextView askdetail;
    private TextView asklabel;
    //private Button button_addreply;

    private RecyclerView reply_Recycler;
    private SwipeRefreshLayout reply_SwipeRefreshLayout;
    private ReplyAdapter reply_Adapter;
    private List<ReplyBean> reply_Datas;
    private Toolbar theask_toolbar;
    private SearchView reply_SearchView;
    FullyLinearLayoutManager reply_LayoutManager = new FullyLinearLayoutManager(this);

    //reply的url
    private static final String urlreply = "http://yuguole.pythonanywhere.com/Iknow/theask_reply";

    //theask的url
    private static final String urlask = "http://yuguole.pythonanywhere.com/Iknow/theask";

    public static String askIDStr;

    public static String replyIDStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theask_details);

        initView();

    }

    private void initView() {
        asktitle=(TextView)findViewById(R.id.ask_home_title);
        askuser=(TextView)findViewById(R.id.ask_home_askname);
        asktime=(TextView)findViewById(R.id.ask_home_asktime);
        askdetail=(TextView)findViewById(R.id.ask_home_ask_detail);
        asklabel=(TextView)findViewById(R.id.theask_label);

        initText();

        theask_toolbar = (Toolbar) findViewById(R.id.toolbar_theask_detail);
        //theask_toolbar.setTitle("提出新问题");//标题

        theask_toolbar.inflateMenu(R.menu.menu_theask_detail);
        theask_toolbar.setOnMenuItemClickListener(this);
        theask_toolbar.setNavigationIcon(R.drawable.ic_back2);
        theask_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //button_addreply =(Button)findViewById(R.id.add_reply);
        //initaddreply();

        reply_Recycler = (RecyclerView) findViewById(R.id.recyclerview_reply);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        reply_Recycler.setHasFixedSize(true);
        initReplylist();

        initListener();//刷新

        initClickitem();//点击事件
    }

    private void initClickitem() {
        reply_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(theask_detailsActivity.this, reply_Recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //Toast.makeText(theask_detailsActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView re_id=(TextView)view.findViewById(R.id.item_reply_id);
                        replyIDStr=re_id.getText().toString();
                        Toast.makeText(theask_detailsActivity.this, re_id.getText(), Toast.LENGTH_SHORT).show();
                        start_thereplyActivity();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toast.makeText(theask_detailsActivity.this, "长按", Toast.LENGTH_SHORT).show();

                    }
                })
        );
    }

    private void start_thereplyActivity() {
        Intent intent=new Intent(theask_detailsActivity.this,thereply_detailsActivity.class);
        startActivity(intent);
    }


    private void initListener() {
        //下拉刷新
        reply_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_reply);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        reply_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();
    }

    private void initPullRefresh() {
        reply_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initReplylist();
                        //刷新完成
                        reply_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(theask_detailsActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void initReplylist() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlreply, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("asktitle", Homepage.asktitleStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlreply, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null && response.length() > 0) {

                            int status=response.optInt("status");
                            if(status==200)
                            {
                                JSONArray reply = response.optJSONArray("the_reply");

                                //String dataString = ask.toString();
                                Log.d(TAG, String.valueOf(response));

                                reply_Datas = new ArrayList<ReplyBean>();
                                for (int i = 0; i < reply.length(); i++) {
                                    JSONObject jsonData = reply.optJSONObject(i);
                                    //JSONObject jsonData1 = jsonData.optJSONObject("fields");

                                    ReplyBean data = new ReplyBean();
                                    //String reuser=QuizActivity.UseridToUsername(userid);
                                    //UseridToName(id);
                                    data.setRe_id(jsonData.optInt("reid"));
                                    data.setRe_user(jsonData.optString("replyuser"));
                                    data.setRe_details(jsonData.optString("redetails"));
                                    data.setRe_time(jsonData.optString("retime"));

                                    reply_Datas.add(data);
                                    //test.setText(ask.toString());
                                    //Log.i(TAG,e();getMessage(),volleyError);
                                }

                                reply_Adapter = new ReplyAdapter(theask_detailsActivity.this, reply_Datas);
                                reply_Recycler.setAdapter(reply_Adapter);

                                reply_Recycler.setLayoutManager(reply_LayoutManager);
                                //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                //使用系统默认分割线
                                reply_Recycler.addItemDecoration(new DividerItemDecoration(theask_detailsActivity.this, DividerItemDecoration.VERTICAL));
                                //mAdapter.notifyDataSetChanged();

                            }

                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(theask_detailsActivity.this, "加载成功", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(theask_detailsActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    private void initText() {
        //(image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("asktitle", Homepage.asktitleStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlask,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int id=response.optInt("id");
                            askIDStr=""+id;
                            asktitle.setText(response.optString("ask_title"));
                            askdetail.setText(response.optString("ask_details"));
                            asktime.setText(response.optString("ask_time"));
                            asktitle.setText(response.optString("ask_title"));
                            JSONObject ask_user = response.optJSONObject("ask_user");
                            askuser.setText(ask_user.optString("username"));

                            JSONArray ask_label=response.optJSONArray("ask_label");
                            String theasklabel="";
                            for (int i=0;i<ask_label.length();i++){
                                JSONObject jsonData = ask_label.optJSONObject(i);
                                theasklabel=theasklabel+" * "+jsonData.optString("lb_title");
                            }
                            asklabel.setText(theasklabel);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_theask_addreply:
                Intent i=new Intent(theask_detailsActivity.this,addReplyActivity.class);
                startActivity(i);
                break;

            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

