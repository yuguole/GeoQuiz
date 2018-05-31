package com.bignerdranch.android.geoquiz.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Adapter.DataAdapter;
import com.bignerdranch.android.geoquiz.Adapter.InformAdapter;
import com.bignerdranch.android.geoquiz.Models.AskBean;
import com.bignerdranch.android.geoquiz.Models.ReplyBean;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.The_details.theask_detailsActivity;
import com.bignerdranch.android.geoquiz.The_details.thereply_detailsActivity;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Informpage extends Fragment implements Toolbar.OnMenuItemClickListener {
    private RecyclerView informRecycler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private InformAdapter mAdapter;
    private InformAdapter findListAdapter;
    private List<ReplyBean> findList;
    private List<ReplyBean> mDatas;


    private Toolbar toolbar;
    private SearchView mSearchView;

    FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(getActivity());

    private static final String url="http://yuguole.pythonanywhere.com/Iknow/myask_reinform";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_informpage, container,false);
        //initView();

        informRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_inform);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        informRecycler.setHasFixedSize(true);
        //设置为垂直

        init();
        setHasOptionsMenu(true);
        return view;
    }

    //设置toolbar及刷新
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar_inform);
        toolbar.setTitle("通知");//标题
        // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_inform);
        //setHasOptionsMenu(true);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //setHasOptionsMenu(true);
        toolbar.setOnMenuItemClickListener(this);

        MenuItem searchItem=  toolbar.getMenu().findItem(R.id.inform_search);//findViewById(R.id.homepage_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        initSearchView(searchItem);//搜索


        initListener();//下拉刷新
        inititemClick();//点击跳转

    }

    //刷新初始化
    private void initListener() {
        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipelayout_inform);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"),Color.parseColor("#ff0f0f"),Color.parseColor("#0000ff"),Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

    //刷新
    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //刷新完成
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "更新了数据", Toast.LENGTH_SHORT).show();
                    }

                }, 3000);

            }
        });
    }

    //数据获取
    public void init() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //JSONArray ask=null;
//       (2)使用相应的请求需求
        //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response!=null&&response.length()>0){
                            JSONArray info = response.optJSONArray("myask_reinfo");

                            mDatas = new ArrayList<ReplyBean>();
                            for (int i = 0; i < info.length(); i++) {
                                JSONObject jsonData = info.optJSONObject(i);
                                //JSONObject jsonData1 = ask.optJSONObject("fields");
                                ReplyBean data = new ReplyBean();
                                data.setRe_id(jsonData.optInt("re_infoid"));
                                data.setRe_ask(jsonData.optString("re_infoask"));
                                data.setRe_user(jsonData.optString("re_infousr"));
                                data.setRe_time(jsonData.optString("re_infotime"));

                                mDatas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_SHORT).show();
                        mAdapter =  new InformAdapter(getActivity(),mDatas);
                        informRecycler.setAdapter(mAdapter);

                        informRecycler.setLayoutManager(mLayoutManager);
                        //informRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        informRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG,volleyError.getMessage(),volleyError);
            }
        });
        //(3)将请求需求加入到请求队列之中。
        queue.add(request);

        //App.getRequeatQueue().add(jsonrequest);
    }

    //搜索
    private void initSearchView(final MenuItem item) {

        //通过 item 获取 actionview

        findList=new ArrayList<ReplyBean>();
        mSearchView.setQueryHint("搜索...");

        //Toast.makeText(getActivity(), mSearchView.toString(), Toast.LENGTH_SHORT).show();
        mSearchView.setSubmitButtonEnabled(true);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (TextUtils.isEmpty(query)){
                    Toast.makeText(getActivity(),"请输入查找内容！",Toast.LENGTH_SHORT).show();
                    informRecycler.setAdapter(mAdapter);
                }
                else {
                    findList.clear();
                    for (int i=0;i<mDatas.size();i++){
                        ReplyBean information=mDatas.get(i);
                        if (information.getRe_ask().contains(query)
                                ||information.getRe_user().contains(query)
                                ||information.getRe_time().contains(query))
                        {
                            findList.add(information);
                        }
                    }
                    if (findList.size()==0){
                        Toast.makeText(getActivity(),"未搜索到结果",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Log.i("NewActivity", "###length="+findList.size());
                        Toast.makeText(getActivity(), "查找成功", Toast.LENGTH_SHORT).show();
                        findListAdapter=new InformAdapter(getActivity(),findList);
                        //findListAdapter.set
                        informRecycler.setAdapter(findListAdapter);
                        findListAdapter.notifyDataSetChanged();
                    }
                }

                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    informRecycler.setAdapter(mAdapter);
                }
                else {
                    findList.clear();
                    for (int i=0;i<mDatas.size();i++){
                        ReplyBean information=mDatas.get(i);
                        if (information.getRe_ask().contains(newText))
                        {
                            findList.add(information);
                        }
                    }
                    findListAdapter=new InformAdapter(getActivity(),findList);
                    findListAdapter.notifyDataSetChanged();
                    informRecycler.setAdapter(findListAdapter);
                    //findListAdapter.set
                }
                return false;
            }
        });

        informRecycler.setLayoutManager(mLayoutManager);
        //informRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //使用系统默认分割线
        informRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        //mAdapter.notifyDataSetChanged();

        //mAdapter=new DataAdapter(getActivity(),)

    }

    //点击响应
    private void inititemClick() {
        informRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), informRecycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        final TextView title=(TextView)view.findViewById(R.id.item_inform_reid);
                        theask_detailsActivity.replyIDStr=title.getText().toString();
                        //Toast.makeText(getActivity(), asktitleStr, Toast.LENGTH_SHORT).show();
                        start_thereplyActivity();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    //跳转到回复详情页面
    private void start_thereplyActivity() {
        Intent intent=new Intent(getActivity(),thereply_detailsActivity.class);
        getActivity().startActivity(intent);
    }

    //toolbar点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
