package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> list;

    public ProductAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        Product p = list.get(position);
        TextView tvName = convertView.findViewById(R.id.tvItemName);
        TextView tvQty = convertView.findViewById(R.id.tvItemQuantity);
        TextView tvPrice = convertView.findViewById(R.id.tvItemPrice);
        TextView tvCategory = convertView.findViewById(R.id.tvItemCategory);
        TextView tvLowStock = convertView.findViewById(R.id.tvItemLowStock);

        DecimalFormat df = new DecimalFormat("#,###");
        tvName.setText(p.getName());
        tvQty.setText("SL: " + p.getQuantity());
        tvPrice.setText("Giá bán: " + df.format(p.getPrice()).replace(",", ".") + " đ");
        tvCategory.setText(p.getCategory());

        if (p.getQuantity() < 5) {
            tvQty.setTextColor(Color.parseColor("#D32F2F"));
            tvLowStock.setVisibility(View.VISIBLE);
        } else {
            tvQty.setTextColor(Color.parseColor("#212121"));
            tvLowStock.setVisibility(View.GONE);
        }

        return convertView;
    }
}