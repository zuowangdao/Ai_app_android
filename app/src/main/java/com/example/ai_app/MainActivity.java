package com.example.ai_app;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messages;
    private EditText inputEditText;
    private ImageButton sendButton;
    private ImageButton btnLeft;
    private ImageButton btnRight;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ApiConfigPrefs";
    @Override
    protected void onResume()
    {
        super.onResume();
        sharedPreferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        AssistantManager.setApiKey(sharedPreferences.getString("api_key", ""));
        AssistantManager.setDeepseekApiUrl(sharedPreferences.getString("api_url", ""));
        AssistantManager.setAi_MODE(sharedPreferences.getString("api_mode", ""));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        AssistantManager.setApiKey(sharedPreferences.getString("api_key", ""));
        AssistantManager.setDeepseekApiUrl(sharedPreferences.getString("api_url", ""));
        AssistantManager.setAi_MODE(sharedPreferences.getString("api_mode", ""));
        // 初始化视图
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        inputEditText = findViewById(R.id.input_edit_text);
        sendButton = findViewById(R.id.send_button);
        btnLeft = findViewById(R.id.btn_left);
        btnRight= findViewById(R.id.btn_right);

        // 初始化消息列表
        messages = new ArrayList<>();
        messages.add(new Message(AssistantManager.getGreeting(), false));

        // 设置RecyclerView
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);
        btnLeft.setOnClickListener(v -> {
            sharedPreferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            AssistantManager.setApiKey(sharedPreferences.getString("api_key", ""));
            AssistantManager.setDeepseekApiUrl(sharedPreferences.getString("api_url", ""));
            AssistantManager.setAi_MODE(sharedPreferences.getString("api_mode", ""));
            messages.clear();
            chatAdapter.notifyDataSetChanged();
            Toast.makeText(this, "new chat", Toast.LENGTH_SHORT).show();
        });

        btnRight.setOnClickListener(v -> {
                    // 打开设置页面
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                });
        // 发送按钮点击事件
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        }
        );
    }

    private void sendMessage()
    {
        String input = inputEditText.getText().toString().trim();
        if (!input.isEmpty())
        {
            // 添加用户消息
            sendButton.setEnabled(false);
            messages.add(new Message(input, true));
            chatAdapter.notifyItemInserted(messages.size() - 1);
            chatRecyclerView.scrollToPosition(messages.size() - 1);
            inputEditText.setText("");
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    String response = AssistantManager.getResponse(input,messages);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            messages.add(new Message(response, false));
                            chatAdapter.notifyItemInserted(messages.size() - 1);
                            chatRecyclerView.scrollToPosition(messages.size() - 1);
                            sendButton.setEnabled(true);
                        }
                    }
                    );
                }
            }).start();
        }
    }
}