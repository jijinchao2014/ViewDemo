package com.jijc.viewdemo.bean;

import com.jijc.viewdemo.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jijc on 2018.11.08
 */

public class Shop {

    public static Shop get() {
        return new Shop();
    }

    private Shop() {
    }

    public List<Item> getData() {
        return Arrays.asList(
                new Item(1, "01 Everyday Candle", "$12.00 USD", R.drawable.shop1),
                new Item(2, "02 Small Porcelain Bowl", "$50.00 USD", R.drawable.shop2),
                new Item(3, "03 Favourite Board", "$265.00 USD", R.drawable.shop3),
                new Item(4, "04 Earthenware Bowl", "$18.00 USD", R.drawable.shop4),
                new Item(5, "05 Porcelain Dessert Plate", "$36.00 USD", R.drawable.shop5),
                new Item(6, "06 Detailed Rolling Pin", "$145.00 USD", R.drawable.shop6));
    }
}
