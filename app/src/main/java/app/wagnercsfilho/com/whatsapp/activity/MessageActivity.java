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
import app.wagnercsfilho.com.whatsapp.helper.Preference;
import app.wagnercsfilho.com.whatsapp.model.Chat;
import app.wagnercsfilho.com.whatsapp.model.Message;

public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton buttonSendMessage;
    private EditText editMessage;
    private ListView listMessages;
    private MessageAdapter messageAdapter;
    private FirebaseDatabase database;
    private DatabaseReference referenceFrom;
    private DatabaseReference referenceTo;
    private DatabaseReference referenceChatFrom;
    private DatabaseReference referenceChatTo;
    private String currentUserId;
    private String userId;
    private String userName;
    private ArrayList<Message> messages;
    private ValueEventListener valueEventListenerMessages;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();

        userName = extras.getString("userName", "");
        userId = extras.getString("userId", "");

        preference = new Preference(this);

        currentUserId = preference.getUserIdEnrypted();

        database = FirebaseDatabase.getInstance();
        referenceFrom = database.getReference("messages").child(currentUserId).child(userId);
        referenceTo = database.getReference("messages").child(userId).child(currentUserId);
        referenceChatFrom = database.getReference("chats").child(currentUserId).child(userId);
        referenceChatTo = database.getReference("chats").child(userId).child(currentUserId);

        toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        toolbar.setTitle(userName);
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

        listMessages = (ListView) findViewById(R.id.listMessages);
        listMessages.setDivider(null);
        listMessages.setDividerHeight(0);
        buttonSendMessage = (ImageButton) findViewById(R.id.buttonSendMessage);
        editMessage = (EditText) findViewById(R.id.editMessage);

        messages = new ArrayList<>();

        messageAdapter = new MessageAdapter(MessageActivity.this, messages);
        listMessages.setAdapter(messageAdapter);

        valueEventListenerMessages = new ValueEventListener() {
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
        };

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editMessage.getText().toString();

                if (textMessage.isEmpty()) {
                    Toast.makeText(MessageActivity.this, "Digite uma mensagem", Toast.LENGTH_SHORT).show();
                } else {
                    createMessage(textMessage);
                    createChat(userName, textMessage);
                    editMessage.setText("");
                }
            }
        });
    }

    private void createMessage(String textMessage) {
        String messageKey;
        OnCompleteListener onCompleteListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MessageActivity.this, "Falha ao enviar a messagem", Toast.LENGTH_SHORT).show();
                }
            }
        };

        messageKey = referenceFrom.push().getKey();
        Message message = new Message();
        message.setId(messageKey);
        message.setUser(currentUserId);
        message.setMessage(textMessage);

        referenceFrom.child(messageKey).setValue(message).addOnCompleteListener(onCompleteListener);

        messageKey = referenceTo.push().getKey();
        referenceTo.child(messageKey).setValue(message).addOnCompleteListener(onCompleteListener);
    }

    private void createChat(String name, String textMessage) {
        Chat chat;

        OnCompleteListener onCompleteListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MessageActivity.this, "Falha ao enviar a messagem", Toast.LENGTH_SHORT).show();
                }
            }
        };

        chat = new Chat();
        chat.setUserId(userId);
        chat.setName(name);
        chat.setMessage(textMessage);
        referenceChatFrom.setValue(chat).addOnCompleteListener(onCompleteListener);

        chat = new Chat();
        chat.setUserId(currentUserId);
        chat.setName(preference.getUserName());
        chat.setMessage(textMessage);
        referenceChatTo.setValue(chat).addOnCompleteListener(onCompleteListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        referenceFrom.addValueEventListener(valueEventListenerMessages);
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceFrom.removeEventListener(valueEventListenerMessages);
    }
}
