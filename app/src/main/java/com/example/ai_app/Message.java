package com.example.ai_app;
public class Message
{
    private String content;
    private boolean isUser;
    public Message(String content, boolean isUser)
    {
        this.content = content;
        this.isUser = isUser;
    }
    public String getContent()
    {
        return content;
    }
    public boolean isUser()
    {
        return isUser;
    }
}