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

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import com.tharindux.dogfoodapp.client.DownloadImageFromInternet;
import com.tharindux.dogfoodapp.client.R;
import com.tharindux.dogfoodapp.client.ui.activities.CategoryProductsView;
import com.tharindux.dogfoodapp.client.ui.activities.SingleProductView;
import com.tharindux.dogfoodapp.client.model.Categories;

public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    public void onViewCreated(@NonNull View Fragment, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(Fragment, savedInstanceState);

        LinearLayout linearLayoutMain =  Fragment.findViewById(R.id.LinearLayoutmain);

        TextView textView24 = getActivity().findViewById(R.id.textView24);


        ArrayList<Categories> CategoryList= new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.getInstance().collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryList.add(new Categories(document.getId(),"sample"));
                            }

                            RecyclerView.Adapter adapter= new RecyclerView.Adapter() {
                                @NonNull
                                @Override
                                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                                    View categoryView = inflater.inflate(R.layout.category_view_layout,parent,false);
                                    return new VH(categoryView);
                                }
                                StorageReference storageRef = storage.getReference();
                                @Override
                                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                                    VH vh = (VH)holder;

                                    vh.textViewVH.setText(CategoryList.get(position).getName());

                                    vh.view2VH.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent i = new Intent(getActivity(), CategoryProductsView.class);
                                            i.putExtra("Category_name", vh.textViewVH.getText().toString());
                                            i.putExtra("email",textView24.getText().toString());
                                            startActivity(i);
                                        }
                                    });



                                    storageRef.child("Category/"+CategoryList.get(position).getName()+".png")
                                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    new DownloadImageFromInternet(vh.imageViewVH).execute(uri.toString());
                                                }
                                            });


                                }

                                @Override
                                public int getItemCount() {
                                    return CategoryList.size();
                                }
                            };

                            RecyclerView recyclerView  = Fragment.findViewById(R.id.Categorylist);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                        }

                    }
                });


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        StorageReference storageRef = storage.getReference();
        firebaseFirestore.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                                            textView9.setText(DecimalFormat.getInstance().format(Double.parseDouble(unite.getValue().toString())) + ".00");

                                        }
                                    }
                                    for (Map.Entry<String, Object> unite : productlist.entrySet()) {
                                        TextView textView36 = view1.findViewById(R.id.textView36);
                                        if (unite.getKey().equals("Status")) {
                                            if(unite.getValue().toString().equals("Active")) {
                                                done = 1;
                                                textView36.setText("In stock");
                                            } else if (unite.getValue().toString().equals("Inactive")) {
                                                textView36.setText("our stock");
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
                                                Intent i = new Intent(getActivity(), SingleProductView.class);
                                                i.putExtra("Product_name", textView8.getText().toString());
                                                i.putExtra("email",textView24.getText().toString());
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
                });








    }
    class VH extends RecyclerView.ViewHolder{

        TextView textViewVH;
        ImageView imageViewVH;
        View view2VH;
        public VH(@NonNull View itemView) {
            super(itemView);
            textViewVH = itemView.findViewById(R.id.textView7);
            imageViewVH = itemView.findViewById(R.id.imageView3);
            view2VH = itemView.findViewById(R.id.view2);
        }
    }

}