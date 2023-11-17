package com.example.EasyPlumbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.EasyPlumbers.Interface.ItemClickListener;
import com.example.EasyPlumbers.Model.Subcategory;
import com.example.EasyPlumbers.ViewHolder.SubcategoryViewHolder;
import com.example.EasyPlumbers.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView_subcategory;
    RecyclerView.LayoutManager layoutManager_subcategory;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String categoryId ="";
    FirebaseRecyclerAdapter<Subcategory, SubcategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Subcategory");

        recyclerView_subcategory = (RecyclerView) findViewById(R.id.recycler_subcategory);
        recyclerView_subcategory.setHasFixedSize(true);
        layoutManager_subcategory = new LinearLayoutManager(this);
        recyclerView_subcategory.setLayoutManager(layoutManager_subcategory);

        loadAllSubcategories();
      /*  if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
            Log.d("SubCategoryActivity", "Received categoryId: " + categoryId);
            if (categoryId != null && !categoryId.isEmpty()) {
                loadCategoryID(categoryId);
            }
        }*/

    }

    private void loadAllSubcategories() {
        adapter = new FirebaseRecyclerAdapter<Subcategory, SubcategoryViewHolder>(
                Subcategory.class,
                R.layout.subcategory_item,
                SubcategoryViewHolder.class,
                databaseReference
        ) {
            @Override
            public void populateViewHolder(SubcategoryViewHolder subcategoryViewHolder, Subcategory subcategory, int i) {
                subcategoryViewHolder.txt_subcategory.setText(subcategory.getName());
                Picasso.with(getBaseContext()).load(subcategory.getImage())
                        .into(subcategoryViewHolder.image_subcategory);

                final  Subcategory local = subcategory;
                subcategoryViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(SubCategoryActivity.this, "" + local.getName(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SubCategoryActivity.this, ShowSubcategoryDetailsActivity.class);
                        intent.putExtra("SubcategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        Log.d("TAG", "ItemCount: " + adapter.getItemCount());
        recyclerView_subcategory.setAdapter(adapter);
    }

}