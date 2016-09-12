package app.wagnercsfilho.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.helper.Preference;
import app.wagnercsfilho.com.whatsapp.model.Message;

/**
 * Created by Wagner CS Filho on 11/09/2016.
 */
public class MessageAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Message> messages;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);

        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (messages != null) {

            Message message = messages.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (message.getUser().equals(new Preference(context).getUserIdEnrypted())) {
                inflater.inflate(R.layout.list_item_chat_from, parent, false);
            } else {
                inflater.inflate(R.layout.list_item_chat_to, parent, false);
            }


        }

        return view;
    }
}
