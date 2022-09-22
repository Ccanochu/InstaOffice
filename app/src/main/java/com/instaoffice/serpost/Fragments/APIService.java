package com.instaoffice.serpost.Fragments;

import com.instaoffice.serpost.Notifications.MyResponse;
import com.instaoffice.serpost.Notifications.Emisor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
//NOS VA SERVIR DE INTERMEDIARIO PARA LA ACTIVIDAD DE LAS NOTIFICACIONES
public interface APIService {
    //Se ingresa la clave de autorización otorgada por FireBase para poder crear el servicio

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAghC8JmM:APA91bEqg8owIrQaDi05S_oR0f96wOSMVQ0SGTjUOcHiQ1f3DX_IEpw0RKO822GMMTYUizwO9Xe8dnRhIhTKuygd56aplt9GBYEYFzb4lXNFiDbsKDldPN-A9nxsyaUN60za-u0rXarl"
            }

    )
//SE LE ENVÍA LA NOTIFICACIÓN AL SERVIDOR DE FIREBASE
    @POST("fcm/send")
    Call<MyResponse>sendNotification(@Body Emisor body);
}
