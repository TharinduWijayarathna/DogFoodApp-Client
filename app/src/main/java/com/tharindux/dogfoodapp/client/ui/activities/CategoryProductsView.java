package com.tharindux.dogfoodapp.client.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import com.tharindux.dogfoodapp.client.DownloadImageFromInternet;
import com.tharindux.dogfoodapp.client.R;

public class CategoryProductsView extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products_view);

        CategoryProductsView main = this;


        LinearLayout linearLayoutMain = findViewById(R.id.CategoryProductViewLayout);


//        TextView textView24 = getActivity().findViewById(R.id.textView24);

        ;


        LayoutInflater inflater = LayoutInflater.from(main);
        Toast.makeText(main, "Loading.........", Toast.LENGTH_LONG).show();
        StorageReference storageRef = storage.getReference();
        firebaseFirestore.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(getIntent().getStringExtra("Category_name"))){
                                    TextView textView1 = findViewById(R.id.textView1);
                                    textView1.setText(getIntent().getStringExtra("Category_name"));
                                    if(document.getData().size()==0){
                                        View view1 =  inflater.inflate(R.layout.no_product, null);
                                        ImageView imageView1 = view1.findViewById(R.id.imageView1);

                                        storageRef.child("sign_error_icon.png")
                                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        new DownloadImageFromInternet(imageView1).execute(uri.toString());

                                                    }
                                                });

                                        TextView textView8 = view1.findViewById(R.id.textView8);
                                        textView8.setText("No product");
                                        linearLayoutMain.addView(view1);

                                    }else {
                                        Map<String, Object> categories = document.getData();
                                        for (Map.Entry<String, Object> entry : categories.entrySet()) {
                                            int done = 0;
                                            View view1 =  inflater.inflate(R.layout.product_view_layout, null);
                                            Map<String, Object> productlist = (Map<String, Object>) entry.getValue();
                                            for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                                if (unite.getKey().equals("Name")) {
                                                    TextView textView8 = view1.findViewById(R.id.textView8);
                                                    textView8.setText(unite.getValue().toString());
                                                }
                                            }
                                            for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                                if (unite.getKey().equals("Price")) {
                                                    TextView textView9 = view1.findViewById(R.id.textView9);
                                                    textView9.setText(unite.getValue().toString());
                                                }
                                            }
                                            for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                                if (unite.getKey().equals("Status")) {
                                                    TextView textView36 = view1.findViewById(R.id.textView36);
                                                    if(unite.getValue().toString().equals("Active")) {
                                                        done = 1;
                                                        textView36.setText("In Stock");
                                                    } else if (unite.getValue().toString().equals("Inactive")) {
                                                        textView36.setText("Out of stock");
                                                        textView36.setTextColor(R.color.red);

                                                    }
                                                }
                                            }
                                            for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                                if (unite.getKey().equals("PicName")) {
                                                    storageRef.child("Products/"+unite.getValue().toString())
                                                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    new DownloadImageFromInternet((ImageView)
                                                                            view1.findViewById(R.id.imageView4)).execute(uri.toString());

                                                                }
                                                            });
                                                }
                                            }
                                            Button button =view1.findViewById(R.id.button2);
                                            if(done==1) {
                                                button.setText("+");
                                                button.setEnabled(true);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        TextView textView8 = view1.findViewById(R.id.textView8);
                                                        Intent i = new Intent(main, SingleProductView.class);
                                                        i.putExtra("Product_name", textView8.getText().toString());
                                                        i.putExtra("email",getIntent().getStringExtra("email").toString());
                                                        startActivity(i);
                                                    }
                                                });
                                            }else {
                                                button.setText("X");
                                                button.setEnabled(false);
                                            }
                                            linearLayoutMain.addView(view1);
//                                    else {
//                                        Button button =view1.findViewById(R.id.button2);
//                                        button.setEnabled(false);
//
//                                    }

                                        }
                                    }


                                }
                            }
                        }
                    }
                });



    }
}