package com.company.wilipop.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.company.wilipop.R;
import com.company.wilipop.model.Product;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductsActivity extends AppCompatActivity {

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, NewProductActivity.class));
            }
        });

        mRef = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Product>()
                .setIndexedQuery(mRef.child("products/all-products"), mRef.child("products/data"), Product.class)
                .setLifecycleOwner(this)
                .build();

        PostsAdapter adapter = new PostsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_signout) {
            AuthUI.getInstance()
                    .signOut(ProductsActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(ProductsActivity.this, SignInActivity.class));
                            finish();
                        }
                    });
            return true;
        }
        if (id == R.id.my_chats) {
            startActivity(new Intent(ProductsActivity.this, MyChatsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class PostsAdapter extends FirebaseRecyclerAdapter<Product, PostsAdapter.PostViewHolder> {

        class PostViewHolder extends RecyclerView.ViewHolder {
            TextView title, author;
            ImageView photo;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvProductDescription);
                author = itemView.findViewById(R.id.tvSeller);
                photo = itemView.findViewById(R.id.ivPhoto);
            }
        }

        PostsAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
            super(options);
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new PostViewHolder(inflater.inflate(R.layout.product_viewholder, viewGroup, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull PostViewHolder holder, final int position, @NonNull final Product product) {
            final String productKey = getRef(position).getKey();

            holder.title.setText(product.description);
            holder.author.setText(product.displayName);
            Glide.with(holder.itemView.getContext()).load(product.photoUrl).into(holder.photo);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("PRODUCT_KEY", productKey);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}