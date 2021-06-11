package com.example.productmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productmanagement.R;
import com.example.productmanagement.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddProductActivity extends AppCompatActivity {

    TextView addTime;
    EditText name, price;
    Spinner type;
    String[] types;
    Button add, cancel;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initView();
        Intent intent = getIntent();
        String userKey = intent.getStringExtra("user_key");
        addSpinner();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Products");
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        addTime.setText(dayOfMonth + " / " + (month + 1) + " / " + year);
                    }
                }, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                if (n.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Name is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String t = type.getSelectedItem().toString();
                String date = addTime.getText().toString();
                if (date.equals("Chọn thời gian")) {
                    Toast.makeText(getApplicationContext(), "Date is blank", Toast.LENGTH_SHORT).show();
                    return;
                }if (price.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Money is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                int money = Integer.parseInt(price.getText().toString());

                String key = String.valueOf(new Random().nextInt());

                Map<String, Object> data = new HashMap<>();
                data.put("name", n);
                data.put("type", t);
                data.put("date", date);
                data.put("money", money);
                data.put("key", key);
                data.put("userKey", userKey);
                reference.child("Product - " + key)
                        .setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        addTime = findViewById(R.id.add_time);
        name = findViewById(R.id.add_name);
        price = findViewById(R.id.add_money);
        type = findViewById(R.id.add_type);
        add = findViewById(R.id.add_btn);
        cancel = findViewById(R.id.cancel_btn);
    }

    private void addSpinner() {
        types = new String[]{"Điện thoại", "Máy tính", "Quần áo", "Giày", "Khác"};
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, types );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type.setAdapter(adapter);
    }
}