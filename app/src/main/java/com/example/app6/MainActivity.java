package com.example.app6;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.loader.content.CursorLoader;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String CHANNEL_ID = "abcd";
    private NotificationManagerCompat mNotificationManagerCompat;
    public static final String CID = "channe1";
    String phone, name;

    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManagerCompat = NotificationManagerCompat.from(this);

        but = findViewById(R.id.but1);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification();
            }
        });
    }

    public void notif() {
        Notification not1 = new NotificationCompat.Builder(this, CID)
                .setContentTitle(name)
                .setContentText(phone)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        mNotificationManagerCompat.notify(1, not1);
    }

    public void contacts(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 1);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void contactpicked(Intent data){
        Cursor cursor;
        try{
            Uri uri=data.getData();
            cursor=getContentResolver().query(uri,null,null,null);
            cursor.moveToFirst();
            int temp = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phone=cursor.getString(temp);

            temp = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            name=cursor.getString(temp);

            Toast.makeText(getApplicationContext(),name+"\n"+phone,Toast.LENGTH_SHORT).show();
            notif();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                contactpicked(data);
                Cursor cursor;
                try {
                    String phone;
                    Uri uri = data.getData();
                    if (uri != null) {
                        cursor = new CursorLoader(getApplicationContext(), uri, null, null, null, null)
                                .loadInBackground();
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            int number1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            phone = cursor.getString(number);
                            String name = cursor.getString(number1);
                            Toast.makeText(getApplicationContext(), name + "\n" + phone, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notification() {
        createNotification();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hey, there...")
                .setContentText("Have a good Day!");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
        int NOTIFICATION_ID = 1;
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String descr = "All notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(descr);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}
