package com.instaoffice.serpost;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {
    //Se crean las variables que se usarán en el activity
    MaterialEditText nombreusuario, email, contrasena, departamento, provincia, sede;
    Button btn_registrar;
    //Se crea las variables para Firebase
    //FirebaseAuth auth;
    //DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Se instancia a la barra de superior que tendrá título Registrar en esta activity
        Toolbar toolbar = findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registrar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Se instancian a los textbox del activity para obtener sus valores
        nombreusuario = findViewById(R.id.nombreusuario);
        email = findViewById(R.id.emailUsuario);
        contrasena = findViewById(R.id.contrasena);
        departamento= findViewById(R.id.departamentoUsuario);
        provincia=findViewById(R.id.provinciaUsuario);
        sede= findViewById(R.id.sedeUsuario);
        btn_registrar = findViewById(R.id.btn_registrar);

        //Se obtiene la instancia del servicio de autenticación de firebase
        //auth = FirebaseAuth.getInstance();

        //Se agrega evento al boton registrar para recuperar los datos
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_nombreusuario = nombreusuario.getText().toString();
                String txt_email = email.getText().toString();
                String txt_contrasena = contrasena.getText().toString();
                String txt_departamento = departamento.getText().toString();
                String txt_provincia = provincia.getText().toString();
                String txt_sede=sede.getText().toString();

                //Si los elementos recuperados están vacíos, se envía un mensaje al activity solocitando que se llene
                if (TextUtils.isEmpty(txt_nombreusuario) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_contrasena )||
                        TextUtils.isEmpty(txt_departamento)|| TextUtils.isEmpty(txt_provincia)|| TextUtils.isEmpty(txt_sede)){
                    Toast.makeText(RegistroActivity.this, "Se debe llenar todos los casilleros", Toast.LENGTH_SHORT).show();
                //Si la contraseña en menor de 6 dígito se solicita que se redacte una de al menos 6 dígitos
                } else if (txt_contrasena.length() < 6 ){
                    Toast.makeText(RegistroActivity.this, "La contraseña debe poseer al menos 6 caracteres",
                            Toast.LENGTH_SHORT).show();
                //En caso todos los datos cumplan con las condiciones se envian al método registrar
                } else {
                    registrar(txt_nombreusuario, txt_email, txt_contrasena,txt_departamento,txt_provincia,txt_sede);
                }
            }
        });
    }

    //El método registrar se encarga de crear el usuario en la base de datos Firebase

    //Tambien se encarga de añadir el correo y contraseña al servicio de autenticación de Firebase
    private void registrar(final String nombreusuario, String email, String password, String departamento, String provincia, String sede){

    //Se crea el usuario para autenticación al logear
        /*auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            //Se obtiene el id de la autenticación reciente
                            String idUsuario = firebaseUser.getUid();
                            //Se obtiene la instancia de la base de datos.
                            //Se obtiene la fila en la cual se encuentra el usuario a registrar
                            reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario);

                            //Se añade los datos necesarios para registrar al usuario
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", idUsuario);
                            hashMap.put("nombreUsuario", nombreusuario);
                            hashMap.put("departamento", departamento);
                            hashMap.put("provincia",provincia);
                            hashMap.put("sede",sede);
                            hashMap.put("imagenURL", "default");
                            hashMap.put("estado", "Desconectado");
                            hashMap.put("busqueda", nombreusuario.toLowerCase());


                            //Al registrarse exitosamente, se redirige al activity principal. Ya que al registrar
                            //se logea automaticamente
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
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
                                    }
                                }
                            });
                            //En caso suceda algún error al registrar se mostrará el siguiente mensaje
                        } else {
                            Toast.makeText(RegistroActivity.this, "Usted no puede registrarse con ese correo o contraseña",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
    }
}
