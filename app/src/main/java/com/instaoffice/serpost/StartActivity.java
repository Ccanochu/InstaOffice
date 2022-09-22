package com.instaoffice.serpost;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;*/

public class StartActivity extends AppCompatActivity {
//1.- CREAMOS NUESTRAS VARIABLES LOCALES PARA TOMAR LOS ELEMENTOS DEL DISEÑO
    //LAS VARIABLES TIPO BUTTON , LAS CUALES ENCONTRAMOS EN LA PANTALLA DE INICIO
    Button login, registro;
    //NUESTRA VARIABLE DE FIREBASE --> FIREBASEUSER : PARA HACER LA CONSULLTA SI ES QUE EXISTE UN USUARIO LOGEADO
    //FirebaseUser firebaseUser;

//2.- AQUÍ ES CUANDO RECIÉN SE HA ACCEDIDO A LA APP
    //ESTE ES UN PASO ANTES DE QUE SE INSTANCIEN LOS ELEMENTOS DEL DISEÑO
    // EN EL MÉTODO ONSTART
    @Override
    protected void onStart() {
        super.onStart();
        //AQUÍ VAMOS A IDENTIFICAR SI EXISTE UN USUARIO LOGEADO
        // CON EL MÉTODO getCurrentUser() :VAMOS A OBTENER AL USUARIO CON SESIÓN ACTIVA
        /*firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //SI ES DIFERENTE DE NULO NOS VA MANDAR A LA PANTALLA PRINCIPAL
        if (firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            //Y FINALIZARÁ EL ACTIVITY ANTERIOR --> EL START ACTIVITY : QUE VIENE A SER LA PANTALLA DE INICIO
        }*/
    }

//3.- AQUÍ RECIÉN VAMOS A INSTANCIAR LOS ELEMENTOS DEL DISEÑO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //La actividad es básicamente una ventana vacía
        //CON SetContentView VAMOS A llenar la ventana con la interfaz de usuario que TENEMOS EN LOS archivos de diseño
        setContentView(R.layout.activity_start);
        //AQUÍ VAMOS A INSTANCIAR LOS BOTONES DE LOGEO Y REGISTRO
        // CON findViewById: vamos enlazar un recurso de la interfaz , con una variable en nuestro código.
        login = findViewById(R.id.login);
        registro = findViewById(R.id.registro);
        //VAMOS ASIGNAR 1 EVENTO A CADA BOTÓN
        //AQUÍ LE ASIGNAMOS UN EVENTO AL BOTÓN LOGIN
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent : nos va permitir la vinculación en tiempo de ejecución entre 2 actividades.
                startActivity(new Intent(StartActivity.this, LogeoActivity.class));
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegistroActivity.class));
            }
        });
    }
}
