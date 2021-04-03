package dev.hellojoy.firestoreproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private TextInputEditText etEmail,etPassword;
    private Button btnLogin;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        db = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);


        if(sharedPreferences.getBoolean("isLogin",false)){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(view -> {
            if (isEmpty(etEmail.getText().toString())){
                showToast("enter email");
            }else if (isEmpty(etPassword.getText().toString())){
                showToast("enter password");
            }else{

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Query query = db.collection("users")
                        .whereEqualTo("email", email).whereEqualTo("password",password);
                query.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            String emailId = documentSnapshot.getString("email");
                            String passwordHash = documentSnapshot.getString("password");
                            if(emailId.equals(email) && passwordHash.equals(password)){
                                showToast("success");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLogin",true);
                                editor.apply();
                                startActivity(new Intent(this,MainActivity.class));
                                break;
                            }
                        }
                    }
                    if(task.getResult().size() == 0 ){
                        showToast("check your email or password");
                    }
                });

            }
        });


    }

    public void goToRegistration(View view) {
        startActivity(new Intent(this,RegistrationActivity.class));
    }

    private boolean isEmpty(String txt){
        return TextUtils.isEmpty(txt);
    }

    private void showToast(String txt){
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }
}