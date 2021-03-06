package com.company.wilipop.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.company.wilipop.R;
import com.company.wilipop.model.Chat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyChatsActivity extends AppCompatActivity {

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chats);

        mRef = FirebaseDatabase.getInstance().getReference();

        String uid = "uid-"+ FirebaseAuth.getInstance().getUid();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setIndexedQuery(mRef.child("chats/user-chats").child(uid), mRef.child("chats/data"), Chat.class)
                .setLifecycleOwner(this)
                .build();

        ChatsAdapter chatsAdapter = new ChatsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rvMyChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatsAdapter);
    }

    class ChatsAdapter extends FirebaseRecyclerAdapter<Chat, ChatsAdapter.ChatViewHolder> {
        class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView lastMessage, title;
            ImageView photo;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                lastMessage = itemView.findViewById(R.id.tvLastMessage);
                title = itemView.findViewById(R.id.tvProductDescription);
                photo = itemView.findViewById(R.id.ivPhoto);
            }
        }

        public ChatsAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
            super(options);
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ChatViewHolder(inflater.inflate(R.layout.chat_viewholder, viewGroup, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull ChatViewHolder holder, final int position, @NonNull final Chat chat) {
            final String chatKey = getRef(position).getKey();

            holder.title.setText(chat.productDescription);
            holder.lastMessage.setText(chat.lastMessage);
            Glide.with(holder.itemView.getContext()).load(chat.productPhotoUrl).into(holder.photo);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("CHAT_KEY", chatKey);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
