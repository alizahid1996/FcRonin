package com.example.fcronin.Interfaces;

import android.view.View;

import com.example.fcronin.Models.Group;
import com.example.fcronin.Models.User;

public interface OnUserGroupItemClick {
    void OnUserClick(User user, int position, View userImage);
    void OnGroupClick(Group group, int position, View userImage);
}
