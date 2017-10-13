package com.example.tnrkd.colormakey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tnrkd.colormakey.dto.Color;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XNOTE on 2017-09-24.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    private static GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private static FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private final String TAG = "LoginActivity";

    Thread loadingThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        Global.colors = new ArrayList<>();
        findViewById(R.id.sign_in_button).setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }else {

        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {

            // 로그인한 사용자의 email, id값 Global에 저장
            Global.userEmail = user.getEmail();
            Global.userName = user.getDisplayName();
            Global.userUID = user.getUid();

            // Firebase DB에 사용자 정보 저장
            mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(Global.userUID).child("name");
            mDatabase.setValue(Global.userName);

            // 사용자의 palette 정보 로딩
            mDatabase = FirebaseDatabase.getInstance().getReference().child("palette");
            loadingThread = new Thread() {
                @Override
                public void run() {
                    ValueEventListener colorListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // DB에서 모든 user 목록을 가져온다.
                            HashMap<String, Object> list = (HashMap<String, Object>)dataSnapshot.getValue();
                            ArrayList<HashMap<String, Object>> colorList = (ArrayList<HashMap<String, Object>>)list.get(Global.userUID);

                            Global.colors.clear(); // colors 초기화 하고 다시 add
                            if(colorList != null) {
                                for(HashMap<String, Object> hashMap : colorList) {
                                    Color color = new Color();
                                    color.setRgbcode((String)hashMap.get("rgbcode"));
                                    color.setHexcode((String)hashMap.get("hexcode"));
                                    Global.colors.add(color);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        }
                    };
                    mDatabase.addListenerForSingleValueEvent(colorListener);
                }
            };
            loadingThread.start();

            Intent intent = new Intent(this, HomeMenuActivity.class);
            startActivity(intent);
            Global.logoutFlag = false;
        }else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static void logout(AppCompatActivity activity) {

        Global.logoutFlag = true;
        Global.activity = activity;

        final GoogleApiClient.ConnectionCallbacks gaccc = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if(!Global.logoutFlag) {

                }else {
                    mAuth.signOut();
                    if(mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    if(Global.activity != null) {
                                        Global.activity.finish();
                                        Global.activity = null;
                                        Global.logoutFlag = false;
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        };
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(gaccc);
    }
}