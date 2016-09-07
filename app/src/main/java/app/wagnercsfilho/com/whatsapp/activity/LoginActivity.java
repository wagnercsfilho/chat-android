package app.wagnercsfilho.com.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.Random;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.helper.Permission;
import app.wagnercsfilho.com.whatsapp.helper.Preference;

public class LoginActivity extends AppCompatActivity {

    EditText edit_pais;
    EditText edit_ddd;
    EditText edit_telefone;
    Button button_cadastrar;

    private String[] permissions = new String[] {
            android.Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permission.setPermission(1, this, permissions);

        edit_pais = (EditText) findViewById(R.id.edit_pais);
        edit_ddd = (EditText) findViewById(R.id.edit_ddd);
        edit_telefone = (EditText) findViewById(R.id.edit_telefone);
        button_cadastrar = (Button) findViewById(R.id.button_cadastrar);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(edit_telefone, simpleMaskTelefone);
        edit_telefone.addTextChangedListener(maskTelefone);

        SimpleMaskFormatter simpleMaskDDD = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskDDD = new MaskTextWatcher(edit_ddd, simpleMaskDDD);
        edit_ddd.addTextChangedListener(maskDDD);

        SimpleMaskFormatter simpleMaskPais = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskPais = new MaskTextWatcher(edit_pais, simpleMaskPais);
        edit_pais.addTextChangedListener(maskPais);

        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edit_pais.getText().toString() +
                        edit_ddd.getText().toString() +
                        edit_telefone.getText().toString();
                phone = phone.replace("+", "");
                phone = phone.replace("-", "");

                // Gerar Token
                Random random = new Random();
                String token = String.valueOf(random.nextInt(9999 - 1000) + 1000);

                Preference preference = new Preference(LoginActivity.this);
                preference.saveUserPreference(phone, token);

                // Send SMS
                phone = "5554";
                Log.i("TOKEN", token);
                boolean isSent = sendSMS("+" + phone, "Confirmation Code: " + token);

                if (isSent) {
                    Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean sendSMS(String phone, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                alertPermissionDenied();
            }
        }
    }

    private void alertPermissionDenied() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Permissões Negadas");
        alert.setMessage("Para utilizar este app é necessario aceitar as permissões");
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.create();
        alert.show();
    }
}
