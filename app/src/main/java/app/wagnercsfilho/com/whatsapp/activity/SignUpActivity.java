package app.wagnercsfilho.com.whatsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.model.User;
import app.wagnercsfilho.com.whatsapp.services.AuthService;

public class SignUpActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonSignUp;

    private User user;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        authService = new AuthService(this);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User();
                user.setName(editName.getText().toString());
                user.setEmail(editEmail.getText().toString());
                user.setPassword(editPassword.getText().toString());
                authService.signUpUserWithEmailAndPassword(user);
            }
        });
    }

}
