package com.example.zyb.materialtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by zyb on 2017/2/24.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.MyHolder> {

    private Context mContext;
    private List<FruitBean> mList;

    public FruitAdapter(List<FruitBean> mList) {
        this.mList = mList;
    }

    @Override
    public FruitAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, parent, false);
        final MyHolder holder = new MyHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FruitBean fruitBean = mList.get(position - 1);
                Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, fruitBean.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruitBean.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FruitAdapter.MyHolder holder, int position) {
        FruitBean fruitBean = mList.get(position);
        holder.name.setText(fruitBean.getName());
        Glide.with(mContext).load(fruitBean.getImageId()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView image;
        private TextView name;

        MyHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            image = (ImageView) cardView.findViewById(R.id.fruit_image);
            name = (TextView) cardView.findViewById(R.id.fruit_name);
        }
    }
}
