package com.example.jhonsalya.evist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPassword = (EditText) findViewById(R.id.editPassword);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void registerButtonClicked(View view){
        /*Toast.makeText(RegisterActivity.this, "Please Fill All Form", Toast.LENGTH_LONG).show();
        final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();*/

        final String nameValue = editName.getText().toString().trim();
        final String emailValue = editEmail.getText().toString().trim();
        final String phoneValue = editPhone.getText().toString().trim();
        final String passwordValue = editPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(nameValue) && !TextUtils.isEmpty(emailValue) && !TextUtils.isEmpty(phoneValue)  && !TextUtils.isEmpty(passwordValue)){
            mAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference newPost = mDatabase.child(user_id);
                        newPost.child("name").setValue(nameValue);
                        newPost.child("phone").setValue(phoneValue);
                        newPost.child("password").setValue(passwordValue);
                        newPost.child("email").setValue(emailValue);
                        newPost.child("image").setValue("default");
                        newPost.child("status").setValue("0");

                        Toast.makeText(RegisterActivity.this, "Register Complete, Please Login", Toast.LENGTH_LONG).show();
                        Intent loginActivityIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginActivityIntent);
                    }
                }
            });

            /*final DatabaseReference newPost = databaseReference.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("Name").setValue(nameValue);
                    newPost.child("Phone").setValue(phoneValue);
                    newPost.child("Password").setValue(passwordValue);
                    newPost.child("Email").setValue(emailValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                                Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainActivityIntent);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        }
    }
}
