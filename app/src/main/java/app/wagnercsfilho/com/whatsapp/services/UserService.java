package app.wagnercsfilho.com.whatsapp.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.wagnercsfilho.com.whatsapp.model.User;

public class UserService {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private final String USER_REFERENCE = "users";

    public UserService() {
        this.database = FirebaseDatabase.getInstance();
        this.reference = this.database.getReference(USER_REFERENCE);
    }

    public void create(User user) {
        this.reference.child(user.getId()).setValue(user);
    }

}
