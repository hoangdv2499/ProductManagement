package com.example.productmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productmanagement.EditProductActivity;
import com.example.productmanagement.R;
import com.example.productmanagement.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textName.setText("Tên sản phẩm : " + product.getName());
        holder.textType.setText("Loại sản phẩm : " + product.getType());
        holder.textDate.setText("Ngày mua : " + product.getDate());
        holder.textMoney.setText("Giá tiền : " + product.getMoney());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textId, textName, textType, textDate, textMoney;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName      = itemView.findViewById(R.id.item_name);
            textType      = itemView.findViewById(R.id.item_type);
            textDate      = itemView.findViewById(R.id.item_date);
            textMoney      = itemView.findViewById(R.id.item_money);
        }
    }

    public void setProduct(List<Product> products){
        this.products = products;
    }
}
