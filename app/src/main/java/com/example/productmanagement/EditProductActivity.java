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

import com.example.productmanagement.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EditProductActivity extends AppCompatActivity {

    TextView addTime;
    EditText name, price;
    Spinner type;
    ArrayList<String> types;
    Button edit, delete, cancel;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        initView();
        addSpinner();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Products");

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        name.setText(product.getName());
        price.setText(product.getMoney() + "");
        for (String t : types) {
            if (t.equals(product.getType())) {
                type.setSelection(types.indexOf(t));
            }
        }
        addTime.setText(product.getDate());
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        addTime.setText(dayOfMonth + " / " + (month + 1) + " / " + year);
                    }
                }, mYear, mMonth, mDay);
                dialog.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
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


                Map<String, Object> data = new HashMap<>();
                data.put("name", n);
                data.put("type", t);
                data.put("date", date);
                data.put("money", money);
                data.put("key", product.getKey());
                reference.child("Product - " + product.getKey())
                        .updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Product - " + product.getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Delete success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancel update", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    private void initView() {
        addTime = findViewById(R.id.edt_time);
        name = findViewById(R.id.edt_name);
        price = findViewById(R.id.edt_money);
        type = findViewById(R.id.edt_type);
        edit = findViewById(R.id.update_btn);
        delete = findViewById(R.id.delete_btn);
        cancel = findViewById(R.id.cancel_update_btn);
    }

    private void addSpinner() {
        types = new ArrayList<>();
        types.add("Điện thoại");
        types.add("Máy tính");
        types.add("Quần áo");
        types.add("Giày");
        types.add("Khác");
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, types );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type.setAdapter(adapter);
    }
}