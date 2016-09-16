package app.wagnercsfilho.com.whatsapp.service;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import app.wagnercsfilho.com.whatsapp.helper.Encrypter;
import app.wagnercsfilho.com.whatsapp.helper.Preference;
import app.wagnercsfilho.com.whatsapp.model.Contact;
import app.wagnercsfilho.com.whatsapp.model.User;

public class ContactService {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Context context;
    private final String USER_REFERENCE = "contacts";

    public ContactService(Context context) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance();
        this.reference = this.database.getReference(USER_REFERENCE);
    }

    public void addContact(final String userId) {
        final String userPhoneId = Encrypter.convertToBase64(userId);
        database.getReference("users").child(userPhoneId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap<String, String> userData = new Preference(context).getPhoneUserData();
                    String userContactId = Encrypter.convertToBase64(userData.get(Preference.KEY_PHONE));
                    User user = dataSnapshot.getValue(User.class);
                    Contact contact = new Contact();
                    contact.setId(userContactId);
                    contact.setName(user.getName());
                    contact.setPhoneNumber(user.getPhoneNumber());
                    reference
                            .child(userContactId)
                            .child(userPhoneId)
                            .setValue(contact);
                    Toast.makeText(context, "Contato adicionado com sucesso", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Contato não encontrado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
