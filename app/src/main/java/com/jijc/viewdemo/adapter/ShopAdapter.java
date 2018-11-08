package com.jijc.viewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jijc.viewdemo.R;
import com.jijc.viewdemo.bean.Item;
import com.jijc.viewdemo.utils.GlideCircleTransform;
import com.jijc.viewdemo.utils.UIUtils;

import java.util.List;

/**
 * Created by jijc on 2018.11.08
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Item> data;
    private int tabItemWidth;
    private Context mContext;
    public ShopAdapter(List<Item> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_card, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        /**
         * 重要，位了适配屏幕
         * @see com.jijc.viewdemo.activity.Demo13Activity 的 onCreate(Bundle savedInstanceState)
         */
        tabItemWidth = (int) ((UIUtils.getScreenWidth(parent.getContext())-UIUtils.dip2px(50)) / 4.0f);
        layoutParams.width = tabItemWidth;
        layoutParams.height = tabItemWidth;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(data.get(position).getImage()).transform(new GlideCircleTransform(holder.itemView.getContext()))
                .into(holder.image);
        holder.bindItem(data.get(position),position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private Item mItem;
        private int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }
        void bindItem(Item item, int position) {
            this.mItem = item;
            this.mPosition = position;
        }

        @Override
        public void onClick(View view) {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClickListener(view,mItem,mPosition);
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClickListener(View view, Item item, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
}
