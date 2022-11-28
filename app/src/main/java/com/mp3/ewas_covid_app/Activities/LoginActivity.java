package com.mp3.ewas_covid_app.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mp3.ewas_covid_app.Models.User;
import com.mp3.ewas_covid_app.R;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private Button signUpBTN;
    private Button signUpOrgBTN;
    private Button signInBTN;
    private SignInButton btnGoogleAuth;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    String path = "ewas-users/users/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUpBTN = findViewById(R.id.btn_signup);
        signUpOrgBTN = findViewById(R.id.btn_signup_org);
        signInBTN = findViewById(R.id.btn_signin);
        tilEmail = findViewById(R.id.til_email_org);
        tilPassword = findViewById(R.id.til_password_org);
        btnGoogleAuth = findViewById(R.id.btn_google_auth);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //OnClick Listeners
        signUpBTN.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        signUpOrgBTN.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, OrgSignupActivity.class));
        });

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mAuth.signInWithEmailAndPassword(tilEmail.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //If not, Account type is ORGANIZATION
                                        checkUserExists();

                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthException e) {
                                            Toast.makeText(LoginActivity.this, getMessageFromErrorCode(e.getErrorCode()), Toast.LENGTH_SHORT).show();
                                            //  Log.e("Password weak?",e.getErrorCode());
                                        } catch (Exception e) {
                                            //  Log.e("Error what Exception doe?", e.getMessage());
                                        }
                                    }
                                }
                            }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }catch (IllegalArgumentException e){
                    Toast.makeText(LoginActivity.this, "Missing Fields! Input all info", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

                Intent googleSignInIntent = mSignInClient.getSignInIntent();
                googleAuthLauncher.launch(googleSignInIntent);
            }
        });


    }
    ActivityResultLauncher<Intent> googleAuthLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                //TODO
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                checkUserExists();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getMessageFromErrorCode(String errorCode) {
        switch (errorCode) {
            case "ERROR_EMAIL_ALREADY_IN_USE":
            case "account-exists-with-different-credential":
            case "email-already-in-use":
                return "Email already used. Go to login page.";

            case "ERROR_WRONG_PASSWORD":
            case "wrong-password":
                return "Wrong email/password combination.";

            case "ERROR_USER_NOT_FOUND":
            case "user-not-found":
                return "No user found with this email.";

            case "ERROR_USER_DISABLED":
            case "user-disabled":
                return "User disabled.";

            case "ERROR_TOO_MANY_REQUESTS":
                return "Too many requests to log into this account.";

            case "ERROR_OPERATION_NOT_ALLOWED":
            case "operation-not-allowed":
                return "Server error, please try again later.";

            case "ERROR_INVALID_EMAIL":
            case "invalid-email":
                return "Email address is invalid.";

            default:
                return "Login failed. Please try again.";

        }
    }

    public void checkUserExists(){
        //Check if user exists - to know usertype
        mUser = mAuth.getCurrentUser();
        path = "ewas-users/users/" + mUser.getUid();
        mRef = FirebaseDatabase.getInstance().getReference(path);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    User um = snapshot.getValue(User.class);
                    String test = um.getName();

                    //start to new activity - USER type
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } catch(NullPointerException e){
                    //Means that account logged in is not USER but a ORGANIZATION ACCOUNT
                    startActivity(new Intent(LoginActivity.this, OrgMain.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}