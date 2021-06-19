package com.example.sparks_integration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private LoginButton b;
    private CallbackManager callbackManager;

    private TextView textView,connect,textVw;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView=(TextView)findViewById(R.id.txt);
        b = (LoginButton) findViewById(R.id.login_button);
        textView=(TextView)findViewById(R.id.txt);
        textVw=(TextView)findViewById(R.id.email);
        imageView=(ImageView)findViewById(R.id.img);
        connect=(TextView)findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,google.class);
                startActivity(intent);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        b.setPermissions(Arrays.asList("email", "user_birthday"));

        b.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name=object.getString("name");
                    String id=object.getString("id");
                    String Email=object.getString("email");
                    Log.d("************",id);
                    textView.setText(name);
                    textVw.setText(Email);
                    Picasso.get().load("https://graph.facebook.com/"+id+"/picture?type=large").into(imageView);
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });
        Bundle bundle=new Bundle();
        bundle.putString("fields","email,name,id");

        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }
    AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken==null){
                LoginManager.getInstance().logOut();
                textView.setText("");
                textVw.setText("");
                imageView.setImageDrawable(null);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}