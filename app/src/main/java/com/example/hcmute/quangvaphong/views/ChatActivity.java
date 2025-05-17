package com.example.hcmute.quangvaphong.views;


import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.ChatMessage;
import com.example.hcmute.quangvaphong.views.adapter.ChatAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
            sendMsgToChatBot(msg);
            msgInput.setText("");
        });
    }

    private void sendMsgToChatBot(String msg) {
        OkHttpClient client = new OkHttpClient();


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", id);
            jsonBody.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonBody.toString()
        );

        Request request = new Request.Builder()
                .url("https://n8n.nayamishop.id.vn/webhook/chatbot")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show());
                Log.e("ChatAPI", "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseText = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseText);
                        String output = jsonObject.getString("output");
                        runOnUiThread(() -> addMessage(output, false));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Chatbot phản hồi lỗi", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void addMessage(String msg, boolean isSender) {
        messageList.add(new ChatMessage(msg, isSender));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}