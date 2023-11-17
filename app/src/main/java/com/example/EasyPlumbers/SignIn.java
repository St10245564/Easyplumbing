package com.example.EasyPlumbers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EasyPlumbers.Model.User;
import com.example.EasyPlumbers.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
EditText edtEmail, edtPassword;
Button btnSignIn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPasswords);
        btnSignIn =(Button) findViewById(R.id.btnSignIn1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tbl_user = database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...............");
                mDialog.show();
                tbl_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String email = edtEmail.getText().toString().replace(".", ",");
                        String enteredPassword = edtPassword.getText().toString();
                        Log.d("Debug", "Email: " + email + ", Password: " + enteredPassword);

                        if (snapshot.child(email).exists()) {
                            mDialog.dismiss();
                            User user = snapshot.child(email).getValue(User.class);
                            user.setEmail(email);
                            if (user != null) {
                                String password = user.getPassword();
                                if (password != null && password.equals(enteredPassword)) {
                                    Toast.makeText(SignIn.this, "Sign in successfully !!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignIn.this, HomeActivity.class);
                                    Common.currentUse = user;
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "Password does not match", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "User data is null!!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist in the database!!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}