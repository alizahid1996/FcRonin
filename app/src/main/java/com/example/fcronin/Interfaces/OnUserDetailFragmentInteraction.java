package com.example.fcronin.Interfaces;

import com.example.fcronin.Models.Message;

import java.util.ArrayList;

public interface OnUserDetailFragmentInteraction {
    void getAttachments();

    ArrayList<Message> getAttachments(int tabPos);

    void switchToMediaFragment();
}