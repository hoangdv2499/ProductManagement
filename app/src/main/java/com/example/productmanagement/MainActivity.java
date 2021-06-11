package com.example.productmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.productmanagement.adapter.ProductAdapter;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView totalMoney;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    FloatingActionButton fabAdd;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button accountSetting, logout;
    String userKey;
    DatabaseReference reference;
    ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ProductAdapter(this);
        initView();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference().child("Products");
        Intent intent = getIntent();
        userKey = intent.getStringExtra("user_key");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products = new ArrayList<>();
                long money = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    Product p = item.getValue(Product.class);
                    if (p.getUserKey().equals(userKey)) {
                        products.add(p);
                        money += p.getMoney();
                    }
                }
                totalMoney.setText("Tổng tiền đã mua : " + money);
                adapter.setProduct(products);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, AddProductActivity.class);
                intent1.putExtra("user_key", userKey);
                startActivity(intent1);
            }
        });
    }

    private void initView() {
        totalMoney = findViewById(R.id.total_money);
        fabAdd = findViewById(R.id.Fab_add);
        accountSetting = findViewById(R.id.action_account_btn);
        logout = findViewById(R.id.action_logout_btn);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Product> list = new ArrayList<>();
                for (Product p : products) {
                    if (p.getName().toLowerCase().contains(newText.toLowerCase())) {
                        list.add(p);
                    }
                }
                adapter.setProduct(list);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });

        MenuItem account = menu.findItem(R.id.action_account_btn);
        account.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent userIntent = new Intent(MainActivity.this, AccountSettingActivity.class);
                userIntent.putExtra("userKey_setting",  userKey);
                startActivity(userIntent);
                return false;
            }
        });

        MenuItem logout = menu.findItem(R.id.action_logout_btn);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}