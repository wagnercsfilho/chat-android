package app.wagnercsfilho.com.whatsapp.service;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import app.wagnercsfilho.com.whatsapp.activity.MainActivity;
import app.wagnercsfilho.com.whatsapp.model.User;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AuthService {

    private FirebaseAuth firebaseAuth;
    private CallbackManager fbCallbackManager;
    private AuthCredential fbAuthCredential;
    private LoginButton loginButton;
    private Context context;

    public AuthService(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Boolean isLogged() {
        if (firebaseAuth.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void initializeFacebookLoginButton(LoginButton loginButton) {
        fbCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FIREBASE", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FIREBASE", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FIREBASE", "facebook:onError", error);
            }
        });
    }

    public void signUpUserWithEmailAndPassword(final User user) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.setId(task.getResult().getUser().getUid());
                    UserService userService = new UserService();
                    userService.create(user);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } else {
                    task.getException().printStackTrace();
                    Toast.makeText(context, "Erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    public void signInWithEmailAndPasswordLogin(User user) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth
                .signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(context, "Usuário/Senha inválido!", Toast.LENGTH_LONG).show();
                        }

                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void logOut() {
        firebaseAuth.signOut();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FIREBASE", "handleFacebookAccessToken:" + token);

        fbAuthCredential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(fbAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.i("FACEBOOK",  "logado");
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

}
