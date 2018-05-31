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
import com.bignerdranch.android.geoquiz.Add.add_asklabelActivity;
import com.bignerdranch.android.geoquiz.Models.AskBean;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.all_labelActivity;
import com.bignerdranch.android.geoquiz.The_details.theask_detailsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Homepage extends Fragment implements Toolbar.OnMenuItemClickListener {
    private RecyclerView noteRecycler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DataAdapter mAdapter;
    private DataAdapter findListAdapter;
    private List<AskBean> findList;
    private List<AskBean> mDatas;


    private Toolbar toolbar;
    private SearchView mSearchView;


    public static String asktitleStr;


    FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(getActivity());

    private static final String url="http://yuguole.pythonanywhere.com/Iknow/showask";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_homepage, container,false);

        noteRecycler = (RecyclerView) view.findViewById(R.id.recyclerview);
        //如果item的内容不改变view布局大小，那使用这个设置可以提高RecyclerView的效率
        noteRecycler.setHasFixedSize(true);
        //设置为垂直


        init();
        setHasOptionsMenu(true);

        return view;
    }

    private void initListener() {
        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipelayout);
        //设置 进度条的颜色变化，最多可以设置4种颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff00ff"),Color.parseColor("#ff0f0f"),Color.parseColor("#0000ff"),Color.parseColor("#000000"));
        //setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
        initPullRefresh();

    }

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

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response!=null&&response.length()>0){
                            JSONArray ask = response.optJSONArray("ask");

                            String dataString = ask.toString();
                            Log.d(TAG, String.valueOf(response));

                            mDatas = new ArrayList<AskBean>();
                            for (int i = 0; i < ask.length(); i++) {
                                JSONObject jsonData = ask.optJSONObject(i);
                                //JSONObject jsonData1 = ask.optJSONObject("fields");
                                AskBean data = new AskBean();
                                data.setTitle(jsonData.optString("title"));
                                data.setDetails(jsonData.optString("details"));
                                data.setAsktime(jsonData.optString("asktime"));
                                data.setAskuser(jsonData.optString("askuser"));

                                mDatas.add(data);
                                //test.setText(ask.toString());
                                //Log.i(TAG,e();getMessage(),volleyError);
                            }
                            //Toast.makeText(getActivity(), mDatas.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_SHORT).show();
                        mAdapter =  new DataAdapter(getActivity(),mDatas);
                        noteRecycler.setAdapter(mAdapter);

                        noteRecycler.setLayoutManager(mLayoutManager);
                        //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        //使用系统默认分割线
                        noteRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
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

    //提问按钮跳转
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar_main);
                toolbar.setTitle("首页");//标题
                // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
                toolbar.inflateMenu(R.menu.menu_homepage);
                //setHasOptionsMenu(true);
                //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                //setHasOptionsMenu(true);
                toolbar.setOnMenuItemClickListener(this);

                MenuItem searchItem=  toolbar.getMenu().findItem(R.id.homepage_search);//findViewById(R.id.homepage_search);
                mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
                initSearchView(searchItem);//搜索


                initListener();//下拉刷新
                inititemClick();//点击跳转

            }

            private void initSearchView(final MenuItem item) {

                //通过 item_allask 获取 actionview

                findList=new ArrayList<AskBean>();
                mSearchView.setQueryHint("你想知道些什么");

                //Toast.makeText(getActivity(), mSearchView.toString(), Toast.LENGTH_SHORT).show();
                mSearchView.setSubmitButtonEnabled(true);

                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        if (TextUtils.isEmpty(query)){
                            Toast.makeText(getActivity(),"请输入查找内容！",Toast.LENGTH_SHORT).show();
                            noteRecycler.setAdapter(mAdapter);
                        }
                        else {
                            findList.clear();
                            for (int i=0;i<mDatas.size();i++){
                                AskBean information=mDatas.get(i);
                                if (information.getTitle().contains(query)
                                        ||information.getDetails().contains(query)
                                        ||information.getAskuser().contains(query))
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
                                findListAdapter=new DataAdapter(getActivity(),findList);
                                //findListAdapter.set
                                noteRecycler.setAdapter(findListAdapter);
                                findListAdapter.notifyDataSetChanged();
                            }
                        }

                        return true;
                    }

                    //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (TextUtils.isEmpty(newText)){
                            noteRecycler.setAdapter(mAdapter);
                        }
                        else {
                            findList.clear();
                            for (int i=0;i<mDatas.size();i++){
                                AskBean information=mDatas.get(i);
                                if (information.getTitle().contains(newText))
                                {
                                    findList.add(information);
                                }
                            }
                            findListAdapter=new DataAdapter(getActivity(),findList);
                            findListAdapter.notifyDataSetChanged();
                            noteRecycler.setAdapter(findListAdapter);
                            //findListAdapter.set
                        }
                        return false;
                    }
                });

                noteRecycler.setLayoutManager(mLayoutManager);
                //noteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                //使用系统默认分割线
                noteRecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                //mAdapter.notifyDataSetChanged();

                //mAdapter=new DataAdapter(getActivity(),)

            }

            private void inititemClick() {
                noteRecycler.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity(), noteRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                // do whatever
                                final TextView title=(TextView)view.findViewById(R.id.item_title);
                                asktitleStr=title.getText().toString();
                                Toast.makeText(getActivity(), asktitleStr, Toast.LENGTH_SHORT).show();
                                startAsk_homeActivity();

                            }

                            @Override public void onLongItemClick(View view, int position) {
                                // do whatever
                            }
                        })
                );
            }

            //跳转到添加问题页面
            private void startAsk_homeActivity() {
                Intent intent=new Intent(getActivity(),theask_detailsActivity.class);
                getActivity().startActivity(intent);
            }



            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.add_ask:
                        //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), add_asklabelActivity.class);
                        //Intent i = new Intent(getActivity(), addAskActivity.class);
                        startActivity(i);
                        break;

                    case R.id.home_label:
               // Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent i2 = new Intent(getActivity(), all_labelActivity.class);
                //Intent i = new Intent(getActivity(), addAskActivity.class);
                startActivity(i2);
                break;
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



}
