package com.bignerdranch.android.geoquiz;

import android.content.Intent;
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
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.geoquiz.Fragment.FragmentMain;
import com.bignerdranch.android.geoquiz.Fragment.Homepage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login1Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button register;
    private Button login;



    public static String usernameStr,passwordStr;
    public String Url = "http://yuguole.pythonanywhere.com/Iknow/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login1);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        switch (id){
            case R.id.register:
                Toast.makeText(this, Url, Toast.LENGTH_SHORT).show();
                register();
                break;
            case R.id.login1:
                //Toast.makeText(this, Url, Toast.LENGTH_SHORT).show();
                if(submit()){
                    login1(Url);
                }
                break;
            default:
                break;
        }
    }
    //跳转
    private void register(){
       Intent intent = new Intent(login1Activity.this,RegisterActivity.class);
        startActivity(intent);
    }

    //跳转主界面
    private void toHome(){
        Intent intent2 = new Intent(login1Activity.this, FragmentMain.class);
        startActivity(intent2);
    }


    private boolean submit() {
        usernameStr = username.getText().toString().trim();
        passwordStr = password.getText().toString().trim();

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
        return true;
    }



    //登录
    private void login1(String url) {

        usernameStr = username.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
        //String url = "http://yuguole.pythonanywhere.com/Iknow/login?username="+usernameStr+"&password="+passwordStr;
        //(image_1)使用的方法 创建一个请求队列

        RequestQueue queue = Volley.newRequestQueue(this);
//       (2)使用相应的请求需求
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        Map<String, String> map = new HashMap<>();
        map.put("username", usernameStr);
        map.put("password", passwordStr);

        map.put("Content-Type", "application/json; charset=utf-8");
        JSONObject paramJsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = response.optInt("status");
//                判断注册的状态
                        if (status == 200) {
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            //toHome();
                            Intent intent2=new Intent(login1Activity.this,FragmentMain.class);
                            startActivity(intent2);

                            //finish();

                            //onBackPressed();
//                    QQ名字或者账号重复
                        } else if (status == 400) {
                            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
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
        //App.getRequeatQueue().add(jsonrequest);
    }

    public class myAsyncTask extends AsyncTask<String, Void, Void> {
        //在线程之中所执行的方法
        @Override
        protected Void doInBackground(String... params) {
            login1(params[0]);
            return null;
        }
    }

    /*
    public void setUsername(String UsernameStr){
        this.usernameStr=UsernameStr;
    }

    public String getUsername(){
        return usernameStr;
    }*/
}

/*
 // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    //这里是返回正确反馈的接口（只要请求成功的反馈数据都在这里）
                    public void onResponse(String response) {
                        //数据处理反馈（可以在这里处理服务器返回的数据）

                        //DealResponseFromServer(response);//json数据的解析和用户反馈
                        Log.i("TAG",response);
                        //System.out.println(response.substring(0,100));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //System.out.println("Something went wrong!");
                //error.printStackTrace();

            }
        }){

            @Override
            public Map<String,String>getHeaders(){
                HashMap<String,String>header=new HashMap<String, String>();
                header.put("Accept","application/jason");
                header.put("Content-Type","application/x-www-form-uelencoded");
                return header;
            }
            @Override
            //这里是发送参数的额地方，重写了getParams（）方法（传什么参数给服务器也是实际你自己修改的额
            protected Map<String, String> getParams()
            {
                HashMap<String, String>  params = new HashMap<>();
                // the POST parameters:
                //如果出现空指针异常或者登录失败，先检查这里有没有传进来你要发送的用户名和密码
                //所以在执行get数据方法之前一定要先存数据
                params.put("username", usernameStr);
                params.put("password", passwordStr);
                return params;
            }
            };


        // Add the request to the queue
        App.getRequestQueue().add(stringRequest);
    }
 */


//volley有专门处理error的库，下面就是调用了其中的一些，可以方便调试的时候查找到错误
                /*
                Log.d(TAG, "Volley returned error________________:" + volleyError);
                Class klass = volleyError.getClass();
                if(klass == com.android.volley.AuthFailureError.class) {
                    Log.d(TAG,"AuthFailureError");
                    Toast.makeText(context,"未授权，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.NetworkError.class) {
                    Log.d(TAG,"NetworkError");
                    Toast.makeText(context,"网络连接错误，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.NoConnectionError.class) {
                    Log.d(TAG,"NoConnectionError");
                } else if(klass == com.android.volley.ServerError.class) {
                    Log.d(TAG,"ServerError");
                    Toast.makeText(context,"服务器未知错误，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.TimeoutError.class) {
                    Log.d(TAG,"TimeoutError");
                    Toast.makeText(context,"连接超时，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.ParseError.class) {
                    Log.d(TAG,"ParseError");
                } else if(klass == com.android.volley.VolleyError.class) {
                    Log.d(TAG,"General error");
                }
                Toast.makeText(context,"登录失败",Toast.LENGTH_LONG).show();
                */