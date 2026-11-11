package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvCategory, tvPrice, tvQty, tvLowStock, tvStockValue, tvDesc;
    private Button btnImport, btnSell, btnBack, btnDelete;
    private DatabaseHelper dbHelper;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DatabaseHelper(this);
        tvName = findViewById(R.id.tvDetailName);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvQty = findViewById(R.id.tvDetailQty);
        tvLowStock = findViewById(R.id.tvDetailLowStock);
        tvStockValue = findViewById(R.id.tvDetailStockValue);
        tvDesc = findViewById(R.id.tvDetailDesc);
        btnImport = findViewById(R.id.btnImportOne);
        btnSell = findViewById(R.id.btnSellOne);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDeleteProduct);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        loadDetail();

        btnImport.setOnClickListener(v -> {
            if (currentProduct != null) {
                dbHelper.updateQuantity(productId, currentProduct.getQuantity() + 1);
                loadDetail();
            }
        });

        btnSell.setOnClickListener(v -> {
            if (currentProduct != null) {
                if (currentProduct.getQuantity() == 0) {
                    Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.updateQuantity(productId, currentProduct.getQuantity() - 1);
                    loadDetail();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteProduct(productId);
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadDetail() {
        currentProduct = dbHelper.getProductById(productId);
        if (currentProduct == null) return;

        DecimalFormat df = new DecimalFormat("#,###");
        tvName.setText(currentProduct.getName());
        tvCategory.setText(currentProduct.getCategory());
        tvPrice.setText(df.format(currentProduct.getPrice()).replace(",", ".") + " đ");
        tvQty.setText(String.valueOf(currentProduct.getQuantity()));

        long stockValue = (long) currentProduct.getPrice() * currentProduct.getQuantity();
        tvStockValue.setText(df.format(stockValue).replace(",", ".") + " đ");

        String desc = currentProduct.getDescription();
        tvDesc.setText((desc == null || desc.isEmpty()) ? "Không có mô tả" : desc);

        if (currentProduct.getQuantity() < 5) {
            tvLowStock.setVisibility(View.VISIBLE);
        } else {
            tvLowStock.setVisibility(View.GONE);
        }
    }
}