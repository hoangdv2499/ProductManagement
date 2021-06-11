package com.example.productmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productmanagement.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AccountSettingActivity extends AppCompatActivity {

    EditText email, name, password, phone, address;
    Button cancel, update;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        initView();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        Intent intent = getIntent();
        String userKey = intent.getStringExtra("userKey_setting");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            User user = item.getValue(User.class);
                            if (user.getKey().equals(userKey)) {
                                email.setText(user.getEmail());
                                name.setText(user.getName());
                                password.setText(user.getPassword());
                                phone.setText(user.getPhone());
                                address.setText(user.getAddress());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                if (n.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "name is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pass = password.getText().toString();
                if (pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "pass is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "password is no less than 6", Toast.LENGTH_SHORT).show();
                    return;
                }

                String p = phone.getText().toString();
                if (p.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "phone is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String a = address.getText().toString();
                if (a.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "address is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = new HashMap<>();
                data.put("phone", p);
                data.put("name", n);
                data.put("password", pass);
                data.put("address", a);

                reference.child("User - " + userKey)
                        .updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initView() {
        name = findViewById(R.id.setting_profile_name);
        password = findViewById(R.id.setting_profile_password);
        email = findViewById(R.id.setting_profile_email);
        phone = findViewById(R.id.setting_profile_phone);
        address = findViewById(R.id.setting_profile_address);
        cancel = findViewById(R.id.cancel_setting_button);
        update = findViewById(R.id.update_setting_button);
        email.setEnabled(false);
    }
}