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
import com.bignerdranch.android.geoquiz.login1Activity;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addAskActivity extends  AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private EditText asktitle;
    private EditText askdetails;
    private Button ask_add;
    private Toolbar addask_toolbar;

    public String asktitleStr,askdetailStr,usernameStr;
    public String Url = "http://yuguole.pythonanywhere.com/Iknow/addask";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addask);

        asktitle=(EditText)findViewById(R.id.asktitile);
        askdetails=(EditText)findViewById(R.id.askdetail);
        ask_add=(Button)findViewById(R.id.ask_add);
        ask_add.setOnClickListener(this);

        addask_toolbar = (Toolbar) findViewById(R.id.toolbar_addask);
        addask_toolbar.setTitle("提出新问题");//标题
        addask_toolbar.inflateMenu(R.menu.menu_sendask);
        addask_toolbar.setOnMenuItemClickListener(this);
        addask_toolbar.setNavigationIcon(R.drawable.ic_back2);
        addask_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        switch (id){
            case R.id.ask_add:
                //Toast.makeText(this, Url, Toast.LENGTH_SHORT).show();
                Addask(Url);
                break;

            default:
                break;
        }
    }

    private void Addask(String url){
        asktitleStr = asktitle.getText().toString().trim();
        askdetailStr = askdetails.getText().toString().trim();
//       (image_1)使用的方法 创建一个请求队列

        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求

        Map<String,String> map = new HashMap<>();
        String theTime = DateToStringUtils.date2string(new Date());
        map.put("asktitle", asktitleStr);
        map.put("askdetail", askdetailStr);
        map.put("askuser", login1Activity.usernameStr);
        map.put("asktime",theTime);
        /*
        List<Map<String,String>> asklabelList = new ArrayList<Map<String,String>>();
        Map<String,String> maplabel = new HashMap<>();
        //map.put("asklabel[]","生活");

        //List<String> asklabel=new ArrayList<String>();
        for (int i=0;i<add_asklabelActivity.addLabel.size();i++){
            LabelBean information= add_asklabelActivity.addLabel.get(i);
            maplabel.put("asklabel[]",information.getLb_title());
            asklabelList.add(maplabel);
        }
        map.put("asklabel[]",asklabelList);*/
        //Toast.makeText(getApplicationContext(), asklabel.toString(), Toast.LENGTH_SHORT).show();

        //map.put("asklabel[]","生活");
        /*
        for (int i=0;i<add_asklabelActivity.addLabel.size();i++){
            LabelBean information= add_asklabelActivity.addLabel.get(i);
            Toast.makeText(addAskActivity.this,information.getLb_title(),Toast.LENGTH_SHORT).show();
            map.put("asklabel[]",information.getLb_title());
        }*/

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200||status == 300||status == 510) {
                            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(addAskActivity.this,Homepage.class);
                            startActivity(intent);

//                    QQ名字或者账号重复
                        } else if (status == 500) {
                            Toast.makeText(getApplicationContext(), "问题名重复", Toast.LENGTH_SHORT).show();
//                    出错了
                        } else if (status == 400) {
                            Toast.makeText(getApplicationContext(), "提问失败", Toast.LENGTH_SHORT).show();
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

            case R.id.send_ask:
                //Toast.makeText(add_asklabelActivity.this, addLabel.toString(), Toast.LENGTH_LONG).show();
                Addask(Url);
                break;

        }
        return true;
    }
}
