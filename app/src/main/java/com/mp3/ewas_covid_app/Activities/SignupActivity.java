package com.mp3.ewas_covid_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;

public class SignupActivity extends AppCompatActivity {

    //Declarations
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilNumber;
    private TextInputLayout tilGender;
    private TextInputLayout tilAge;

    private TextInputLayout tilPassword;
    private TextInputLayout tilRepeatPassword;
    private Button btnSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //instantiate
        tilName = findViewById(R.id.til_name_org);
        tilEmail = findViewById(R.id.til_email_org);
        tilNumber = findViewById(R.id.til_number);
        tilGender = findViewById(R.id.til_gender);
        tilAge = findViewById(R.id.til_age);
        tilPassword = findViewById(R.id.til_password_org);
        tilRepeatPassword = findViewById(R.id.til_repeat_password_org);
        btnSignup = findViewById(R.id.btn_signup);

        //FB
        mAuth = FirebaseAuth.getInstance();


        //Listeners
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tilPassword.getEditText().getText().toString().equals(tilRepeatPassword.getEditText().getText().toString())) {
                    tilRepeatPassword.setError("Password did not match");

                } else {
                    try{
                    mAuth.createUserWithEmailAndPassword(tilEmail.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Try catch for null exception

                                    if (task.isSuccessful()) {
                                        //manageUser(mAuth.getCurrentUser());
                                        createDbInstance(mAuth.getCurrentUser());
                                        Toast.makeText(SignupActivity.this, "Sign Up Succesful!", Toast.LENGTH_SHORT).show();
                                    }
                                    //catch blocks for firebase exceptions and other possible exceptions
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            Toast.makeText(SignupActivity.this, e.getReason(), Toast.LENGTH_SHORT).show();
                                          //  Log.e("Password weak?",e.getErrorCode());
                                        } catch (FirebaseAuthException e) {
                                         //   Log.e("New Uncaught Singup error", e.getErrorCode());
                                        }  catch (Exception e) {

                                          //  Log.e("Error what Exception doe?", e.getMessage());
                                        }
                                    }
                                }
                            }).addOnFailureListener(SignupActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }catch(IllegalArgumentException a){}
                }
            }
        });


    }

    private void manageUser(FirebaseUser user) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(tilName.getEditText().getText().toString())
                .build();

        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();

                } else {
                    Toast.makeText(SignupActivity.this, "Error! Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO
            }
        });
    }

    private void createDbInstance(FirebaseUser user) {
        User um = new User(tilName.getEditText().getText().toString(),
                tilEmail.getEditText().getText().toString(),
                tilNumber.getEditText().getText().toString(),
                tilGender.getEditText().getText().toString(),
                Integer.parseInt(tilAge.getEditText().getText().toString()),
                0);

        String uid = user.getUid();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("ewas-users/users");
        mRef.child(uid).setValue(um);

        //For orgTransaction Array
        mRef.child(uid).child("orgTransactions").setValue(0);

        finish();
    }
}