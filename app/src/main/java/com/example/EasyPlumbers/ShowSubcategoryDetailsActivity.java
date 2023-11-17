package com.example.EasyPlumbers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.EasyPlumbers.Database.Database;
import com.example.EasyPlumbers.Model.Order;
import com.example.EasyPlumbers.Model.Subcategory;
import com.example.EasyPlumbers.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowSubcategoryDetailsActivity extends AppCompatActivity {

    TextView sub_name, sub_price, sub_description;
    ImageView sub_image_view;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String subId = "";
    FirebaseDatabase database;
    DatabaseReference subcategory, addToCart;
    Subcategory currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_subbcategorydetails);

        database = FirebaseDatabase.getInstance();
        subcategory =database.getReference("Subcategory");
        addToCart = database.getReference("AddToCart");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        subId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(ShowSubcategoryDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        sub_name = (TextView) findViewById(R.id.food_name);
        sub_price = (TextView) findViewById(R.id.food_price);
        sub_description = (TextView) findViewById(R.id.food_description);
        sub_image_view= (ImageView)findViewById(R.id.img_sub);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaps);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        if (getIntent() != null) {
            subId = getIntent().getStringExtra("SubcategoryId");
            Log.d("ShowDetailActivity", "Received SubcategoryId: " + subId);
            if (subId != null && !subId.isEmpty()) {
                getFoodDetail(subId);
            }
        }


    }

    private void getFoodDetail(String subId) {
         subcategory.child(subId).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 currentFood = snapshot.getValue(Subcategory.class);
                 Picasso.with(getBaseContext()).load(currentFood.getImage())
                         .into(sub_image_view);

                 collapsingToolbarLayout.setTitle(currentFood.getName());
                 sub_price.setText(currentFood.getPrice());
                 sub_name.setText(currentFood.getName());
                 sub_description.setText(currentFood.getDescription());
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}