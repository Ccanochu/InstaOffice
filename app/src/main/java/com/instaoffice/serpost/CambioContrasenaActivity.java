package com.instaoffice.serpost;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;*/

public class CambioContrasenaActivity extends AppCompatActivity {

    EditText enviar_email;
    Button btn_reset;

    //FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasena);

        Toolbar toolbar = findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cambiar contraseña");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enviar_email = findViewById(R.id.enviar_email);
        btn_reset = findViewById(R.id.btn_reset);

        //firebaseAuth = FirebaseAuth.getInstance();

        //BOTÓN RESTABLECER , QUIERO QUE ME TOME EL DATO DEL CORREO QUE HE INGRESADO

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = enviar_email.getText().toString();

                //CON ESE DATO HACEMOS LA CONSULTA
                // SI ESTÁ VACÍO --> MENSAJE
                if (email.equals("")){
                    Toast.makeText(CambioContrasenaActivity.this, "Todos los casilleros son requeridos!", Toast.LENGTH_SHORT).show();
               // Y SI ESTÁ LLENO VA INGRESAR AL SIGUIENTE MÉTODO EL DE FIREBASE AUTH.
                    // (TEMPLATES)EN ESTA INSTANCIA SE VA A RESTABLECIMIENTO DE LA CONTRASEÑA
                } else {
                    // EL CORREO SE ENVIA AL SIGUIENTE MÉTODO PARA PROCEDER CON EL RESTABLECIMIENTO DE CONTRASEÑA
                    //sendPasswordResetEmail BUSCA UN PERMISO ASOCIADO AL CORREO Y ME MANDA UN LINK A MI CORREO DE UNA PAG. PARA RESTABLECER LA CONTRASEÑA
                    /*firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //CUANDO ESTE COMPLETA LA SOLICITUD
                            if (task.isSuccessful()){
                                Toast.makeText(CambioContrasenaActivity.this, "REVISE SU CORREO", Toast.LENGTH_SHORT).show();
                             // AQUÍ ME MANDA A LA PANTALLA DE LOGEO
                                startActivity(new Intent(CambioContrasenaActivity.this, LogeoActivity.class));
                            //Y SI EN CASO EL CORREO INGRESADO NO EXISTE
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(CambioContrasenaActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                }
            }
        });

    }
}
