package com.instaoffice.serpost;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
/*import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;*/
import com.instaoffice.serpost.Adapter.MensajeAdapter;
import com.instaoffice.serpost.Fragments.APIService;
import com.instaoffice.serpost.Model.Chat;
import com.instaoffice.serpost.Model.Usuario;
import com.instaoffice.serpost.Notifications.Client;
import com.instaoffice.serpost.Notifications.Data;
import com.instaoffice.serpost.Notifications.MyResponse;
import com.instaoffice.serpost.Notifications.Emisor;
import com.instaoffice.serpost.Notifications.Token;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MensajeActivity extends AppCompatActivity {
//1.-
//Se crea las variables de los elementos que compone el activity mensaje
    //Imagen y nombre del usuario que recibe el mensaje
    CircleImageView imagenPerfil;
    TextView nombreUsuario;
    //Para inicializar Firebase
    /*FirebaseUser fuser;
    DatabaseReference reference;*/
    //Textbox y botón para enviar mensajes
    ImageButton btn_enviar;
    ImageButton btn_adjuntar;
    EditText text_enviar;
//2.-
    //AQUÍ SE CREA LA INSTANCIA DE MENSAJE ADAPTER PARA PODER UTILIZARLO
    //List<Chat> : SE CREA PARA QUE SEA LLENADA POR EL ADAPTADOR Y ESTE NOS DEVUELVA ITEMS ( LA LISTA DE MSJS )
    //Adaptador que llenará el recycler view con la lista de los mensajes enviados y recibidos
    MensajeAdapter mensajeAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

//3.-
    //Se inicializa las variables para el servicio de Storage de Firebase
    //StorageReference storageReference;
    Intent intent;
    //ValueEventListener seenListener;
    //Variable del idUsuario y tipo de dato fecha
    String idUsuario;
    Date fecha;
    //SE CREA LA INSTANCIA DEL SERVICIO
    APIService apiService;
    //CREAMOS LA VARIABLE DE NOTIFICACIÓN DE TIPO BOOLEAN  (SE VA MANTENER EN FALSE HASTA QUE SE ENVIE UN MSJ)
    boolean notify = false;

//4.-
   //CON ESTE MÉTODO VA PERMITIR INICIAR LA ACTIVIDAD UNA VEZ SE HAYA SELECCIONADO UN USUARIO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//5.-
        //CON SetContentView VAMOS A llenar la ventana con la interfaz del diseño
        setContentView(R.layout.activity_mensaje);
//6.-
        // VAMOS A  LLAMAR Y CONFIGURAR la barra superior que contiene la imagen y el nombre del usuario destino
        //findViewById : es un método utilizado para conectar código con elementos en la interfaz de usuario (UI).
        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //setDisplayHomeAsUpEnabled : ME VA PERMITIR REGRESAR A LA PANTALLA ANTERIOR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //LE ASIGNAMOS UN EVENTO A LA BARRA SUPERIOR
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ME VA PERMITIR regresar al activity principal
                startActivity(new Intent(MensajeActivity.this, MainActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

//7.-
        //Se instancia al api service
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //Se inicializa el recyclerview
        //findViewById : es un método utilizado para conectar código con elementos en la interfaz de usuario (UI).
        recyclerView = findViewById(R.id.recycler_view_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Se inicializa los elementos que componen el activity
        //findViewById : es un método utilizado para conectar código con elementos en la interfaz de usuario (UI).
        imagenPerfil = findViewById(R.id.imagenPerfil);
        nombreUsuario = findViewById(R.id.nombreusuario);
        btn_enviar = findViewById(R.id.btn_enviar);
        btn_adjuntar=findViewById(R.id.btn_adjuntar);
        text_enviar = findViewById(R.id.text_enviar);

//8.-
        //VOY A TOMAR LOS DATOS DEL USUARIO QUE SE GUARDÓ INTERNAMENTE EN LA VARIABLE ID USUARIO ( USUARIOADAPTER)
        //Se extrae el elemento que fue enviado desde el activity anterior
        intent = getIntent();
        //getStringExtra : MÉTODO PARA OBTENER DATOS
        idUsuario = intent.getStringExtra("idUsuario");
        // CON EL MÉTODO getCurrentUser() :VAMOS A OBTENER AL USUARIO CON SESIÓN ACTIVA
        //fuser = FirebaseAuth.getInstance().getCurrentUser();
        //HACEMOS REFERENCIA HACIA LA CARPETA FICHEROS , QUE SE ENCUENTRA EN EL SERVIDOR DE ALMACENAMIENTO
        //storageReference = FirebaseStorage.getInstance().getReference("ficheros");

//9.- ENVIAR MENSAJE
        //Lógica para botón para enviar mensaje
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ESTOY OBTENIENDO LA FECHA DEL SISTEMA
                fecha= Calendar.getInstance().getTime();
                // Y LE ASIGNO UN FORMATO
                String fecham = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                //SE VA ACTIVAR LA NOTIFICACIÓN
                notify = true;
                //SE OBTIENE EL TEXTO QUE SE DESEA ENVIAR
                String mensaje = text_enviar.getText().toString();
                // AQUÍ LA VERIFICAIÓN
                //SI EL MENSAJE NO ESTÁ VACÍO
                if (!mensaje.equals("")){
                    //SE ENVÍA LOS SGTS PARÁMETROS
                    //fecham : LA HORA EN FORMATO DE TEXTO
                    //enviarMensaje(fuser.getUid(), idUsuario, mensaje,fecham);
                } else {
                    Toast.makeText(MensajeActivity.this, "No puede enviar mensaje vacío", Toast.LENGTH_SHORT).show();
                }
                //CUANDO SE ENVÍA EL MSJ SE LIMPIA EL CASILLERO
                text_enviar.setText("");
            }
        });

 //10.- ADJUNTAR ARCHIVOS
        //LE ASIGNAMOS UN EVENTO AL BOTON ADJUNTAR
        btn_adjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    SeleccionarArchivo();
            }
        });

//11.- IMAGEN EN LA BARRA SUPERIOR DENTRO DEL CHAT
        //Conexión con Firebase, se obtiene los datos del usuario de la tabla usuario:
        //En caso el usuario no haya subido alguna foto se utilizará la imagen local por defecto
        /*reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //dataSnapshot : ME VA DEVOLVER EL CONTENIDO  DE LA TABLA
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                nombreUsuario.setText(usuario.getNombreUsuario());
                if (usuario.getImagenURL().equals("default")){
                    imagenPerfil.setImageResource(R.mipmap.ic_launcher);
                } else {
                    //Y SI NO CON LA LIBRERÍA Glide ME INSTANCIE LA IMAGEN SUBIDA POR EL USUARIO DENTRO DE LA VISTA
                    Glide.with(getApplicationContext()).load(usuario.getImagenURL()).into(imagenPerfil);
                }

                //ACA LLAMO AL MÉTODO DE LEER MENSAJES (9.2.5)  PARA MOSTRARLOS EN LA VISTA
                leerMensajes(fuser.getUid(), idUsuario, usuario.getImagenURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //EL MÉTODO EVALUA SI EL MSJ HA SIDO VISUALIZADO O NO POR EL RECEPTOR
        mensajeVisto(idUsuario);
    }


    //Solo PDF
   /* private void SeleccionarArchivo() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar archivo PDF"),1);
    }*/
//10.1 ADJUNTAR CUALQUIER TIPO DE ARCHIVO
   //CREAMOS UN INTENT QUE ME VA PERMITIR CAPTURAR UN ARCHIVO DE UALQUIER TIPO
    private void SeleccionarArchivo() {
        Intent intent = new Intent();
        // el tipo de archivo que se va buscar
        //file/* >> EXPLORADOR DE archivos
        intent.setType("file/*");
        // ACTION_GET_CONTENT : muestra la lista de los archivos del usuario y permite navegar a traves de ellos y elegir 1
        //y devolverlo a la acción principal
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //SE MATERIALISA EL ACIVITY PARA SELECCIONAR ARCHIVOS
        startActivityForResult(Intent.createChooser(intent,"Seleccionar archivo PDF"),1);
    }

    //Cuando se ha seleccionado el archivo
    // va tomar los siguientes parámetros del sistema (requestCode,  resultCode,  Intent data)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //Intent data : el archivo que se ha seleccionado
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            // SE PASA AL MÉTODO SUBIR ARCHIVO
            subirArchivo(data.getData());
        }
    }
    //MÉTODO PARA OBTENER  QUE TIPO DE extensión ES EL ARCHIVO SELECCIONADO (del fichero del archivo)
    //MimeTypeMap : CONTIENE EL TIPO DE EXTENSIONES
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    //MÉTODO PARA SUBIR EL ARCHIVO
    private void subirArchivo(Uri data) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando");
        pd.show();
        //Se sube el archivo con el nombre del tiempo del sistema y la extensión en PDF
        //StorageReference reference = storageReference.child(System.currentTimeMillis()+".pdf");
        //Se sube el archivo con el nombre del tiempo del sistema y la extensión del archivo  (PDF,XSLX,JPG,PNG,PPTX, MP3...)
        /*StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(data));
        reference.putFile(data)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //ESTOY GUARDANDO LA URL DEL ARCHIVO SUBIDO
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();
                    String murl= url.toString();
                    //SE ENVIA EL MENSAJE = URL
                    fecha= Calendar.getInstance().getTime();
                    String fecham = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                    notify = true;
                    enviarMensaje(fuser.getUid(), idUsuario, murl,fecham);
                    //
                    pd.dismiss();
                    //SE CIERRA EL CUADRO DE DIALOGO DE CARGANDO
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
            //PORCENTAJE DE CARGA PARA LA SUBIDA DE ARCHIVOS

            double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
            pd.setMessage("Cargando: "+ (int)progress+"%");
        }
    });*/
    }

//12.- ACTUALIZANDO LA BASE DE DATOS  LEIDO O ENVIADO
    //Este método configura el estado del mensaje para que figure como Leido
    private void mensajeVisto(final String idUsuario){
        /*reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceptor().equals(fuser.getUid()) && chat.getEmisor().equals(idUsuario)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("visto", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

//9.2
    //Este método se usa para enviar el mensaje al usuario destino
    private void enviarMensaje(String emisor, final String receptor, String mensaje, String fecha){
        //se referencia la instancia de la base de datos
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //GUARDAMOS LOS DATOS EN LA BD
        //HashMap : designa claves únicas para los valores correspondientes que se pueden recuperar en cualquier punto dado.
        //NOS VA PERMITIR SUBIR ESTA TABLA A FIREBASE
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("emisor", emisor);
        hashMap.put("receptor", receptor);
        hashMap.put("mensaje", mensaje);
        hashMap.put("visto", false);
        hashMap.put("fecham",fecha);
        //Se ingresa todos los datos pertinentes para enviar un mensaje y se agrega a la tabla Chats
        //reference.child("Chats").push().setValue(hashMap);

//9.2.1 LLENO DATOS A LA TABLA CHATLIST
        // COMO EMISOR : Se agrega el usuario CON QUIEN SE HA INICIADO LA CONVERSACIÓN a la tabla de ChatList
        /*final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid()) //MI ID
                .child(idUsuario); // Y EL ID DEL USUARIO RECEPTOR

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // VA A HACER UNA LECTURA DE ESA RUTA
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(idUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

//9.2.2 LLENO DATOS A LA TABLA CHATLIST
        // COMO RECEPTOR : Se LE agrega AL usuario CON QUIEN SE HA INICIADO LA CONVERSACIÓN a la tabla de ChatList
        /*final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(idUsuario)//ID RECEPTOR
                .child(fuser.getUid()); //MI ID
        chatRefReceiver.child("id").setValue(fuser.getUid());

 //9.2.3 NOTIFICACIÓN (PARÁMETROS PARA EL ENVIO DE LA NOTIFICACIÓN)
        //DECLARAMOS LA VARIABLE msg Y LE ASIGNAMOS EL MENSAJE (ESTO PARA ENVIAR LA NOTIFICACIÓN)
        final String msg = mensaje;

        //HACEMOS LA INSTANCIA A LA TABLA USUARIOS PARA VER QUIÉN ES EL EMISOR  (A MI TABLA)
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // LE ASIGNO LOS DATOS DEL EMISOR ( DE LA TABLA USUARIOS )AL OBJETO usuario
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (notify) {
                    // si notify = true >> se instancia el método enviar notificación con los sgts parámetros
                    enviarNotificacion(receptor, usuario.getNombreUsuario(), msg, usuario.getId());
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

 //9.2.4 NOTIFICACIÓN : (AQUÍ YA SE ENVÍA LA NOTIFICACIÓN)
    //DECLARAMOS LOS PARÁMETROS DE ENTRADA
    private void enviarNotificacion(String receptor, final String nombreUsuario, final String mensaje, final String emisor){

        //Se instancia la referencia de la tabla de Tokens
        /*DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receptor);
        query.addValueEventListener(new ValueEventListener() {
            //La notificación contendrá los siguientes datos
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //GUARDAMOS EL TOKEN DEL USUARIO A QUIÉN SE LE ENVIARÁ LA NOTIFICACIÓN
                    Token token = snapshot.getValue(Token.class);
                    //GUARDANDO LOS PARÁMETROS CON LOS DATOS ( PARA EL ENVIO DE LA NOTIFICACIÓN
                    Data data = new Data(fuser.getUid(), R.drawable.ic_message, nombreUsuario+": "+mensaje, "Nuevo Mensaje",
                            idUsuario,emisor);

                    //CREAMOS UN OBJETO TIPO EMISOR -->> Y GUARDAMOS DATA Y SU TOKEN
                    Emisor emisor = new Emisor(data, token.getToken());

                    //INSTANCIAMOS EL API SERVICE PARA ENVIAR LA NOTIFICACIÓN AL SERVICIO DE FIREBASE
                    apiService.sendNotification(emisor)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    //CUANDO ENVIAS UNA PETICIÓN A UN SERVICIO Y TE DEVUELVE 200 = OK
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MensajeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }


// 9.2.5  LEER MENSAJES DENTRO DEL CHAT
    //Este activity muestra el listado del chat del remitente
    //DECLARAMOS LOS PARÁMETROS DE ENTRADA DEL MÉTODO
    private void leerMensajes(final String emisor, final String receptor, final String imageurl){
        mchat = new ArrayList<>();
        //HACEMOS referencia A LA  base de datos y se extrae los datos de la tabla CHATS
        /*reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            //SE HACE LA LECTURA DE LA RUTA ( ME VA LISTAR LOS ELEMENTOS DE LA TABLA CHATS)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ME VA PERMITIR LIMPIAR PARA EVITAR DUPLICADOS ( SI ES QUE MODIFICO DENTRO DE FIREBASE)
                mchat.clear();
                //RECORRER LA TABLA CHATS
                //CON LA SENTENCIA REPETITIVA "FOR "
                // snapshot : ESTÁ GUARDADO EL CONTENIDO DE LA TABLA
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceptor().equals(emisor) && chat.getEmisor().equals(receptor) ||
                            chat.getReceptor().equals(receptor) && chat.getEmisor().equals(emisor)){
                        mchat.add(chat);
                        // SE AÑADE EL OBJETO CHAT DE LA TABLA CHAT
                    }
                    //Estos datos de los chats se usan para llenar el message adapter que se usara para llenar el recyclerview
                    //MANDO ESTOS PARÁMETROS AL ADAPTADOR
                    mensajeAdapter = new MensajeAdapter(MensajeActivity.this, mchat, imageurl);

//9.2.6 EVENTO PARA COPIAR A PORTAPAPELES
                    //Se llama al evento creado en el MensajeAdapter para crear evento al seleccionar un item del mensaje adapter
                    mensajeAdapter.setOnClickListener(new View.OnClickListener() {
                       //ESTE CÓDIGO HABILITA LA COMPATIBILIDAD CON ANDROID VERS.  M
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            //ClipboardManager : EL ADMINISTRADOR DE PORTAPAPELES
                            //SE UBICA LA POSICIÓN DEL MSJ
                            String texto= mchat.get(recyclerView.getChildAdapterPosition(v)).getMensaje();
                            //INSTANCIAMOS EL SERVICIO
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // SE COPIA EL MSJ AL PORTAPAPELES
                            ClipData clip = ClipData.newPlainText("text",  texto);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MensajeActivity.this, "Mensaje copiado al portapapeles", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //SE ENVÍA AL ADAPTADOR , Y DEL ADAPTADOR LO MANDA AL RECYCLE VIEW
                    recyclerView.setAdapter(mensajeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    //CODIGO PARA ALMACENAR EL USUARIO PREVIO
    private void currentUser(String idUsuario){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", idUsuario);
        editor.apply();
    }

//CONECTADO O DESCONECTADO
    //Se cambia el estado del usuario cada vez que este ingrese al activity
    private void status(String status){
        //reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());
        //ACTUALIZA EL ESTADO COMO HASHMAP EN LA BASE DE DATOS
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("estado", status);

        //reference.updateChildren(hashMap);
        //ACA SOLO SE RECIBE EL PARÁMETRO
    }
    // ACÁ SE ESTABLECE EL ESTADO
    //Este método configura el estado del usuario a ONLINE cuando este use el aplicativo
    @Override
    protected void onResume() {
        super.onResume();
        status("En línea");
        currentUser(idUsuario);
    }
    //Este método configura el estado del usuario a OFFLINE cuando este tenga el aplicativo cerrado o en segundo plano
    @Override
    protected void onPause() {
        super.onPause();
        //reference.removeEventListener(seenListener);
        status("Desconectado");
        currentUser("none");
    }
}
