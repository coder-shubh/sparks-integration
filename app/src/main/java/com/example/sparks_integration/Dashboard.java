package com.example.sparks_integration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    ArrayList<String> array_list = new ArrayList<String>();
    GoogleSignInClient mGoogleSignInClient;
    ImageView profile_pic;
    TextView Name,Email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Chip logout = (Chip) findViewById(R.id.bt_logout);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        Name = (TextView) findViewById(R.id.name);
        Email = (TextView) findViewById(R.id.email);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "user Log_out....", Toast.LENGTH_SHORT).show();

                signOut();
            }
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();

            String personEmail = acct.getEmail();
            //String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            //array_list.add("name:"+personName+"\n"+"Email:"+personEmail+"\n"+"photo:"+personPhoto);
            Name.setText(personName);
            Email.setText(personEmail);
            Glide.with(this).load(String.valueOf(personPhoto)).into(profile_pic);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(getApplicationContext(), "signout", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

}
