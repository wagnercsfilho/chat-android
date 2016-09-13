package app.wagnercsfilho.com.whatsapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.adapter.MessageAdapter;
import app.wagnercsfilho.com.whatsapp.helper.Encrypter;
import app.wagnercsfilho.com.whatsapp.helper.Preference;
import app.wagnercsfilho.com.whatsapp.model.Message;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton buttonSendMessage;
    private EditText editMessage;
    private ListView listChat;
    private MessageAdapter messageAdapter;
    private FirebaseDatabase database;
    private DatabaseReference referenceFrom;
    private DatabaseReference referenceTo;
    private String currentUserId;
    private String userId;
    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String name = getIntent().getExtras().getString("name", "");
        String phoneNumber = getIntent().getExtras().getString("phoneNumber", "");

        currentUserId = new Preference(this).getUserIdEnrypted();
        userId = Encrypter.convertToBase64(phoneNumber);

        database = FirebaseDatabase.getInstance();
        referenceFrom = database.getReference("messages").child(currentUserId).child(userId);
        referenceTo = database.getReference("messages").child(userId).child(currentUserId);

        toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        toolbar.setTitle(name);
        toolbar.setSubtitle(phoneNumber);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listChat = (ListView) findViewById(R.id.listChat);
        buttonSendMessage = (ImageButton) findViewById(R.id.buttonSendMessage);
        editMessage = (EditText) findViewById(R.id.editMessage);

        messages = new ArrayList<>();

        messageAdapter = new MessageAdapter(ChatActivity.this, messages);
        listChat.setAdapter(messageAdapter);

        referenceFrom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    messages.add(message);
                }

                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textMessage = editMessage.getText().toString();

                if (textMessage.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Digite uma mensagem", Toast.LENGTH_SHORT).show();
                } else {
                    String messageKey = referenceFrom.push().getKey();

                    Message message = new Message();
                    message.setId(messageKey);
                    message.setUser(currentUserId);
                    message.setMessage(textMessage);

                    referenceFrom.child(messageKey).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "Falha ao enviar a messagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    referenceTo.child(messageKey).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "Falha ao enviar a messagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
