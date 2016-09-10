package app.wagnercsfilho.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import app.wagnercsfilho.com.whatsapp.R;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String name = getIntent().getExtras().getString("name", "");
        String phoneNumber = getIntent().getExtras().getString("phoneNumber", "");

        toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        toolbar.setTitle(name);
        toolbar.setSubtitle(phoneNumber);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
    }
}
