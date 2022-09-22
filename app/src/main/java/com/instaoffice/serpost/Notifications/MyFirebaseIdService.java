package com.instaoffice.serpost.Notifications;

import androidx.annotation.NonNull;

/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.messaging.FirebaseMessagingService;*/

//Método para obtener el token del dispositivo
public class MyFirebaseIdService //extends FirebaseMessagingService
 {

    /*@Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String newToken = FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();

        if (firebaseUser != null) {
            updateToken(newToken);
        }
    }*/


    private void updateToken(String refreshToken) {
        /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);*/
    }

}
