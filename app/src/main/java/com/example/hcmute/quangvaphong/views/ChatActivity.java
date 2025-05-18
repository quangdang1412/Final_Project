package com.example.hcmute.quangvaphong.views;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.controllers.ChatBot;
import com.example.hcmute.quangvaphong.models.ChatMessage;
import com.example.hcmute.quangvaphong.views.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private EditText msgInput;
    private ImageButton sendMsgBtn, backBtn;
    private RecyclerView recyclerView;

    private String id = UUID.randomUUID().toString();

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgInput = findViewById(R.id.chat_message_input);
        sendMsgBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);

        recyclerView = findViewById(R.id.chat_recycler_view);

        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        backBtn.setOnClickListener(v -> {
            finish();
        });

        sendMsgBtn.setOnClickListener(v -> {
            String msg = msgInput.getText().toString().trim();
            if (msg.isEmpty())
                return;
            addMessage(msg, true);
            ChatBot.sendMsgToChatBot(this, msg, id, new ChatBot.ChatBotCallback() {
                @Override
                public void onResponse(String message) {
                    addMessage(message, false);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(ChatActivity.this, "Chatbot phản hồi lỗi", Toast.LENGTH_SHORT).show();
                }
            });
            msgInput.setText("");
        });
    }


    private void addMessage(String msg, boolean isSender) {
        messageList.add(new ChatMessage(msg, isSender));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}