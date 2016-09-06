package app.wagnercsfilho.com.whatsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.helper.Preference;

public class ValidadorActivity extends AppCompatActivity {

    private EditText editToken;
    private Button buttonValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        editToken = (EditText) findViewById(R.id.edit_token);
        buttonValidate = (Button) findViewById(R.id.button_validate);

        SimpleMaskFormatter simpleMaskToken = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskTextToken = new MaskTextWatcher(editToken, simpleMaskToken);
        editToken.addTextChangedListener(maskTextToken);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preference preference = new Preference(ValidadorActivity.this);
                HashMap<String, String> userData = preference.getUserData();

                String userToken = userData.get(Preference.KEY_TOKEN);
                String inputToken = editToken.getText().toString();

                if (inputToken.equals(userToken)) {
                    Toast.makeText(ValidadorActivity.this, "Token válido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ValidadorActivity.this, "Token inválido", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
