package com.example.ai_app;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.animation.Animation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder>
{
    private List<Message> messages;
    public ChatAdapter(List<Message> messages)
    {
        this.messages = messages;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position)
    {
        Message message = messages.get(position);
        holder.messageText.setText(message.getContent());
        // 设置长按监听器
        holder.messageText.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Message", message.getContent());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // 设置不同样式的气泡
        if (message.isUser())
        {
            holder.messageText.setBackgroundResource(R.drawable.user_message_bubble);
            holder.messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            holder.timestamp.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else
        {
            holder.messageText.setBackgroundResource(R.drawable.assistant_message_bubble);
            holder.messageText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            holder.timestamp.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        // 设置时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a", Locale.getDefault());
        holder.timestamp.setText(sdf.format(new Date()));
        Animation animation = message.isUser() ?
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in_right) :
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

    }
    static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        TextView messageText;
        TextView timestamp;

        MessageViewHolder(View itemView)
        {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
    @Override
    public int getItemCount()
    {
        return messages.size();
    }
}