package com.example.ai_app;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity
{

    private TextInputEditText etApi, etUrl, etMode;
    private Button btnSave;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ApiConfigPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 初始化视图
        initViews();

        // 加载已保存的配置
        loadConfig();

        // 设置保存按钮点击事件
        setupSaveButton();
    }

    private void initViews() {
        etApi = findViewById(R.id.et_api);
        etUrl = findViewById(R.id.et_url);
        etMode = findViewById(R.id.et_mode);
        btnSave = findViewById(R.id.btn_save);
    }

    private void loadConfig()
    {
        // 从SharedPreferences读取配置
        String apiKey = sharedPreferences.getString("api_key", "");
        String apiUrl = sharedPreferences.getString("api_url", "");
        String apiMode = sharedPreferences.getString("api_mode", "");

        // 设置到输入框
        etApi.setText(apiKey);
        etUrl.setText(apiUrl);
        etMode.setText(apiMode);
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> saveConfig());
    }

    private void saveConfig() {
        // 获取当前输入的值
        String apiKey = etApi.getText().toString().trim();
        String apiUrl = etUrl.getText().toString().trim();
        String apiMode = etMode.getText().toString().trim();

        // 验证输入
        if (apiKey.isEmpty() || apiUrl.isEmpty() || apiMode.isEmpty())
        {
            Toast.makeText(this, "请填写所有配置项", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证URL格式
        if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://"))
        {
            Toast.makeText(this, "URL必须以http://或https://开头", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存到SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("api_key", apiKey);
        editor.putString("api_url", apiUrl);
        editor.putString("api_mode", apiMode);
        editor.apply();

        Toast.makeText(this, "配置已保存", Toast.LENGTH_SHORT).show();
        finish(); // 关闭当前Activity
    }
}