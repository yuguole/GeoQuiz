package com.bignerdranch.android.geoquiz.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.geoquiz.Persondetails.myLabelActivity;
import com.bignerdranch.android.geoquiz.Persondetails.myReplyActivity;
import com.bignerdranch.android.geoquiz.all_labelActivity;
import com.bignerdranch.android.geoquiz.Persondetails.myAskActivity;
import com.bignerdranch.android.geoquiz.R;
import com.bignerdranch.android.geoquiz.login1Activity;

public class Personpage extends Fragment implements Toolbar.OnMenuItemClickListener {

    private LinearLayout myAsk,myLabel,myReply;
    private Toolbar toolbar;
    private Button exit;
    private TextView myname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personpage, container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar_person);
        toolbar.setTitle("我的");//标题
        // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_empty);
        //setHasOptionsMenu(true);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //setHasOptionsMenu(true);
        toolbar.setOnMenuItemClickListener(this);
        initView();
    }

    private void initView() {
        myLabel=(LinearLayout) getActivity().findViewById(R.id.layout_mylabel);
        myAsk=(LinearLayout) getActivity().findViewById(R.id.layout_myask);
        myReply=(LinearLayout) getActivity().findViewById(R.id.layout_myreply);
        myname=(TextView)getActivity().findViewById(R.id.person_myusername);
        myname.setText(login1Activity.usernameStr);

        exit=(Button)getActivity().findViewById(R.id.person_exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "mylabel", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),login1Activity.class);
                startActivity(i);
            }
        });

        myLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "mylabel", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),myLabelActivity.class);
                startActivity(i);
            }
        });

        myAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "myask", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),myAskActivity.class);
                startActivity(i);
            }
        });

        myReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "mylabel", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),myReplyActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
