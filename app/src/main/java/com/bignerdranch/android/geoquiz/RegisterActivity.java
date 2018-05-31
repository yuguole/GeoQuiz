package com.bignerdranch.android.geoquiz;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private EditText repassword;
    private Button re_register;

    public String usernameStr, passwordStr, repasswordStr;

    public String Url = "http://yuguole.pythonanywhere.com/Iknow/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.re_username);
        password = (EditText) findViewById(R.id.re_password);
        repassword = (EditText) findViewById(R.id.re2_password);
        re_register = (Button) findViewById(R.id.re_register);

        re_register.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.re_register:
                if (submit()) {
                    Register(Url);
                }
                break;
            default:
                break;
        }
    }

    private boolean submit() {
        usernameStr = username.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
        repasswordStr = repassword.getText().toString().trim();
        // validate
        //username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(usernameStr)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this, "用户密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

//        repeatpassword = et_repeatpassword.getText().toString().trim();
        if (TextUtils.isEmpty(repasswordStr)) {
            Toast.makeText(this, "重复密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
//        判断两次输入的密码值是否一样一样的话就
        if (!(passwordStr.equals(repasswordStr))) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public void Register(String url) {

        usernameStr = username.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
//       (image_1)使用的方法 创建一个请求队列

        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameStr);
        map.put("password", passwordStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200) {
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            onBackPressed();
//                    QQ名字或者账号重复
                        } else if (status == 500) {
                            Toast.makeText(getApplicationContext(), "用户名重复", Toast.LENGTH_SHORT).show();
//                    出错了
                        } else if (status == 400) {
                            Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
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

    public class myAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            Register(params[0]);
            return null;
        }
    }
}


    /*
    private void register(){
        final String usernameStr = username.getText().toString().trim();
        final String passwordStr = password.getText().toString().trim();
        String url = "http://yuguole.pythonanywhere.com/Iknow/register?username="+usernameStr+"&password="+passwordStr;

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.substring(0,100));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("username", usernameStr);
                params.put("password", passwordStr);
                return params;
            }
        };

        // Add the request to the queue
        App.getRequestQueue().add(stringRequest);

    }
    */



