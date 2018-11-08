package com.jijc.viewdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jijc.viewdemo.R;
import com.jijc.viewdemo.adapter.ShopAdapter;
import com.jijc.viewdemo.bean.Item;
import com.jijc.viewdemo.bean.Shop;
import com.jijc.viewdemo.gallery.DSVOrientation;
import com.jijc.viewdemo.gallery.DiscreteScrollView;
import com.jijc.viewdemo.gallery.InfiniteScrollAdapter;
import com.jijc.viewdemo.gallery.ScaleTransformer;
import com.jijc.viewdemo.utils.ListUtils;
import com.jijc.viewdemo.utils.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 多图滚动，支持第一张图片在中间和居左，在布局文件中设置
 * is_first_center = true 即为第一张图居中显示
 * 注意 轮播的图片至少是2张 注意图片的屏幕适配问题 这里动态计算和设置item和父容器的宽高
 * 参照： https://github.com/yarolegovich/DiscreteScrollView
 * 主要改动点：
 * {@link com.jijc.viewdemo.gallery.DiscreteScrollLayoutManager #layoutView(RecyclerView.Recycler recycler, int position, Point viewCenter)}
 */
public class Demo13Activity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {

    private List<Item> data;
    private Shop shop;

    private TextView currentItemName;
    private TextView currentItemPrice;
    private LinearLayout ll_roll;
    private DiscreteScrollView itemPicker;
    private DiscreteScrollView itemPicker1;
    private InfiniteScrollAdapter infiniteAdapter;
    private InfiniteScrollAdapter infiniteAdapter1;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo13);

        currentItemName = (TextView) findViewById(R.id.item_name);
        currentItemPrice = (TextView) findViewById(R.id.item_price);
        ll_roll = (LinearLayout) findViewById(R.id.ll_roll);
        //重要，位了适配屏幕
        int tabItemWidth = (int) ((UIUtils.getScreenWidth(this)-UIUtils.dip2px(50)) / 4.0f);
        ll_roll.getLayoutParams().height = tabItemWidth+UIUtils.dip2px(10);
        itemPicker = (DiscreteScrollView) findViewById(R.id.item_picker);
        itemPicker1 = (DiscreteScrollView) findViewById(R.id.item_picker1);

        shop = Shop.get();
        data = shop.getData();
        //重要 滚动图片支持有两张
        if (ListUtils.getSize(data) > 1){
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    int position = itemPicker.getCurrentItem();
                    position++;
                    position++;
                    itemPicker.smoothScrollToPosition(position);
                    itemPicker1.smoothScrollToPosition(position);
                }
            };
            timer.schedule(task,5000,5000);

            ll_roll.setVisibility(View.VISIBLE);
            itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
            itemPicker.addOnItemChangedListener(this);
            ShopAdapter adapter = new ShopAdapter(data);
            infiniteAdapter = InfiniteScrollAdapter.wrap(adapter);
            itemPicker.setAdapter(infiniteAdapter);
            itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.8f)
                    .build());
            adapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(View view, Item item, int position) {
                    Toast.makeText(Demo13Activity.this, "第一个："+item.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            itemPicker1.setOrientation(DSVOrientation.HORIZONTAL);
            itemPicker1.addOnItemChangedListener(this);
            ShopAdapter adapter1 = new ShopAdapter(data);
            infiniteAdapter1 = InfiniteScrollAdapter.wrap(adapter1);
            itemPicker1.setAdapter(infiniteAdapter1);
            itemPicker1.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(1f)
                    .build());
            adapter1.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(View view, Item item, int position) {
                    Toast.makeText(Demo13Activity.this, "第二个："+item.getName(), Toast.LENGTH_SHORT).show();
                }
            });


            onItemChanged(data.get(0));
        }else {
            ll_roll.setVisibility(View.GONE);
        }

    }

    private void onItemChanged(Item item) {
        currentItemName.setText(item.getName());
        currentItemPrice.setText(item.getPrice());
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int position) {
        int positionInDataSet = infiniteAdapter.getRealPosition(position);
        onItemChanged(data.get(positionInDataSet));
    }

}
