package app.wagnercsfilho.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.model.Contact;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private ArrayList<Contact> contacts;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);

        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (contacts != null) {
            Contact contact = contacts.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_contacts, parent, false);

            TextView textContactName = (TextView) view.findViewById(R.id.textContactName);
            textContactName.setText(contact.getName());

            TextView textContactPhoneNumber = (TextView) view.findViewById(R.id.textContactPhoneNumber);
            textContactPhoneNumber.setText(contact.getPhoneNumber());

            ImageView imageAvatar = (ImageView) view.findViewById(R.id.imageAvatar);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(60)
                    .height(60)
                    .endConfig()
                    .buildRound(String.valueOf(contact.getName().charAt(0)), color);
            imageAvatar.setImageDrawable(drawable);


        }

        return view;
    }

}
