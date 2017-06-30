package hagai.edu.firebasedatabase;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hagai.edu.firebasedatabase.models.ChatMessage;
import hagai.edu.firebasedatabase.models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsappFragment extends Fragment {
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whatsapp, container, false);
        unbinder = ButterKnife.bind(this, view);

        //TODO: discuss sharedInstance.
        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setupRecycler();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSend)
    public void onBtnSendClicked() {
        String text = etMessage.getText().toString();
        if(TextUtils.isEmpty(text))return;

        ChatMessage chat = new ChatMessage(new User(user), text);

        mDatabase.getReference("BetterChat").push().setValue(chat);

        etMessage.setText(null);
    }





    private void readFromDb(){
        //1) get a ref to the table.
        DatabaseReference chatRef = mDatabase.getReference("Chat");
        //final List<String> items = new ArrayList<>();
        //2) add a listener to the table



        //get the table at the beginning
        //AND each time the data changes - GET ALL THE TABLE AGAIN.
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> rows = dataSnapshot.getChildren();

                for (DataSnapshot row : rows) {
                    String text = row.getValue(String.class);
                   // items.add(text);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void readOnce(){
        //1) reference to the table:
        DatabaseReference chatTableRef = mDatabase.getReference("Chat");


        //2) add a listener
        chatTableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tableSnapshot) {
                for (DataSnapshot row : tableSnapshot.getChildren()) {
                    String message = row.getValue(String.class);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void readIncremental(){
        //get a reference to the table
        DatabaseReference chatTableRef = mDatabase.getReference("Chat");

        //2) add a listener
        chatTableRef .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot rowDataSnapshot, String s) {
                String message = rowDataSnapshot.getValue(String.class);

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String key) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupRecycler() {
        BetterChatAdapter adapter = new BetterChatAdapter(getContext(), mDatabase.getReference("BetterChat"));
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static class BetterChatAdapter extends FirebaseRecyclerAdapter<ChatMessage, BetterChatAdapter.BetterChatViewHolder>{
        private Context context;
        public BetterChatAdapter(Context context, Query query) {
            super(ChatMessage.class, R.layout.chat_item, BetterChatViewHolder.class, query);
            this.context = context;
        }

        @Override
        protected void populateViewHolder(BetterChatViewHolder viewHolder, ChatMessage model, int position) {
            viewHolder.tvMessage.setText(model.getMessage());
            viewHolder.tvSenderTime.setText(model.getSender() + " " + model.getTime());

            Glide.with(context).load(model.getProfileImage()).into(viewHolder.ivProfile);
        }

        public static class BetterChatViewHolder extends RecyclerView.ViewHolder {
            CircularImageView ivProfile;
            TextView tvSenderTime;
            TextView tvMessage;

            public BetterChatViewHolder(View itemView) {
                super(itemView);
                ivProfile = (CircularImageView) itemView.findViewById(R.id.ivProfile);
                tvMessage = (TextView) itemView.findViewById(R.id.tvChat);
                tvSenderTime = (TextView) itemView.findViewById(R.id.tvSenderTime);
            }
        }
    }


    static class ChatAdapter extends FirebaseRecyclerAdapter<String, ChatAdapter.ChatViewHolder>{
        public ChatAdapter(Query ref) {
            super(String.class, R.layout.chat_item, ChatViewHolder.class, ref);
        }
        @Override
        protected void populateViewHolder(ChatViewHolder v, String text, int position) {
            v.tvChat.setText(text);
        }
        public static class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView tvChat;

            public ChatViewHolder(View itemView) {
                super(itemView);
                tvChat = (TextView) itemView.findViewById(R.id.tvChat);
            }
        }

    }
}
