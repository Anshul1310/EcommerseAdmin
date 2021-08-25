package com.anstudios.ecommerseadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterScreen extends AppCompatActivity {

    private EditText name, email, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Please wait while we register you.");
        progressDialog.setCanceledOnTouchOutside(false);
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_mail);
        password = findViewById(R.id.register_password);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }

    public void open_login_screen_from_register_screen(View view) {
        startActivity(new Intent(RegisterScreen.this, LoginActivity.class));
    }

    public void sign_up_user(View view) {
        if (!email.getText().toString().equals("") && !password.getText().toString().equals("") && !name.getText().toString().equals("")) {
            progressDialog.show();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.show();
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("name", name.getText().toString());
                            hashMap.put("email", email.getText().toString());
                            hashMap.put("password", password.getText().toString());
                            FirebaseDatabase.getInstance().getReference("admins").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    SplashScreen.sharedPreferences.edit().putString("name", hashMap.get("name")).apply();
                                    SplashScreen.sharedPreferences.edit().putString("email", hashMap.get("email")).apply();
                                    progressDialog.cancel();
                                    Toast.makeText(RegisterScreen.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterScreen.this, MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(RegisterScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.cancel();
                    Toast.makeText(RegisterScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Blank field can't be processed.", Toast.LENGTH_SHORT).show();
        }

    }
}