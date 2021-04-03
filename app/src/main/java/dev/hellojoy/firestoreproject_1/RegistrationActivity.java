package dev.hellojoy.firestoreproject_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText etName,etEmail,etPassword;
    private Button btnSubmit;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSubmit = findViewById(R.id.btn_submit);

        db = FirebaseFirestore.getInstance();


        btnSubmit.setOnClickListener(view -> {
            if (isEmpty(etName.getText().toString())){
                showToast("enter name");
            }else if (isEmpty(etEmail.getText().toString())){
                showToast("enter email");
            }else if (isEmpty(etPassword.getText().toString())){
                showToast("enter password");
            }else{

                String email = etEmail.getText().toString();

                Query query = db.collection("users")
                        .whereEqualTo("email", email);
                query.get().addOnCompleteListener(task -> {
                    boolean isExist = false;
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            String emailId = documentSnapshot.getString("email");
                            if(emailId.equals(email)){
                                showToast("email id already exist");
                                isExist = true;
                                break;
                            }
                        }
                    }
                    if(task.getResult().size() == 0 ){
                        Map<String,Object> map = new HashMap();
                        map.put("name",etName.getText().toString());
                        map.put("email",etEmail.getText().toString());
                        map.put("password",etPassword.getText().toString());

                        db.collection("users").add(map).addOnSuccessListener((OnSuccessListener) o -> {
                            showToast("users added");
                        }).addOnFailureListener(e -> {
                            showToast("something went wrong");
                        });
                    }
                });

            }
        });




    }

    public void goToLogin(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    private boolean isEmpty(String txt){
        return TextUtils.isEmpty(txt);
    }

    private void showToast(String txt){
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }
}