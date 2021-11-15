package com.example.java_get_contacts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS = 101;
    private static final int REQUEST_CODE_READ_FILES = 1;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionStatus2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED && permissionStatus2 == PackageManager.PERMISSION_GRANTED) {
            getContacts();
            listFiles();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_CONTACTS);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getContacts() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contacts = getContentResolver().query(uri, null, null, null, null);
        ArrayList<String> files = new ArrayList<>();

        if (contacts != null) {

            while (contacts.moveToNext()) {
                @SuppressLint("Range")
                String file = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                files.add(file);
            }
            contacts.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, files
        );

        ListView contactList = findViewById(R.id.list_view);

        contactList.setAdapter(adapter);
    }

    public void listFiles() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getPath());
        File f = new File(dir.toString());
        File[] files = f.listFiles();

        String[] theNamesOfFiles = new String[files.length];


        for (int i = 0; i < theNamesOfFiles.length; i++) {
            Log.i("BRUH", String.valueOf(theNamesOfFiles.length));
            theNamesOfFiles[i] = files[i].getName();
        }
//        List<String> list = null;
//        if (files != null) {
//            for (File file : files) {
//                Log.i("TAG", file.getName());
//                list.add(file.getName());
//            }
//        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, theNamesOfFiles
        );

        ListView fileslist = findViewById(R.id.list_view2);

        fileslist.setAdapter(adapter);

    }
}
