package com.example.jhonsalya.evist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditUserActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;
    private EditText editEmail;
    private EditText editName;
    private EditText editPhone;

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("UserProfile");

        imageButton = (ImageButton) findViewById(R.id.edit_user_image);
        editName = (EditText) findViewById(R.id.edit_user_name);
        editEmail = (EditText) findViewById(R.id.edit_user_email);
        editPhone = (EditText) findViewById(R.id.edit_user_phone);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        databaseReference.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_image = (String) dataSnapshot.child("image").getValue();
                String user_name = (String) dataSnapshot.child("name").getValue();
                String user_email = (String) dataSnapshot.child("email").getValue();
                String user_phone = (String) dataSnapshot.child("phone").getValue();

                Picasso.with(EditUserActivity.this).load(user_image).into(imageButton);
                editName.setText(user_name, TextView.BufferType.EDITABLE);
                editEmail.setText(user_email);
                editPhone.setText(user_phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void imageClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton.setImageURI(uri);
        }
    }

    private void changeImage(){
        StorageReference filePath = storageReference.child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadurl = taskSnapshot.getDownloadUrl();
                final DatabaseReference newPost = databaseReference.child(mCurrentUser.getUid());
                newPost.child("image").setValue(downloadurl.toString());
            }
        });

    }

    public void editUserButtonClicked(View view){
        final ProgressDialog mDialog = new ProgressDialog(EditUserActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        if(uri != null){
            changeImage();
        }

        final String nameValue = editName.getText().toString().trim();
        final String phoneValue = editPhone.getText().toString().trim();

        if(!TextUtils.isEmpty(nameValue) &&
                !TextUtils.isEmpty(phoneValue)){

            final DatabaseReference newPost = databaseReference.child(mCurrentUser.getUid());

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("name").setValue(nameValue);
                    newPost.child("phone").setValue(phoneValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mDialog.dismiss();
                                Toast.makeText(EditUserActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                                Intent UserProfileActivity = new Intent(EditUserActivity.this, UserProfileActivity.class);
                                UserProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(UserProfileActivity);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
