package com.instaoffice.serpost.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/
import com.instaoffice.serpost.Model.Usuario;
import com.instaoffice.serpost.R;
/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;*/
import com.instaoffice.serpost.MensajeActivity;

import java.util.concurrent.ExecutionException;

public class MyFirebaseMessaging //extends FirebaseMessagingService
{

    private static final String CHANNEL_ID = "com.instaoffice.serpost";
    private static final String CHANNEL_NAME = "serpost";
    private static final String CHANNEL_DESCRIPTION = "Aplicación de chat";
    String img="default";

    /*@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");

        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {
            if (!currentUser.equals(user)) {
                sendNotification(remoteMessage);
            }
        }

    }


    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String emisor = remoteMessage.getData().get("emisor");

        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        //_____________________________________________________________________________________
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuario").child(emisor);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            //Puedes usar el método onDataChange() para leer una instantánea estática del contenido
            // de una ruta de acceso determinada y ver cómo se encontraba en el momento del evento.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                img= usuario.getImagenURL();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        //_____________________________________________________________________________________

        int notification_id = (int) System.currentTimeMillis();
        NotificationManager notificationManager = null;
        NotificationCompat.Builder mBuilder;


        //Set pending intent to builder
        //Intent intent = new Intent(this, MensajeActivity.class);
        Bundle bundle = new Bundle();

        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //Notification builder
        /*if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                mChannel.setDescription(CHANNEL_DESCRIPTION);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            FutureTarget<Bitmap> futureTarget =
                    Glide.with(this)
                            .asBitmap()
                            .load(img).override(Target.SIZE_ORIGINAL)
                            .submit(24, 24);

            Bitmap bitmap = null;
            try {
                bitmap = futureTarget.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Glide.with(this).clear(futureTarget);
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            mBuilder.setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_message)//show icon on status bar
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_ALL).setLargeIcon(bitmap);

        } else {
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentText(body)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_VIBRATE);
        }

        notificationManager.notify(1002, mBuilder.build());
    }*/
}