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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CambioContrasenaActivity extends AppCompatActivity {

    EditText enviar_email;
    Button btn_reset;

    FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = enviar_email.getText().toString();

                if (email.equals("")){
                    Toast.makeText(CambioContrasenaActivity.this, "Todos los casilleros son requeridos!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CambioContrasenaActivity.this, "Por favor ingrese su correo", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CambioContrasenaActivity.this, LogeoActivity.class));
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(CambioContrasenaActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
