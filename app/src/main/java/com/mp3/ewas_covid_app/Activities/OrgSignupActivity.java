package com.mp3.ewas_covid_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mp3.ewas_covid_app.Models.Organization;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;

public class OrgSignupActivity extends AppCompatActivity {

    //Declarations
    private TextInputLayout tilNameOrg;
    private TextInputLayout tilEmailOrg;
    private TextInputLayout tilAddressOrg;
    private Button signupOrgBTN;

    private TextInputLayout tilPasswordOrg;
    private TextInputLayout tilRepeatPasswordOrg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_signup);

        //Instantiate
        tilNameOrg = findViewById(R.id.til_name_org);
        tilEmailOrg = findViewById(R.id.til_email_org);
        tilAddressOrg = findViewById(R.id.til_address_org);
        signupOrgBTN = findViewById(R.id.btn_signup_act_org);
        tilPasswordOrg = findViewById(R.id.til_password_org);
        tilRepeatPasswordOrg = findViewById(R.id.til_repeat_password_org);

        //FB
        mAuth = FirebaseAuth.getInstance();

        //Listeners
        signupOrgBTN.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                if (!tilPasswordOrg.getEditText().getText().toString().equals(tilRepeatPasswordOrg.getEditText().getText().toString())) {
                    tilRepeatPasswordOrg.setError("Password did not match");

                } else {
                    try{
                        mAuth.createUserWithEmailAndPassword(tilEmailOrg.getEditText().getText().toString(), tilPasswordOrg.getEditText().getText().toString())
                                .addOnCompleteListener(OrgSignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //Try catch for null exception

                                        if (task.isSuccessful()) {
                                            //manageUser(mAuth.getCurrentUser());
                                            createDbInstance(mAuth.getCurrentUser());
                                            Toast.makeText(OrgSignupActivity.this, "Sign Up Succesful!", Toast.LENGTH_SHORT).show();
                                        }
                                        //catch blocks for firebase exceptions and other possible exceptions
                                        if (!task.isSuccessful()) {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthWeakPasswordException e) {
                                                Toast.makeText(OrgSignupActivity.this, e.getReason(), Toast.LENGTH_SHORT).show();
                                                //  Log.e("Password weak?",e.getErrorCode());
                                            } catch (FirebaseAuthException e) {
                                                //   Log.e("New Uncaught Singup error", e.getErrorCode());
                                            }  catch (Exception e) {

                                                //  Log.e("Error what Exception doe?", e.getMessage());
                                            }
                                        }
                                    }
                                }).addOnFailureListener(OrgSignupActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(OrgSignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }catch(IllegalArgumentException a){}
                }
            }
        });
    }

    private void createDbInstance(FirebaseUser user) {
        Organization um = new Organization(
                tilNameOrg.getEditText().getText().toString(),
                tilAddressOrg.getEditText().getText().toString(),
                tilEmailOrg.getEditText().getText().toString());

        String uid = user.getUid();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("ewas-users/organizations");
        mRef.child(uid).setValue(um);

        //For orgTransaction Array
        mRef.child(uid).child("userTransactions").setValue(0);

        //For AccountType Clarification
        mRef.child(uid).child("accountType").setValue("org");

        finish();
    }
}