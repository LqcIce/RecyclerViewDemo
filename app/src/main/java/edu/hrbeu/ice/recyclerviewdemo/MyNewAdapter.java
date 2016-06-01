package edu.hrbeu.ice.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ice on 2016/5/29.
 */
public class MyNewAdapter extends RecyclerView.Adapter<MyNewAdapter.MyViewHolder>  {

    private static final String TAG = "MyNewAdapter";
    private final RecyclerView mRecyclerView;
    ArrayList<ItemBean> arrayList;

    Context context;
    MyImageLoader myImageLoader;

    private int mStart, mEnd;
    private boolean firstFlag = true;


    public MyNewAdapter(Context context, final ArrayList<ItemBean> arrayList, RecyclerView recyclerView) {
        this.arrayList = arrayList;
        this.context = context;
        myImageLoader = new MyImageLoader(context);

        mRecyclerView = recyclerView;


    }

    //int i=0;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
      //  Log.e(TAG, "onCreateViewHolder: "+(i++));
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.titleText.setText(arrayList.get(position).title);
        holder.imageView.setTag(arrayList.get(position).drawable);

        if (!myImageLoader.getImageByCache(holder.imageView, arrayList.get(position).drawable)) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
            myImageLoader.getImageByThread(holder.imageView, arrayList.get(position).drawable);
        }

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;
        TextView detailText;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            titleText = (TextView) itemView.findViewById(R.id.title);
            detailText = (TextView) itemView.findViewById(R.id.detail);
        }
    }
}
