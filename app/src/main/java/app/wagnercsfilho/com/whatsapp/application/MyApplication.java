package app.wagnercsfilho.com.whatsapp.application;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.wagnercsfilho.com.whatsapp.activity.LoginActivity;
import app.wagnercsfilho.com.whatsapp.activity.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyApplication extends Application {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate() {
        super.onCreate();
        generateKeyHash();
        initializeFacebookSdk();
        checkForUserStateLogin();
    }

    @Override
    public void onTerminate() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onTerminate();
    }

    private void generateKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("app.wagnercsfilho.com.whatsapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void checkForUserStateLogin() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(MyApplication.this, MainActivity.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.d("FIREBASE", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Intent intent = new Intent(MyApplication.this, LoginActivity.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.d("FIREBASE", "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }
}
