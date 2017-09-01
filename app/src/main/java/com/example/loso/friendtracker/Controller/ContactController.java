package com.example.loso.friendtracker.Controller;

import android.content.Intent;
import android.provider.ContactsContract;

import com.example.loso.friendtracker.Model.Friend;

import java.util.ArrayList;

/**
 * Contains the sample code on the usage of the contact picker. Probably better to put it in another class.
 * <p>
 * Created by letti on 01/09/2017.
 */

public class ContactController {
    private ArrayList<Friend> friends;

//    	public void makeFriendsFromContacts() {
//        if (friends == null) {
//            friends = new ArrayList<Friend>();
//        }
//        protected static final int PICK_CONTACTS = 100;
//        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(contactPickerIntent, PICK_CONTACTS);
//
//        /*
//  * @author ermyasabebe
//  * */
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data){
//            if (requestCode == PICK_CONTACTS) {
//                if (resultCode == RESULT_OK) {
//                    ContactDataManager contactsManager = new ContactDataManager(this, data);
//                    String name = "";
//                    String email = "";
//                    try {
//                        name = contactsManager.getContactName();
//                        email = contactsManager.getContactEmail();
//                    } catch (ContactDataManager.ContactQueryException e) {
//                        Log.e("LOG_TAG", e.getMessage());
//                    }
//                }
//            }
//        }
//
//        ContactDataManager contacts = new ContactDataManager();
//
//    }
//


}
