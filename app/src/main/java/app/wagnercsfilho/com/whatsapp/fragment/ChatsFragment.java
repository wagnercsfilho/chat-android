package app.wagnercsfilho.com.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.activity.MessageActivity;
import app.wagnercsfilho.com.whatsapp.adapter.ChatAdapter;
import app.wagnercsfilho.com.whatsapp.helper.Preference;
import app.wagnercsfilho.com.whatsapp.model.Chat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {


    private RecyclerView listChat;
    private ArrayList<Chat> chats;
    private ChatAdapter adapter;
    private ValueEventListener valueEventListenerChats;
    private DatabaseReference referenceChat;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        listChat = (RecyclerView) view.findViewById(R.id.listChat);

        Preference preference = new Preference(getContext());
        String currentUserId = preference.getUserIdEnrypted();
        chats = new ArrayList<>();

        listChat.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatAdapter(chats, getContext());
        listChat.setAdapter(adapter);

        valueEventListenerChats = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chats.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    chats.add(chat);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        adapter.SetOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Chat chat = chats.get(position);

                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("userId", chat.getUserId());
                intent.putExtra("userName", chat.getName());
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        referenceChat = database.getReference("chats").child(currentUserId);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        referenceChat.removeEventListener(valueEventListenerChats);
    }

    @Override
    public void onStart() {
        super.onStart();
        referenceChat.addValueEventListener(valueEventListenerChats);
    }
}
