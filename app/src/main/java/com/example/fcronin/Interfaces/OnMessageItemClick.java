package com.example.fcronin.Interfaces;

import com.example.fcronin.Models.Message;

public interface OnMessageItemClick {
    void OnMessageClick(Message message, int position);

    void OnMessageLongClick(Message message, int position);
}