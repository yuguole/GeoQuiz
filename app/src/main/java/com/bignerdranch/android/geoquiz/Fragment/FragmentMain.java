package com.bignerdranch.android.geoquiz.Fragment;

import android.os.Bundle;

import com.bignerdranch.android.geoquiz.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.UUID;

public class FragmentMain extends FragmentActivity implements
        OnClickListener {

    //定义3个Fragment的对象
    private Homepage home;
    private Informpage inform;
    private Personpage person;
    //帧布局对象,就是用来存放Fragment的容器
    private FrameLayout flayout;
    //定义底部导航栏的三个布局
    private RelativeLayout course_layout;
    private RelativeLayout found_layout;
    private RelativeLayout settings_layout;
    //定义底部导航栏中的ImageView与TextView
    private ImageView course_image;
    private ImageView found_image;
    private ImageView settings_image;
    private TextView course_text;
    private TextView settings_text;
    private TextView found_text;
    //定义要用的颜色值
    private int whirt = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int blue =0xFF0AB2FB;
    //定义FragmentManager对象
    FragmentManager fManager;

    //private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentmain);
        fManager = getSupportFragmentManager();

        initViews();
    }

    //完成组件的初始化
    public void initViews()
    {
        course_image = (ImageView) findViewById(R.id.home_image);
        found_image = (ImageView) findViewById(R.id.inform_image);
        settings_image = (ImageView) findViewById(R.id.person_image);
        course_text = (TextView) findViewById(R.id.home_text);
        found_text = (TextView) findViewById(R.id.inform_text);
        settings_text = (TextView) findViewById(R.id.person_text);
        course_layout = (RelativeLayout) findViewById(R.id.home_layout);
        found_layout = (RelativeLayout) findViewById(R.id.inform_layout);
        settings_layout = (RelativeLayout) findViewById(R.id.person_layout);
        course_layout.setOnClickListener(this);
        found_layout.setOnClickListener(this);
        settings_layout.setOnClickListener(this);
    }

    //重写onClick事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_layout:
                setChioceItem(0);
                //Toast.makeText(this, gray, Toast.LENGTH_SHORT).show();
                break;
            case R.id.inform_layout:
                setChioceItem(1);
                break;
            case R.id.person_layout:
                setChioceItem(2);
                break;
            default:
                break;
        }

    }


    //定义一个选中一个item后的处理
    public void setChioceItem(int index)
    {
        //重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fManager.beginTransaction();
        clearChioce();
        hideFragments(transaction);
        switch (index) {
            case 0:
                course_image.setImageResource(R.drawable.ic_tabbar_course_pressed);
                course_text.setTextColor(blue);
                course_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (home == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    home = new Homepage();
                    transaction.add(R.id.content, home);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(home);
                }
                break;

            case 1:
                found_image.setImageResource(R.drawable.ic_tabbar_found_pressed);
                found_text.setTextColor(blue);
                found_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (inform == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    inform = new Informpage();
                    transaction.add(R.id.content, inform);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(inform);
                }
                break;

            case 2:
                settings_image.setImageResource(R.drawable.ic_tabbar_settings_pressed);
                settings_text.setTextColor(blue);
                settings_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (person == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    person = new Personpage();
                    transaction.add(R.id.content, person);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(person);
                }
                break;
        }
        transaction.commit();
    }

    //隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (home != null) {
            transaction.hide(home);
        }
        if (inform != null) {
            transaction.hide(inform);
        }
        if (person != null) {
            transaction.hide(person);
        }
    }


    //定义一个重置所有选项的方法
    public void clearChioce()
    {
        course_image.setImageResource(R.drawable.ic_tabbar_course_normal);
        course_layout.setBackgroundColor(whirt);
        course_text.setTextColor(gray);
        found_image.setImageResource(R.drawable.ic_tabbar_found_normal);
        found_layout.setBackgroundColor(whirt);
        found_text.setTextColor(gray);
        settings_image.setImageResource(R.drawable.ic_tabbar_settings_normal);
        settings_layout.setBackgroundColor(whirt);
        settings_text.setTextColor(gray);
    }

}
