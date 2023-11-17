package com.example.EasyPlumbers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EasyPlumbers.Model.User;
import com.example.EasyPlumbers.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    EditText edtPhone, edtPassword, edtName, edtEmail;
    Button btnSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);

        btnSignUp =(Button) findViewById(R.id.btnSignUp1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tbl_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validation checks
                if (validateInputs()) {
                    ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please wait...............");
                    mDialog.show();

                    String phoneNumber = edtPhone.getText().toString();

                    tbl_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(phoneNumber).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "The phone number already exists!", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(
                                        edtName.getText().toString(),
                                        edtPassword.getText().toString(),
                                        edtEmail.getText().toString()
                                );
                                tbl_user.child(phoneNumber).setValue(user);
                                Toast.makeText(SignUp.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                }
            }
        });
    }

    private boolean validateInputs() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Check if email is in correct format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is strong (you can add your own criteria for a strong password)
        if (password.length() < 8) {
            Toast.makeText(this, "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Add more validation checks if needed

        return true;
    }
}