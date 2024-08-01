package com.tharindux.dogfoodapp.client.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tharindux.dogfoodapp.client.DownloadImageFromInternet;
import com.tharindux.dogfoodapp.client.R;
import com.tharindux.dogfoodapp.client.ui.activities.MainLayout;
import com.tharindux.dogfoodapp.client.ui.activities.SingleProductView;
import com.tharindux.dogfoodapp.client.model.Product;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }



    FirebaseFirestore firebaseFirestore ;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onViewCreated(@NonNull View Fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(Fragment, savedInstanceState);

        TextView textView24 = getActivity().findViewById(R.id.textView24);

        StorageReference storageRef = storage.getReference();
        LinearLayout linearLayoutCart = Fragment.findViewById(R.id.cartproductlist);

//Load cart
        firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("User").document(textView24.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    double Total = 0;
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> userAllDetails = document.getData();
                            for (Map.Entry<String, Object> CartDetails : userAllDetails.entrySet()) {
                                if (CartDetails.getKey().equals("Cart")) {
                                    Map<String, Object> details = (Map<String, Object>) CartDetails.getValue();
                                    if(details.entrySet().size()>0){
                                        for (Map.Entry<String, Object> products : details.entrySet()) {
                                            Map<String, Object> productsfiels = (Map<String, Object>) products.getValue();


                                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                                            View view1 = inflater.inflate(R.layout.cart_product_view, null);

                                                String name="";
                                                double price =0;
                                                int qty = 0;

                                                for (Map.Entry<String, Object> fields : productsfiels.entrySet()) {
                                                    if (fields.getKey().equals("Name")) {
                                                        ImageView imageView = view1.findViewById(R.id.imageView4);
                                                        imageView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent i = new Intent(getActivity(), SingleProductView.class);
                                                                i.putExtra("Product_name", fields.getValue().toString());
                                                                i.putExtra("email",textView24.getText().toString());
                                                                startActivity(i);
                                                            }
                                                        });
                                                        name = fields.getValue().toString();
                                                    }
                                                    if (fields.getKey().equals("Price")) {
                                                        price = Double.valueOf(fields.getValue().toString());
                                                    }
                                                    if (fields.getKey().equals("Qty")) {
                                                        qty = Integer.valueOf(fields.getValue().toString());
                                                    }
                                                    if (fields.getKey().equals("PicName")) {
                                                        storageRef.child("Products/"+fields.getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                new DownloadImageFromInternet((ImageView)
                                                                        view1.findViewById(R.id.imageView4)).execute(uri.toString());

                                                            }
                                                        });
                                                    }


                                                    TextView textView8 = view1.findViewById(R.id.textView8);
                                                    textView8.setText(name);

                                                    TextView textView25 = view1.findViewById(R.id.textView25);
                                                    textView25.setText(String.valueOf(price));

                                                    TextView textView32 = view1.findViewById(R.id.textView32);
                                                    textView32.setText(String.valueOf(qty));
                                                    Total = Total + (price*qty)/2;
                                                    updateprice();
                                                    Button button = view1.findViewById(R.id.button2);
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Map<String, Object> Cart = new HashMap<>();
                                                            Map<String, Object> Data = new HashMap<>();
                                                            Data.put(products.getKey().toString(), FieldValue.delete());
                                                            Data.put(products.getKey().toString(), FieldValue.delete());
                                                            Data.put(products.getKey().toString(), FieldValue.delete());
                                                            Cart.put("Cart", Data);
                                                            firebaseFirestore.collection("User").document(textView24.getText().toString())
                                                                    .set(Cart, SetOptions.merge());
                                                            linearLayoutCart.removeView(view1);

                                                            Total = Total - (Double.parseDouble(textView25.getText().toString()) * Double.parseDouble(textView32.getText().toString()));
                                                            updateprice();

                                                            if (linearLayoutCart.getChildCount() == 0) {
                                                                noProduct();
//                                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                                                            }
                                                        }
                                                    });

                                                }
                                                linearLayoutCart.addView(view1);
                                        }
                                    }else {
                                        noProduct();
                                    }

                                }
                            }
                        }


                    }
                    @SuppressLint("ResourceAsColor")
                    public void noProduct(){
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View view1 = inflater.inflate(R.layout.no_product, null);
                        Button button4 = Fragment.findViewById(R.id.button4);
                        TextView textView21 = Fragment.findViewById(R.id.textView21);
                        TextView textView22 = Fragment.findViewById(R.id.textView22);
                        TextView textView23 = Fragment.findViewById(R.id.textView23);

                        button4.setVisibility(View.INVISIBLE);
                        textView21.setVisibility(View.INVISIBLE);
                        textView22.setVisibility(View.INVISIBLE);
                        textView23.setVisibility(View.INVISIBLE);

                        ImageView imageView1 = view1.findViewById(R.id.imageView1);
                        TextView textView8 = view1.findViewById(R.id.textView8);

                        storageRef.child("sign_error_icon.png")
                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        new DownloadImageFromInternet(imageView1).execute(uri.toString());

                                    }
                                });
                        linearLayoutCart.addView(view1);
                    }
                    public void updateprice(){
                        TextView textView22 = Fragment.findViewById(R.id.textView22);
                        textView22.setText(String.valueOf(Total));
                    }
                });





//                Checkout process
        Button button = Fragment.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                Date date = new Date();

                ArrayList<Product> getSampleProductList = new ArrayList<>();
                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("User").document(textView24.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    Map<String, Object> userAllDetails = document.getData();

//                                    ArrayList<Product> productArrayList = new ArrayList<>();
                                    for (Map.Entry<String, Object> CartDetails : userAllDetails.entrySet()) {
                                        if (CartDetails.getKey().equals("Cart")) {

                                            Map<String, Object> PaymentHistory = (Map<String, Object>) CartDetails.getValue();
                                            /////////////
//                                            for (Map.Entry<String, Object> ProductDetails : PaymentHistory.entrySet()) {
//                                                Map<String, Object> ProductDetailsFields = (Map<String, Object>) ProductDetails.getValue();
//
//                                                String Name= null;
//                                                double Price = 0;
//                                                int Qty= 0;
//                                                for (Map.Entry<String, Object> ProductDetailsFieldsValue : ProductDetailsFields.entrySet()) {
//                                                    if(ProductDetailsFieldsValue.getKey().equals("Name")){
//                                                        Name = ProductDetailsFieldsValue.getValue().toString();
//                                                    }
//                                                    if(ProductDetailsFieldsValue.getKey().equals("Price")){
//                                                        Price = Double.parseDouble(ProductDetailsFieldsValue.getValue().toString());
//                                                    }
//                                                    if(ProductDetailsFieldsValue.getKey().equals("Qty")){
//                                                        Qty = Integer.parseInt(ProductDetailsFieldsValue.getValue().toString());
//                                                    }
//                                                }
//                                                productArrayList.add(new Product(Name,Qty,Price,"#"));
//                                            }
//                                            /////////////

                                            TextView textView22 = Fragment.findViewById(R.id.textView22);
                                            Map<String, Object> time = new HashMap<>();
                                            Map<String, Object> PaymentDetails = new HashMap<>();
                                            Map<String, Object> Details = new HashMap<>();

                                            PaymentDetails.put("product",PaymentHistory);
                                            PaymentDetails.put("Total",textView22.getText().toString());
                                            PaymentDetails.put("Status","Pending");

                                            time.put(formatter.format(date),PaymentDetails);
                                            Details.put("PaymentHistory", time);
                                            firebaseFirestore.collection("User").document(textView24.getText().toString())
                                                    .set(Details, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // CartClean

                                                            Map<String,Object> updates = new HashMap<>();
                                                            updates.put("Cart", FieldValue.delete());
                                                                    firebaseFirestore.collection("User").document(textView24.getText().toString())
                                                                    .update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            Intent i = new Intent(getActivity(), MainLayout.class);
                                                                            startActivity(i);
                                                                        }
                                                                    });
                                                        }
                                                    });

                                        }

                                    }

                                }
                            }
                        });
            }
        });

    }


}






