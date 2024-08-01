package com.tharindux.dogfoodapp.client.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tharindux.dogfoodapp.client.DownloadImageFromInternet;
import com.tharindux.dogfoodapp.client.R;
import com.tharindux.dogfoodapp.client.ui.activities.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    public int RESULT_OK = -1;

    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;

    ImageView imageView7;

    EditText NameFields;

    Uri ImageUri =null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View ProfileView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(ProfileView, savedInstanceState);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        TextView textView24 = getActivity().findViewById(R.id.textView24);


        TextView NameView =  ProfileView.findViewById(R.id.textView17);
        TextView StatusView =  ProfileView.findViewById(R.id.textView30);
        NameFields = ProfileView.findViewById(R.id.editTextText3);
        EditText EmailFields = ProfileView.findViewById(R.id.editTextText2);
        EditText PasswordFields = ProfileView.findViewById(R.id.editTextTextPassword2);
        EditText PhoneFields = ProfileView.findViewById(R.id.editTextPhone);
        EditText Address1 = ProfileView.findViewById(R.id.editTextText4);
        EditText Address2 = ProfileView.findViewById(R.id.editTextText5);


        firebaseFirestore.collection("User").document(textView24.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> UserDetails = (Map<String, Object>) document.getData();
                                for (Map.Entry<String, Object> Details : UserDetails.entrySet()) {
                                    if(Details.getKey().equals("Details")){
                                        Map<String, Object> DetailsList = (Map<String, Object>) Details.getValue();
                                        for (Map.Entry<String, Object> Fields : DetailsList.entrySet()) {
                                            if(Fields.getKey().equals("name")){
                                                NameView.setText(Fields.getValue().toString());
                                                NameFields.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("email")){
                                                EmailFields.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("password")){
                                                PasswordFields.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("status")){
                                                StatusView.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("Phone")){
                                                PhoneFields.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("Address Line 1")){
                                                Address1.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("Address Line 2")){
                                                Address2.setText(Fields.getValue().toString());
                                            }
                                            if(Fields.getKey().equals("ProfilePic")){
                                                if(Fields.getValue().toString().equals("null")){

                                                }else{
                                                    storageReference.child("UserProfile/pic"+Fields.getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            new DownloadImageFromInternet((ImageView) imageView7).execute(uri.toString());

                                                            imageView7.setBackground(getActivity().getResources().getDrawable(R.drawable.profile_image_view));
//                                                            imageView7.setBackground(R.drawable.profile_image_view);
                                                        }
                                                    });
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });









        Button button5 = ProfileView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> User = new HashMap<>();
                User.put("name", NameFields.getText().toString());
                User.put("password", PasswordFields.getText().toString());
                User.put("Phone",PhoneFields.getText().toString());
                User.put("email", textView24.getText().toString());
                User.put("Address Line 1", Address1.getText().toString());
                User.put("Address Line 2", Address2.getText().toString());
                User.put("ProfilePic", NameFields.getText().toString()+".png");
                User.put("status", "Active");

                firebaseFirestore.collection("User").document(textView24.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> userAllDetails = document.getData();
                                userAllDetails.put("Details",User);
                                firebaseFirestore.collection("User").document(textView24.getText().toString())
                                        .set(userAllDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                UploadFile(ImageUri);
                                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });


            }
        });



        imageView7 = ProfileView.findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        Button button6 = ProfileView.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });


    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file"),1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            ImageUri = selectedImageUri;
//            UploadFile(ImageUri);
        }
    }

    private void UploadFile(Uri data){
        StorageReference reference = storageReference.child("UserProfile/pic"+NameFields.getText().toString()+".png");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfileFragment()).commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });

    }
}