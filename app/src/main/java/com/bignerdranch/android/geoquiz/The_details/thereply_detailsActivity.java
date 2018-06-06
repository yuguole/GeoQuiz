package com.bignerdranch.android.geoquiz.The_details;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Add.addReplyActivity;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class thereply_detailsActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private TextView thereply_user;
    private TextView thereply_time;
    private TextView thereply_details;


    private Toolbar thereply_toolbar;

    private String there_asktitle;
    private static int good_status;
    private static int bad_status;

    //定义底部导航栏中的ImageView与TextView
    private ImageView good_image;
    private ImageView bad_image;
    private TextView good_number;
    private TextView bad_number;
    private RelativeLayout good_layout;
    private RelativeLayout bad_layout;

    private static final String url = "http://yuguole.pythonanywhere.com/Iknow/thereply";
    private static final String urladdgood = "http://yuguole.pythonanywhere.com/Iknow/addreply_like";
    private static final String urlgooddel = "http://yuguole.pythonanywhere.com/Iknow/delete_relike";
    private static final String urladdbad = "http://yuguole.pythonanywhere.com/Iknow/addreply_bad";
    private static final String urlbaddel = "http://yuguole.pythonanywhere.com/Iknow/delete_rebad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thereply_details);

        initView();
    }

    private void initView() {
        thereply_user=(TextView)findViewById(R.id.thereply_user);
        thereply_time=(TextView)findViewById(R.id.thereply_time);
        thereply_details=(TextView)findViewById(R.id.thereply_details);

        thereply_toolbar = (Toolbar) findViewById(R.id.toolbar_thereply);
        //thereply_toolbar.setTitle();//标题
        thereply_toolbar.inflateMenu(R.menu.menu_thereply);
        thereply_toolbar.setOnMenuItemClickListener(this);
        thereply_toolbar.setNavigationIcon(R.drawable.ic_back2);
        thereply_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        good_image = (ImageView) findViewById(R.id.good_image);
        bad_image = (ImageView) findViewById(R.id.bad_image);
        good_number = (TextView) findViewById(R.id.good_number);
        bad_number = (TextView) findViewById(R.id.bad_number);
        good_layout=(RelativeLayout)findViewById(R.id.good_layout);
        bad_layout=(RelativeLayout)findViewById(R.id.bad_layout);
        good_layout.setOnClickListener(this);
        bad_layout.setOnClickListener(this);

        good_status=0;
        bad_status=0;

        initText();
        //initImage();
        //refresh();
    }


    private void initImage() {
        if (good_status==0){
            good_image.setImageResource(R.drawable.ic_good_normal);

        }else if (good_status==1){
            good_image.setImageResource(R.drawable.ic_good_had);
        }
        if (bad_status==0){
            bad_image.setImageResource(R.drawable.ic_bad_normal);
        }else if (bad_status==1){
            bad_image.setImageResource(R.drawable.ic_bad_had);
        }
    }

    @Override
    public void onClick(View view) {

       switch (view.getId()){
           case R.id.good_layout:
               setChioce(0);
               //Toast.makeText(this, gray, Toast.LENGTH_SHORT).show();
               break;
           case R.id.bad_layout:
               setChioce(1);
               break;
       }

    }

    private void setChioce(int i) {
        switch (i) {
            case 0:
            {
                if(good_status==0 && bad_status==0){
                    addgood();

                }
                else if (good_status==0 && bad_status==1){
                    delbad();
                    addgood();

                }else if (good_status==1 && bad_status==0){
                    delgood();
                }else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }

                initText();

            }
                break;

            case 1:
            {
                if (bad_status==0 && good_status==0){
                    addbad();
                    //delgood();
                }
                else if (bad_status==0 && good_status==1){
                    addbad();
                    delgood();
                }else if (bad_status==1 && good_status==0){
                    delbad();
                }else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }

                initText();
            }
                //course_text.setTextColor(blue);
                break;
        }
    }

    private void addgood() {
        //(image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);
        map.put("replyid",theask_detailsActivity.replyIDStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urladdgood,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int status=response.optInt("status");
                            if (status == 200) {
                                good_status=1;
                                //bad_status=0;
                                //refresh();
                                Toast.makeText(getApplicationContext(), "支持+1", Toast.LENGTH_SHORT).show();
                                //toHome();
//                    QQ名字或者账号重复
                            } else {
                                Toast.makeText(getApplicationContext(), "评价失败", Toast.LENGTH_SHORT).show();
                            }
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

    private void delgood() {
        //(image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);
        map.put("replyid",theask_detailsActivity.replyIDStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlgooddel,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int status=response.optInt("status");
                            if (status == 200) {
                                //Toast.makeText(getApplicationContext(), "取消点赞", Toast.LENGTH_SHORT).show();
                                //bad_image.setImageResource(R.drawable.ic_bad_had);
                                good_status=0;
                                //refresh();
                                //toHome();
//                    QQ名字或者账号重复
                            } else {
                                Toast.makeText(getApplicationContext(), "取消失败", Toast.LENGTH_SHORT).show();
                            }
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

    private void addbad() {
        //(image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);
        map.put("replyid",theask_detailsActivity.replyIDStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urladdbad,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int status=response.optInt("status");
                            if (status == 200) {

                                //refresh();
                                bad_status=1;
                                //good_status=0;
                                Toast.makeText(getApplicationContext(), "不支持+1", Toast.LENGTH_SHORT).show();
                                //toHome();
//                    QQ名字或者账号重复
                            } else {
                                Toast.makeText(getApplicationContext(), "评价失败", Toast.LENGTH_SHORT).show();
                            }
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

    private void delbad() {
        //(image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", login1Activity.usernameStr);
        map.put("replyid",theask_detailsActivity.replyIDStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlbaddel,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int status=response.optInt("status");
                            if (status == 200) {
                                //Toast.makeText(getApplicationContext(), "取消点赞", Toast.LENGTH_SHORT).show();

                                //good_image.setImageResource(R.drawable.ic_good_normal);
                                //refresh();
                                bad_status=0;
                                //toHome();
//                    QQ名字或者账号重复
                            } else {
                                Toast.makeText(getApplicationContext(), "取消失败", Toast.LENGTH_SHORT).show();
                            }
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

    private void initText() {
        //(image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, urlask, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("replyid", theask_detailsActivity.replyIDStr);


        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {

                            int id=response.optInt("id");
                            theask_detailsActivity.replyIDStr=""+id;

                            JSONObject there_user = response.optJSONObject("re_user");
                            thereply_user.setText(there_user.optString("username"));
                            thereply_details.setText(response.optString("re_details"));
                            thereply_time.setText(response.optString("re_time"));

                            JSONObject there_ask = response.optJSONObject("re_ask");
                            there_asktitle=there_ask.optString("ask_title");
                            thereply_toolbar.setTitle(there_asktitle);//标题

                            JSONArray goods = response.optJSONArray("re_like");
                            good_number.setText(goods.length()+"");
                            for (int i = 0; i < goods.length(); i++) {
                                JSONObject jsonData1 = goods.optJSONObject(i);
                                String good_user=jsonData1.optString("username");
                                if (good_user.equals(login1Activity.usernameStr)){
                                    good_status=1;
                                }
                            }

                            JSONArray bads = response.optJSONArray("re_bad");
                            bad_number.setText(bads.length()+"");
                            for (int i = 0; i < bads.length(); i++) {
                                JSONObject jsonData2 = bads.optJSONObject(i);
                                String bad_user=jsonData2.optString("username");
                                if (bad_user.equals(login1Activity.usernameStr)){
                                    bad_status=1;
                                }
                            }

                            initImage();
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

            case R.id.thereply_refrash:
                initText();
                break;


        }
        return true;
    }


}
