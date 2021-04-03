package dev.hellojoy.firestoreproject_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logOut(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}