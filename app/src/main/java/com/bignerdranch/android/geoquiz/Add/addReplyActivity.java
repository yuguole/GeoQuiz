package com.bignerdranch.android.geoquiz.Add;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Fragment.Homepage;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.Untils.DateToStringUtils;
import com.bignerdranch.android.geoquiz.The_details.theask_detailsActivity;
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addReplyActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private EditText edit_reply;
    //private Button button_addreply;
    private Toolbar addreply_toolbar;

    public String replyStr;
    public String url = "http://yuguole.pythonanywhere.com/Iknow/addreply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreply);

        initView();
    }

    private void initView() {
        edit_reply=(EditText)findViewById(R.id.edit_addreply);

        addreply_toolbar = (Toolbar) findViewById(R.id.toolbar_addreply);
        addreply_toolbar.setTitle("(回答)"+Homepage.asktitleStr);//标题

        addreply_toolbar.inflateMenu(R.menu.menu_sendreply);
        addreply_toolbar.setOnMenuItemClickListener(this);
        addreply_toolbar.setNavigationIcon(R.drawable.ic_back2);
        addreply_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void addreply() {
        replyStr=edit_reply.getText().toString().trim();
        //       (image_1)使用的方法 创建一个请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求

        //获取当前时间
        String thereplyTime = DateToStringUtils.date2string(new Date());

        Map<String, String> map = new HashMap<>();
        map.put("reaskid", theask_detailsActivity.askIDStr);
        map.put("redetail", replyStr);
        map.put("reuser", login1Activity.usernameStr);
        map.put("retime",thereplyTime);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200) {
                            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(addReplyActivity.this,theask_detailsActivity.class);
                            startActivity(intent);
                            //onBackPressed();
//
                        } else if (status == 500) {
                            Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
//                    出错了
                        } else if (status == 400) {
                            Toast.makeText(getApplicationContext(), "回答失败", Toast.LENGTH_SHORT).show();
                        }
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

            case R.id.send_reply:
                //Toast.makeText(add_asklabelActivity.this, addLabel.toString(), Toast.LENGTH_LONG).show();
                //Addask(Url);
                addreply();
                break;

        }
        return true;
    }
}
