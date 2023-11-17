package com.example.EasyPlumbers;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EasyPlumbers.Interface.ItemClickListener;
import com.example.EasyPlumbers.Model.Category;
import com.example.EasyPlumbers.Model.Subcategory;
import com.example.EasyPlumbers.ViewHolder.MenuViewHolder;
import com.example.EasyPlumbers.ViewHolder.PopularAdapter;
import com.example.EasyPlumbers.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView, recyclerViewPopu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    //Getting popular data
    FirebaseRecyclerAdapter<Subcategory, PopularAdapter> popularAdapter;
    DatabaseReference popular;

    //WishList
    DatabaseReference wishlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        popular = database.getReference("Subcategory");
        wishlist = database.getReference("Wishlist");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView txtFullNames = (TextView) headerView.findViewById(R.id.fullNames);
        txtFullNames.setText(Common.currentUse.getName());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);

// Use LinearLayoutManager with a horizontal orientation
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Recycle popular
        recyclerViewPopu = (RecyclerView)findViewById(R.id.recycler_popu);
        recyclerViewPopu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopu.setLayoutManager(layoutManager);


        loadData();
        loadPopularRecycleview();
    }

    private void loadPopularRecycleview() {
        popularAdapter = new FirebaseRecyclerAdapter<Subcategory, PopularAdapter>(Subcategory.class, R.layout.popular_items, PopularAdapter.class, popular) {
            @Override
            protected void populateViewHolder(PopularAdapter popularAdapter, Subcategory subcategory, int i) {
                popularAdapter.title.setText(subcategory.getName());
                popularAdapter.price.setText("R" + subcategory.getPrice());
                Picasso.with(getBaseContext()).load(subcategory.getImage())
                        .into(popularAdapter.img);

                final  Subcategory local = subcategory;
                popularAdapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomeActivity.this, "" + local.getName(), Toast.LENGTH_SHORT).show();

                        // Use the key provided in populateViewHolder method
                        Intent intent = new Intent(HomeActivity.this, ShowSubcategoryDetailsActivity.class);
                        intent.putExtra("SubcategoryId", getRef(position).getKey());
                        startActivity(intent);
                    }
                });

                // When the wishlist button is clicked
                popularAdapter.whishList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the current user's unique identifier (you should replace 'getUserUniqueId()' with the actual way to get the user's unique ID)
                        String userUniqueId = Common.currentUse.getPhone();


                        // Create a unique key for the wishlist item (you can use a timestamp or a unique identifier)
                        String wishlistItemId = String.valueOf(System.currentTimeMillis());

                        // Create a wishlist item and populate it with the data you want to save
                        Subcategory wishlistItem = new Subcategory(
                                wishlistItemId,
                                subcategory.getName(),
                                subcategory.getImage(),
                                subcategory.getDescription(),
                                subcategory.getPrice(),
                                subcategory.getDiscount(),
                                subcategory.getCategoryId()
                        );

                        // Use the wishlist reference to push the wishlist item to the database, associating it with the user's unique identifier
                        wishlist.child(userUniqueId).child(wishlistItemId).setValue(wishlistItem);

                        Toast.makeText(HomeActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerViewPopu.setAdapter(popularAdapter);
    }


    private void AddToWhishList() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        // Handle your menu item clicks here...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here.
        int id = item.getItemId();

        // Implement your navigation logic here based on the clicked item's ID.
        // For example, you can use a NavController or perform actions accordingly.
        switch (id) {
            case R.id.nav_menu:
                // Handle Home navigation
                // Example: startActivity(new Intent(this, HomeFragment.class));
                break;
            case R.id.nav_cart:
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_orders:
                Intent intents = new Intent(HomeActivity.this, OrderStatusActivity.class);
                startActivity(intents);
                break;
            case R.id.nav_logout:
                Intent intentss = new Intent(HomeActivity.this, SignIn.class);
                intentss.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentss);
                break;

            default:
                break;
        }

        // Close the drawer after handling the click
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadData() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category model, int i) {
                menuViewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(menuViewHolder.imageView);

                final Category clickItem = model;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomeActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity.this, SubCategoryActivity.class);
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

    }
}
