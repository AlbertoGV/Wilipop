package com.company.wilipop.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.company.wilipop.R;
import com.company.wilipop.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewProductActivity extends AppCompatActivity {

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        mRef = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.btnPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishPost();
            }
        });
    }

    void publishPost(){
        String title   = ((EditText) findViewById(R.id.etTitle)).getText().toString();
        String uid     = "uid-"+FirebaseAuth.getInstance().getUid();
        String author  = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String postKey = FirebaseDatabase.getInstance().getReference().push().getKey();

        mRef.child("products/data").child("product-"+postKey).setValue(new Product(title, uid, author));
        mRef.child("products/all-products").child("product-"+postKey).setValue(true);

        finish();
    }
}
