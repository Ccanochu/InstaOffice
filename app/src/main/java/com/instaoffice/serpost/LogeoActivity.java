package com.instaoffice.serpost;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;*/
import com.rengwuxian.materialedittext.MaterialEditText;

public class LogeoActivity extends AppCompatActivity {
    //Se crea las variables para los elementos del activity de logeo
    MaterialEditText email, contrasena;
    Button btn_logeo;
    //Se crea las variables para Firebase
    //FirebaseAuth auth;
    TextView olvido_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeo);


        //Se crea, instancia y configura las variables para la barra superior del activity de Logeo
        Toolbar toolbar = findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Se instancia el servicio de autenticación de Firebase para el logeo
        //auth = FirebaseAuth.getInstance();


        //Se instancia las variables de los componentes del activity
        email = findViewById(R.id.emailUsuario);
        contrasena = findViewById(R.id.contrasena);
        btn_logeo = findViewById(R.id.btn_logeo);
        olvido_contrasena = findViewById(R.id.olvido_contrasena);


        //Se configura el evento para redirigir al activity de cambiar de contraseña
        olvido_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogeoActivity.this, CambioContrasenaActivity.class));
            }
        });


        //Se configura y se codifica el código de logeo
        btn_logeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se instancia los elementos del activity de logeo y se extrae el contenido ingresado (EL CORREO Y LA CONTRASEÑA)
                String txt_email= email.getText().toString();
                String txt_contrasena = contrasena.getText().toString();
 //CONSISTENCIACIÓN
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_contrasena)){
                    Toast.makeText(LogeoActivity.this, "Debe llenar todos los casilleros", Toast.LENGTH_SHORT).show();
                } else {
                    //Se solicita y realiza el servicio de autenticación de ingreso a la aplicación mediante FIrebase
                    /*auth.signInWithEmailAndPassword(txt_email, txt_contrasena)
                            //signInWithEmailAndPassword : logearse con correo y contraseña --> método de firebase

                            //SI SALIÓ BIEN LA AUTENTICACIÓN ENTONCES IGUAL QUE EN REGISTRAR ME MANDE AL ACTIVITY PRINCIPAL
                            // Y FINALICE START ACTIVITY
                            // Y SI ES QUE HAY ALGÚN ERROR
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        //Si la autenticación se realizó con éxito, se crea
                                        Intent intent = new Intent(LogeoActivity.this, MainActivity.class);
                                        //FLAG_ACTIVITY_CLEAR_TASK
                                        //Si se establece en un Intent pasado al Contexto # startActivity,
                                        // este flag hará que cualquier tarea existente que esté asociada con la actividad
                                        // se borre antes de que se inicie la actividad.
                                        //FLAG_ACTIVITY_NEW_TASK
                                        //Si se establece, esta actividad se convertirá en el
                                        // comienzo de una nueva tarea en esta pila de historial.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LogeoActivity.this, "Error al autenticar el usuario",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                }
            }
        });
    }
}
