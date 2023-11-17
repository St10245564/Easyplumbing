package com.example.EasyPlumbers.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EasyPlumbers.Interface.ItemClickListener;
import com.example.EasyPlumbers.R;

public class SubcategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_subcategory;
    public ImageView image_subcategory;
    private ItemClickListener itemClickListener;
    public SubcategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_subcategory = (TextView) itemView.findViewById(R.id.subcategory_name);
        image_subcategory = (ImageView) itemView.findViewById(R.id.subcategory_image);

        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
