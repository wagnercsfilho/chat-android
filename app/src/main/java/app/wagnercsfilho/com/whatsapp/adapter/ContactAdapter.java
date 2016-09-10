package app.wagnercsfilho.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.model.Contact;

/**
 * Created by Wagner CS Filho on 10/09/2016.
 */
public class ContactAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Contact> contacts;

    public ContactAdapter(Context context,  ArrayList<Contact> contacts) {
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

            view = inflater.inflate(R.layout.list_contacts, parent);

            TextView textView = (TextView) view.findViewById(R.id.textContactName);
            textView.setText(contact.getName());
        }

        return view;
    }
}
