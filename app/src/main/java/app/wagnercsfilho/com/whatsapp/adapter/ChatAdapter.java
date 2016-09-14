package app.wagnercsfilho.com.whatsapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import app.wagnercsfilho.com.whatsapp.R;
import app.wagnercsfilho.com.whatsapp.model.Chat;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private ArrayList<Chat> chats;
    private Context context;
    OnItemClickListener mItemClickListner;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView editName;
        public TextView editMessage;
        public ImageView imageAvatar;

        public MyViewHolder(View view) {
            super(view);

            imageAvatar = (ImageView) view.findViewById(R.id.imageAvatar);
            editName = (TextView) view.findViewById(R.id.editName);
            editMessage = (TextView) view.findViewById(R.id.editMessage);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListner != null) {
                mItemClickListner.onItemClick(view, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListner) {
        this.mItemClickListner = mItemClickListner;
    }

    public ChatAdapter(ArrayList<Chat> chats, Context context) {
        this.context = context;
        this.chats = chats;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_chat, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.editName.setText(chat.getName());
        holder.editMessage.setText(chat.getMessage());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)
                .height(60)
                .endConfig()
                .buildRound(String.valueOf(chat.getName().charAt(0)), color);
        holder.imageAvatar.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


}
