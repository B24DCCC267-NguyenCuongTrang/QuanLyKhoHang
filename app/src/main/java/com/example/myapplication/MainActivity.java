package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvTotalProducts, tvTotalValue, tvLowStockCount;
    private ListView lvProducts;
    private Button btnOpenAdd;
    private DatabaseHelper dbHelper;
    private ArrayList<Product> productList;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStockCount = findViewById(R.id.tvLowStockCount);
        lvProducts = findViewById(R.id.lvProducts);
        btnOpenAdd = findViewById(R.id.btnOpenAdd);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        lvProducts.setAdapter(adapter);

        btnOpenAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddProductActivity.class));
        });

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product selected = productList.get(position);
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", selected.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        productList.clear();
        productList.addAll(dbHelper.getAllProducts());
        adapter.notifyDataSetChanged();

        DecimalFormat df = new DecimalFormat("#,###");
        tvTotalProducts.setText(String.valueOf(productList.size()));
        tvTotalValue.setText(df.format(dbHelper.getTotalValue()).replace(",", ".") + " đ");
        tvLowStockCount.setText(String.valueOf(dbHelper.countLowStock()));
    }
}