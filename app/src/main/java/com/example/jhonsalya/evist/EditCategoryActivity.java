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

public class EditCategoryActivity extends AppCompatActivity {

    private EditText editName;
    private String post_key = null;

    //for opening gallery on android phone
    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        post_key = getIntent().getExtras().getString("PostId");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
        storageReference = FirebaseStorage.getInstance().getReference();

        editName = (EditText) findViewById(R.id.edit_category_name);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        databaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_name = (String) dataSnapshot.child("Name").getValue();

                editName.setText(post_name, TextView.BufferType.EDITABLE);
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
        StorageReference filePath = storageReference.child("EventCategory").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadurl = taskSnapshot.getDownloadUrl();
                final DatabaseReference newPost = databaseReference.child(post_key);
                newPost.child("image").setValue(downloadurl.toString());

            }
        });
    }

    public void editCategoryButtonClicked(View view){
        final ProgressDialog mDialog = new ProgressDialog(EditCategoryActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        if(uri != null){
            changeImage();
        }

        final String nameValue = editName.getText().toString().trim();

        if(!TextUtils.isEmpty(nameValue)){
            final DatabaseReference newPost = databaseReference.child(post_key);

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("Name").setValue(nameValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mDialog.dismiss();
                                Toast.makeText(EditCategoryActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                                Intent mainActivityIntent = new Intent(EditCategoryActivity.this, CategoryActivity.class);
                                startActivity(mainActivityIntent);
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
