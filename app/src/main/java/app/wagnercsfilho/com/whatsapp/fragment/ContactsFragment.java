package app.wagnercsfilho.com.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.activity.MessageActivity;
import app.wagnercsfilho.com.whatsapp.adapter.ContactAdapter;
import app.wagnercsfilho.com.whatsapp.helper.Preference;
import app.wagnercsfilho.com.whatsapp.model.Contact;

public class ContactsFragment extends Fragment {

    ListView listContact;
    ContactAdapter adapter;
    ArrayList<Contact> contacts;
    DatabaseReference contactsReference;
    ValueEventListener contactsEventListener;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        listContact = (ListView) view.findViewById(R.id.listContacts);

        contacts = new ArrayList<>();
        adapter = new ContactAdapter(
                getActivity(),
                contacts
        );
        listContact.setAdapter(adapter);

        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact contact = contacts.get(position);
                String userName = contact.getName();
                String userId = contact.getId();

                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        String currentUserId = new Preference(getContext()).getUserIdEnrypted();
        contactsReference = FirebaseDatabase.getInstance()
                .getReference("contacts")
                .child(currentUserId);
        contactsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Contact contact = data.getValue(Contact.class);
                    contacts.add(contact);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        contactsReference.child(new Preference(getContext()).getUserIdEnrypted());


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        contactsReference.addValueEventListener(contactsEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        contactsReference.removeEventListener(contactsEventListener);
    }
}
