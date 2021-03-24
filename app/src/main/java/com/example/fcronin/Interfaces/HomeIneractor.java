package com.example.fcronin.Interfaces;

import com.example.fcronin.Models.Contact;
import com.example.fcronin.Models.User;

import java.util.ArrayList;

public interface HomeIneractor {
    User getUserMe();

    ArrayList<Contact> getLocalContacts();

}