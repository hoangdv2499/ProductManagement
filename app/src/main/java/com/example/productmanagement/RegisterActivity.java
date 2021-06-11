package com.example.productmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    Button register, cancel;
    EditText email, phone, name, password, address;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        database = FirebaseDatabase.getInstance();
        System.out.println(database);
        reference = database.getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                if (e.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "email is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String p = phone.getText().toString();
                if (p.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "phone is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                String a = address.getText().toString();
                if (a.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "address is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String key = String.valueOf(new Random().nextInt());
                Map<String, Object> data = new HashMap<>();
                data.put("email", e);
                data.put("phone", p);
                data.put("name", n);
                data.put("password", pass);
                data.put("address", a);
                data.put("key", key);

                reference.child("User - " + key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            auth.createUserWithEmailAndPassword(e, pass);
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
                finish();
            }
        });
    }

    private void initView() {
        register = findViewById(R.id.btn_register_rf);
        cancel = findViewById(R.id.btn_cancel);
        phone = findViewById(R.id.edt_phone_register);
        name = findViewById(R.id.edt_name_register);
        password = findViewById(R.id.edt_registerpassword);
        address = findViewById(R.id.edt_address_register);
        email = findViewById(R.id.edt_email_register);
    }
}
