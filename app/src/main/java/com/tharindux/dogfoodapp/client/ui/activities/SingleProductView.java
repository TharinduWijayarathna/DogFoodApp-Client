package com.tharindux.dogfoodapp.client.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.tharindux.dogfoodapp.client.DownloadImageFromInternet;
import com.tharindux.dogfoodapp.client.R;
import com.tharindux.dogfoodapp.client.model.Product;

public class SingleProductView extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore ;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SingleProductView main = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Product product = new Product();

        StorageReference storageRef = storage.getReference();
        firebaseFirestore.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> categories = document.getData();
                                for (Map.Entry<String, Object> entry : categories.entrySet()) {
                                    if (entry.getKey().equals(getIntent().getStringExtra("Product_name"))) {
                                        Map<String, Object> productlist = (Map<String, Object>) entry.getValue();
                                        for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                            if (unite.getKey().equals("Name")) {
                                                TextView textView12 = findViewById(R.id.textView12);
                                                product.setName(unite.getValue().toString());
                                                textView12.setText(unite.getValue().toString());
                                            }
                                        }
                                        for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                            if (unite.getKey().equals("Price")) {
                                                TextView textView15 = findViewById(R.id.textView15);
                                                product.setPrice(Double.parseDouble(unite.getValue().toString()));
                                                textView15.setText("Rs." + unite.getValue().toString());
                                            }
                                        }
                                        for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                            if (unite.getKey().equals("Qty")) {
                                                TextView textView16 = findViewById(R.id.textView16);
                                                product.setQty(Integer.parseInt(unite.getValue().toString()));
                                                textView16.setText(unite.getValue().toString());
                                            }
                                        }
                                        for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                            if (unite.getKey().equals("Description")) {
                                                TextView textView13 = findViewById(R.id.textView13);
                                                product.setDescription(unite.getValue().toString());
                                                textView13.setText(unite.getValue().toString());
                                            }
                                        }
                                        for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                            if (unite.getKey().equals("PicName")) {
                                                product.setPicName(unite.getValue().toString());
                                                storageRef.child("Products/"+unite.getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        new DownloadImageFromInternet((ImageView)
                                                                findViewById(R.id.imageView6)).execute(uri.toString());

                                                    }
                                                });
                                            }
                                        }
                                    }

                                }
                            }

                            storageRef.child("Tomato-Bottle.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    new DownloadImageFromInternet((ImageView)
                                            findViewById(R.id.imageView8)).execute(uri.toString());
                                }
                            });
                            storageRef.child("cheese.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    new DownloadImageFromInternet((ImageView)
                                            findViewById(R.id.imageView9)).execute(uri.toString());
                                }
                            });
                            storageRef.child("red-onion.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    new DownloadImageFromInternet((ImageView)
                                            findViewById(R.id.imageView10)).execute(uri.toString());
                                }
                            });

                        }
                    }
                });



        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText editTextNumber = findViewById(R.id.editTextNumber);

                TextView textView16 = findViewById(R.id.textView16);

                if(editTextNumber.getText().length()>0){
                    if((Integer.parseInt(editTextNumber.getText().toString())) >0){
                        if(Integer.parseInt(editTextNumber.getText().toString()) <= Integer.parseInt(textView16.getText().toString())){

                            Toast.makeText(SingleProductView.this, "Please Wait..", Toast.LENGTH_LONG).show();
                            Map<String, Object> Cart = new HashMap<>();
                            Map<String, Object> CartProducts = new HashMap<>();
                            Map<String, Object> Data = new HashMap<>();
                            Data.put("Name", product.getName());
                            Data.put("Price", product.getPrice());
                            Data.put("Qty", editTextNumber.getText().toString());
                            Data.put("Description", product.getDescription().toString());
                            Data.put("PicName", product.getPicName().toString());

                            CartProducts.put(getIntent().getStringExtra("Product_name").toString(), Data);

                            Cart.put("Cart", CartProducts);
                            firebaseFirestore.collection("User").document(getIntent().getStringExtra("email").toString())
                                    .set(Cart, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent i = new Intent(main, MainLayout.class);
                                            i.putExtra("email",getIntent().getStringExtra("email").toString());
                                            startActivity(i);
                                        }
                                    });
                        }else {
                            Toast.makeText(SingleProductView.this, "Invalid Quantity", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(SingleProductView.this, "Invalid Quantity", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(SingleProductView.this, "Please Enter the Quantity", Toast.LENGTH_LONG).show();
                }

            }
        });

        TextView textView35 = findViewById(R.id.textView35);
        textView35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main, MainLayout.class);
                i.putExtra("email",getIntent().getStringExtra("email").toString());
                startActivity(i);
            }
        });


    }
}
