package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.KeyEvent;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity  {
    private ImageView firstimage;
    private LinearLayout firstlayout;


    //private Button mLoginButton;

    String username;

    //两次返回键退出程序
    boolean isExit;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                System.exit(0);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        firstlayout=(LinearLayout)findViewById(R.id.layout_first);
        firstlayout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);

        firstimage=(ImageView)findViewById(R.id.firstimage);
        //firstimage.setBackgroundResource
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(2000);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                skip();
            }
        });
        firstimage.startAnimation(anim);
    }

    public void skip() {
        Intent intent = new Intent();
        intent.setClass(this, login1Activity.class);
        startActivity(intent);
        finish();
    }



        /*
         mLoginButton=(Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(QuizActivity.this,login1Activity.class);
                startActivity(i);
            }
        });
         */





}
