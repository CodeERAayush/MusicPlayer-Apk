package com.codeeraayush.firestoreimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {

    private StorageTask mUploadTask;

    private static final int PICK_IMAGE_REQUEST=1;
    private Button chooser,upload;
    private ImageView imageView;
    private Uri imageUri;
    private EditText Title,Caption;
    ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView mProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setUpActionBar();

        Title=findViewById(R.id.ghatNumber);
        Caption=findViewById(R.id.Caption);
        progressBar=findViewById(R.id.progress);


        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        mProfile=findViewById(R.id.profile_image);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadActivity.this,ViewImage.class));
                finish();
            }
        });


        imageView=findViewById(R.id.uploadedImg);
        chooser=findViewById(R.id.chooseImg);
        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        upload=findViewById(R.id.mUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUploadTask!=null&&mUploadTask.isInProgress()){
                    Toast.makeText(UploadActivity.this, "Upload Already in progress!", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadToDb();
                }
            }
        });

    }


    //for implicit intent to open gallery

    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    //for the result picked from gallery

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }


    //give the extension of image or file

    private String getFileExtension(Uri uri){

        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }

    private void uploadToDb() {
if(imageUri!=null){
    StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
    mUploadTask=ref.putFile(imageUri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);

//                    Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
//                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String photoStringLink = uri.toString();
//                        }
//                    });

                    Task<Uri> task=taskSnapshot.getStorage().getDownloadUrl();
                    while(!task.isSuccessful());
                    String uploadPdfurl=""+task.getResult();


                    Toast.makeText(UploadActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    Upload upload=new Upload(Title.getText().toString().trim(),
                            Caption.getText().toString().trim(),
                            uploadPdfurl);
                    String uploadId=databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(upload);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            double per=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
            progressBar.setProgress((int)per);
        }
    });
}else{
    Toast.makeText(UploadActivity.this, "Please select an Image!", Toast.LENGTH_SHORT).show();
}
    }





    private void setUpActionBar() {

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navBar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Lifeline,R.string.Lifeline);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}