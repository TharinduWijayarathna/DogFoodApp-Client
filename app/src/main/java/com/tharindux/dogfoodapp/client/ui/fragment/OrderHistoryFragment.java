package com.tharindux.dogfoodapp.client.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tharindux.dogfoodapp.client.R;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class OrderHistoryFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View Orderview, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(Orderview, savedInstanceState);

        TextView textView24 = getActivity().findViewById(R.id.textView24);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(textView24.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> userAllDetails = document.getData();
                            for (Map.Entry<String, Object> PaymentHistory : userAllDetails.entrySet()) {
                                if (PaymentHistory.getKey().equals("PaymentHistory")) {
                                    Map<String, Object> PaymentHistoryAllDetails = (Map<String, Object>) PaymentHistory.getValue();

                                    if (PaymentHistoryAllDetails.entrySet().size() > 0) {
                                        for (Map.Entry<String, Object> HistoryDetails : PaymentHistoryAllDetails.entrySet()) {

                                            LinearLayout linearLayoutOrder = Orderview.findViewById(R.id.orderproductlist);
                                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                                            View view1 = inflater.inflate(R.layout.order_history_view_layout, null);

                                            ArrayList<String> arrayList = new ArrayList<>();

                                            Map<String, Object> HistoryDetailsFields = (Map<String, Object>) HistoryDetails.getValue();
                                            for (Map.Entry<String, Object> ProductSet : HistoryDetailsFields.entrySet()) {
                                                if (ProductSet.getKey().equals("product")) {
                                                    Map<String, Object> ProductFile = (Map<String, Object>) ProductSet.getValue();
                                                    for (Map.Entry<String, Object> ProductDetails : ProductFile.entrySet()) {
                                                        Map<String, Object> ProductFields = (Map<String, Object>) ProductDetails.getValue();

                                                        String name = null;
                                                        String Qty = null;
                                                        String Price = null;
                                                        int done = 0;

                                                        for (Map.Entry<String, Object> ProductFieldsDetails : ProductFields.entrySet()) {

                                                            if (ProductFieldsDetails.getKey().equals("Name")) {
                                                                name = ProductFieldsDetails.getValue().toString();
                                                                done = done + 1;
                                                            }
                                                            if (ProductFieldsDetails.getKey().equals("Qty")) {
                                                                Qty = ProductFieldsDetails.getValue().toString();
                                                                done = done + 1;
                                                            }
                                                            if (ProductFieldsDetails.getKey().equals("Price")) {
                                                                Price = ProductFieldsDetails.getValue().toString();
                                                                done = done + 1;
                                                            }
                                                            if (done == 3) {
                                                                arrayList.add("Name - " + name + "\nQuantity - " + Qty + "\nPrice - " + "Rs." +DecimalFormat.getInstance().format(Double.parseDouble(Price)) + ".00");
                                                            }
                                                        }
                                                    }
                                                }
                                                if (ProductSet.getKey().equals("Total")) {
                                                    TextView textView31 = view1.findViewById(R.id.textView31);
                                                    textView31.setText("Rs." + DecimalFormat.getInstance().format(Double.parseDouble(ProductSet.getValue().toString())) + ".00");
                                                }
                                                if (ProductSet.getKey().equals("Status")) {
                                                    TextView textView6 = view1.findViewById(R.id.textView6);
                                                    textView6.setText(ProductSet.getValue().toString());
                                                    if (ProductSet.getValue().toString().equals("Pending")) {
                                                        textView6.setTextColor(getResources().getColor(R.color.btn_color, null));
                                                    }
                                                    if (ProductSet.getValue().toString().equals("Delivered")) {
                                                        textView6.setTextColor(getResources().getColor(R.color.green, null));
                                                    }
                                                }
                                            }

                                            Spinner spinner = view1.findViewById(R.id.spinner_languages);
                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, arrayList);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spinner.setAdapter(adapter);

                                            TextView textView = view1.findViewById(R.id.textView26);
                                            textView.setText(HistoryDetails.getKey());

                                            linearLayoutOrder.addView(view1);
                                        }
                                    } else {
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfileFragment()).commit();
                                    }
                                }
                            }
                        }
                    }
                });
    }
}
