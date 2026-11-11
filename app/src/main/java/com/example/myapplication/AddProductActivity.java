package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {
    private EditText edtName, edtPrice, edtQty, edtDesc;
    private Spinner spnCategory;
    private Switch swtActive;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseHelper(this);
        edtName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtProductPrice);
        edtQty = findViewById(R.id.edtProductQty);
        edtDesc = findViewById(R.id.edtDescription);
        spnCategory = findViewById(R.id.spnCategory);
        swtActive = findViewById(R.id.swtActive);
        btnSave = findViewById(R.id.btnSaveProduct);

        String[] categories = {"Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spnCategory.setAdapter(catAdapter);

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String qtyStr = edtQty.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();
            String category = spnCategory.getSelectedItem().toString();
            int active = swtActive.isChecked() ? 1 : 0;

            if (name.isEmpty()) {
                edtName.setError("Không được để trống tên sản phẩm");
                return;
            }
            if (priceStr.isEmpty() || Integer.parseInt(priceStr) <= 0) {
                edtPrice.setError("Giá bán phải lớn hơn 0");
                return;
            }
            if (qtyStr.isEmpty() || Integer.parseInt(qtyStr) < 0) {
                edtQty.setError("Số lượng không được âm");
                return;
            }

            int price = Integer.parseInt(priceStr);
            int qty = Integer.parseInt(qtyStr);

            Product p = new Product(name, price, qty, category, active, desc);
            dbHelper.insertProduct(p);
            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}