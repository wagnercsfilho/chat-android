package app.wagnercsfilho.com.whatsapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.adapter.TabAdapter;
import app.wagnercsfilho.com.whatsapp.helper.SlidingTabLayout;
import app.wagnercsfilho.com.whatsapp.services.AuthService;

public class MainActivity extends AppCompatActivity {

    private AuthService authService;
    private Toolbar toolbar;
    private Button buttonLogout;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authService = new AuthService(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Whatsapp");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLogout:
                authService.logOut();
                return true;
            case R.id.itemAddPerson:
                openDialogAddPerson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void openDialogAddPerson() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Novo contato");
        alert.setMessage("E-mail do usuário");

        final EditText editText = new EditText(this);
        alert.setView(editText);

        alert.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String personEmail = editText.getText().toString();

                if (personEmail.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Digite o email do contato", Toast.LENGTH_LONG).show();
                } else {
                    
                }

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.create();
        alert.show();

    }
}
