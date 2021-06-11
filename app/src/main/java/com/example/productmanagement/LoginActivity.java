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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button login, register;
    EditText email, password;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        auth = FirebaseAuth.getInstance();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    users.add(item.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                if (e.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "email is blank", Toast.LENGTH_SHORT).show();
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
                for (User u : users) {
                    if (u.getEmail().equals(e) && u.getPassword().equals(pass)) {
                        auth.signInWithEmailAndPassword(e, pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("user_key", u.getKey());
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        break;
                    }
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register_lf);
        email = findViewById(R.id.edt_email_login);
        password = findViewById(R.id.edt_loginpassword);
    }
}