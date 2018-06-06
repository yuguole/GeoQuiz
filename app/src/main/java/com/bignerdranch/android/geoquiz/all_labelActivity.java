package com.bignerdranch.android.geoquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.bignerdranch.android.geoquiz.Fragment.FullyLinearLayoutManager;
import com.bignerdranch.android.geoquiz.Fragment.RecyclerItemClickListener;
import com.bignerdranch.android.geoquiz.Models.LabelBean;
import com.bignerdranch.android.geoquiz.Persondetails.myAskActivity;
import com.bignerdranch.android.geoquiz.The_details.thelabel_askActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class all_labelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private RecyclerView all_label_Recycler;
    private SwipeRefreshLayout all_label_SwipeRefreshLayout;
    private LabelAdapter all_label_Adapter;
    private List<LabelBean> all_label_Datas;
    private Toolbar all_label_toolbar;
    private SearchView all_label_SearchView;
    FullyLinearLayoutManager all_label_LayoutManager = new FullyLinearLayoutManager(this);

    private List<LabelBean> findList;
    private LabelAdapter findListAdapter;

    public static String The_LabelStr;
    private static final String showurl = "http://yuguole.pythonanywhere.com/Iknow/showlabel";
    private static final String adduserlabelurl = "http://yuguole.pythonanywhere.com/Iknow/adduser_label";
    private static final String addnewlabelurl = "http://yuguole.pythonanywhere.com/Iknow/addlabel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_label);

        initView();
    }

    private void initView() {
        all_label_Recycler = (RecyclerView) findViewById(R.id.recyclerview_addmylabel);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        all_label_Recycler.setHasFixedSize(true);
        all_label_toolbar = (Toolbar) findViewById(R.id.toolbar_addmylabel);
        all_label_toolbar.setTitle("标签列表");//标题
        all_label_toolbar.inflateMenu(R.menu.menu_labelshow);
        all_label_toolbar.setOnMenuItemClickListener(this);
        all_label_toolbar.setNavigationIcon(R.drawable.ic_back2);
        all_label_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        MenuItem searchItem=  all_label_toolbar.getMenu().findItem(R.id.all_label_search);//findViewById(R.id.homepage_search);
        all_label_SearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        initSearchView(searchItem);//搜索

        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        //myask_toolbar.setNavigationIcon(R.mipmap.ic_search);
        //myask_toolbar.inflateMenu(R.menu.menu_homepage);
        //myask_toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) this);
        init();
        initListener();//刷新
        initClickitem();//点击事件
    }

    private void initSearchView(MenuItem searchItem) {
        findList=new ArrayList<LabelBean>();
        all_label_SearchView.setQueryHint("你想知道标签");

        //Toast.makeText(getActivity(), mSearchView.toString(), Toast.LENGTH_SHORT).show();
        all_label_SearchView.setSubmitButtonEnabled(true);

        all_label_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query)){
                    Toast.makeText(all_labelActivity.this,"请输入查找内容！",Toast.LENGTH_SHORT).show();
                    all_label_Recycler.setAdapter(all_label_Adapter);
                }
                else {
                    findList.clear();
                    for (int i=0;i<all_label_Datas.size();i++){
                        LabelBean information=all_label_Datas.get(i);
                        if (information.getLb_title().contains(query))
                        {
                            findList.add(information);
                        }
                    }
                    if (findList.size()==0){
                        Toast.makeText(all_labelActivity.this,"未搜索到结果",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Log.i("NewActivity", "###length="+findList.size());
                        Toast.makeText(all_labelActivity.this, "查找成功", Toast.LENGTH_SHORT).show();
                        findListAdapter=new LabelAdapter(all_labelActivity.this,findList);
                        //findListAdapter.set
                        all_label_Recycler.setAdapter(findListAdapter);
                        findListAdapter.notifyDataSetChanged();
                    }
                }

                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    all_label_Recycler.setAdapter(all_label_Adapter);
                }
                else {
                    findList.clear();
                    for (int i=0;i<all_label_Datas.size();i++){
                        LabelBean information=all_label_Datas.get(i);
                        if (information.getLb_title().contains(newText))
                        {
                            findList.add(information);
                        }
                    }
                    findListAdapter=new LabelAdapter(all_labelActivity.this,findList);
                    findListAdapter.notifyDataSetChanged();
                    all_label_Recycler.setAdapter(findListAdapter);
                    //findListAdapter.set
                }
                return false;
            }
        });

        all_label_Recycler.setLayoutManager(all_label_LayoutManager);
        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //使用系统默认分割线
        all_label_Recycler.addItemDecoration(new DividerItemDecoration(all_labelActivity.this,DividerItemDecoration.VERTICAL));
        //mAdapter.notifyDataSetChanged();

        //mAdapter=new DataAdapter(getActivity(),)
    }

    private void initClickitem() {

        //点击跳转
        all_label_Recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(all_labelActivity.this, all_label_Recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(all_labelActivity.this, "点击", Toast.LENGTH_SHORT).show();
                        // do whatever
                        final TextView lbtitle = (TextView) view.findViewById(R.id.item_label_title);
                        The_LabelStr = lbtitle.getText().toString();
                        //Toast.makeText(all_labelActivity.this, The_LabelStr, Toast.LENGTH_SHORT).show();
                        startLabel_askActivity();

                    }

                    @Override
                    public void onLongItemClick(final View view, int position) {
                        //Toast.makeText(all_labelActivity.this, "长按", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(all_labelActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("是否关注该标签？");
                        builder.setCancelable(false);
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final TextView lbtitle1 = (TextView) view.findViewById(R.id.item_label_title);
                                String likelabel = lbtitle1.getText().toString();
                                addlikelabel(likelabel);
                            }
                        });
                        builder.setNegativeButton("否", null);
                        builder.create().show();
                    }

                })
        );

    }

    private void addlikelabel(final String likelabel) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
       // Toast.makeText(this, showurl, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);
        map.put("userlabel",likelabel);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, adduserlabelurl, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            int status = response.optInt("status");
                            if (status == 200) {
                                Toast.makeText(all_labelActivity.this, "关注"+likelabel+"成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(all_labelActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(all_labelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    private void startLabel_askActivity() {
        Intent intent=new Intent(all_labelActivity.this, thelabel_askActivity.class);
        startActivity(intent);
    }

    private void initListener() {
        //下拉刷新
        all_label_SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout_addmylabel);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        all_label_SwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"), Color.parseColor("#ff0f0f"), Color.parseColor("#0000ff"), Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();
    }

    private void initPullRefresh() {
        all_label_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        all_label_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(all_labelActivity.this, "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    private void init() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, showurl, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        //map.put("username", login1Activity.usernameStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, showurl, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            JSONArray label = response.optJSONArray("label");

                            //String dataString = ask.toString();
                            Log.d(TAG, String.valueOf(response));

                            all_label_Datas = new ArrayList<LabelBean>();
                            for (int i = 0; i < label.length(); i++) {
                                JSONObject jsonData = label.optJSONObject(i);

                                LabelBean data = new LabelBean();
                                data.setLb_title(jsonData.optString("labeltitle"));

                                all_label_Datas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(all_labelActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        all_label_Adapter = new LabelAdapter(all_labelActivity.this, all_label_Datas);
                        all_label_Recycler.setAdapter(all_label_Adapter);

                        all_label_Recycler.setLayoutManager(all_label_LayoutManager);
                        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        all_label_Recycler.addItemDecoration(new DividerItemDecoration(all_labelActivity.this, DividerItemDecoration.VERTICAL));
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(all_labelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

    }

    private void addnewlabel(final String label){
        RequestQueue queue = Volley.newRequestQueue(this);
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(this, showurl, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("lb_title", label);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, addnewlabelurl, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            int status = response.optInt("status");
                            if (status == 200) {
                                Toast.makeText(all_labelActivity.this, "添加"+label+"成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(all_labelActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(all_labelActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, volleyError.getMessage(), volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.labelshow_add_label:
                final EditText Inputlabel = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(all_labelActivity.this);
                builder.setTitle("添加新标签");
                builder.setMessage("请输入新标签：");
                builder.setView(Inputlabel);
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputlabel=Inputlabel.getText().toString().trim();
                        addnewlabel(inputlabel);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                init();
                break;

        }
        return true;
    }
}
