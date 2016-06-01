package edu.hrbeu.ice.recyclerviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyNewAdapter mAdapter;

    ArrayList<ItemBean> arrayList = new ArrayList<ItemBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 1; i < 49; i++) {

            ItemBean itemBean = new ItemBean();
            itemBean.drawable = "https://raw.githubusercontent.com/LqcIce/PicStore/master/pic_"+(i<10?"0"+i:i)+".gif";
            itemBean.title = "item第"+i+"项";
            arrayList.add(itemBean);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //设置为瀑布流模式
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        //设置为list模式
//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyNewAdapter(MainActivity.this,arrayList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


    }


}
