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
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
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
//Se crea las variables de los elementos que compone el activity mensaje
    //Imagen y nombre del usuario que recibe el mensaje
    CircleImageView imagenPerfil;
    TextView nombreUsuario;
    //Para inicializar Firebase
    FirebaseUser fuser;
    DatabaseReference reference;
    //Textbox y botón para enviar mensajes
    ImageButton btn_enviar;
    ImageButton btn_adjuntar;
    EditText text_enviar;
    //Adaptador que llenará el recycler view con la lista de los mensajes enviados y recibidos
    MensajeAdapter mensajeAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    //
    //Se inicializa las variables para el servicio de Storage de Firebase
    StorageReference storageReference;
    //
    Intent intent;
    ValueEventListener seenListener;
    //Variable del idUsuario y tipo de dato fecha
    String idUsuario;
    Date fecha;
    APIService apiService;
    //Activa la notificación
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);
        //Se llama y configura a la barra superior que contiene la imagen y el nombre del usuario destino
        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Para regresar al activity principal
                startActivity(new Intent(MensajeActivity.this, MainActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        //Se instancia al api service
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //Se inicializa el recyclerview
        recyclerView = findViewById(R.id.recycler_view_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Se inicializa los elementos que componen el activity
        imagenPerfil = findViewById(R.id.imagenPerfil);
        nombreUsuario = findViewById(R.id.nombreusuario);
        btn_enviar = findViewById(R.id.btn_enviar);
        btn_adjuntar=findViewById(R.id.btn_adjuntar);
        text_enviar = findViewById(R.id.text_enviar);
        //fecha= Calendar.getInstance().getTime();
        //Se extrae el elemento que fue enviado desde el activity anterior
        intent = getIntent();
        idUsuario = intent.getStringExtra("idUsuario");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //
        storageReference = FirebaseStorage.getInstance().getReference("ficheros");
        //
        //Lógica para botón para enviar mensaje
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fecha= Calendar.getInstance().getTime();
                String fecham = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                notify = true;
                String mensaje = text_enviar.getText().toString();
                if (!mensaje.equals("")){
                    enviarMensaje(fuser.getUid(), idUsuario, mensaje,fecham);
                } else {
                    Toast.makeText(MensajeActivity.this, "No puede enviar mensaje vacío", Toast.LENGTH_SHORT).show();
                }
                text_enviar.setText("");
            }
        });
        //Lógica para botón para enviar adjunto
        btn_adjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    SeleccionarArchivo();
            }
        });
        //Conexión con Firebase, se obtiene los datos del usuario de la tabla usuario:
        //En caso el usuario no haya subido alguna foto se utilizará la imagen local por defecto
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                nombreUsuario.setText(usuario.getNombreUsuario());
                if (usuario.getImagenURL().equals("default")){
                    imagenPerfil.setImageResource(R.mipmap.ic_launcher);
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(usuario.getImagenURL()).into(imagenPerfil);
                }

                leerMensajes(fuser.getUid(), idUsuario, usuario.getImagenURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mensajeVisto(idUsuario);
    }
    //Solo PDF
   /* private void SeleccionarArchivo() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar archivo PDF"),1);
    }*/
   //Cualquier archivo
    private void SeleccionarArchivo() {
        Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar archivo PDF"),1);
    }
    //Cuando se ha seleccionado el archivo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            subirArchivo(data.getData());
        }
    }
    //Obtiene la extensión del fichero del archivo
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void subirArchivo(Uri data) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando");
        pd.show();
        //Se sube el archivo con el nombre del tiempo del sistema y la extensión en PDF
        //StorageReference reference = storageReference.child(System.currentTimeMillis()+".pdf");
        //Se sube el archivo con el nombre del tiempo del sistema y la extensión del archivo  (PDF,XSLX,JPG,PNG,PPTX, MP3...)
        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(data));
    reference.putFile(data)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
            double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
            pd.setMessage("Cargando: "+ (int)progress+"%");
        }
    });
    }

    //Este método configura el estado del mensaje para que figure como Leido
    private void mensajeVisto(final String idUsuario){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
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
        });
    }
    //Este método se usa para enviar el mensaje al usuario destino
    private void enviarMensaje(String emisor, final String receptor, String mensaje, String fecha){
        //se referencia la instancia de la base de datos
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("emisor", emisor);
        hashMap.put("receptor", receptor);
        hashMap.put("mensaje", mensaje);
        hashMap.put("visto", false);
        hashMap.put("fecham",fecha);
        //Se ingresa todos los datos pertinentes para enviar un mensaje y se agrega a la tabla Chats
        reference.child("Chats").push().setValue(hashMap);


        // Se agrega el usuario a la tabla de ChatList
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(idUsuario);
        // Se agrega evento a la instancia del chatref que se acaba de crear
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //Se configura el id del chat con el del usuario
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(idUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(idUsuario)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());

        final String msg = mensaje;

        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (notify) {
                    enviarNotificacion(receptor, usuario.getNombreUsuario(), msg, usuario.getId());
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Este método enviará la notificación
    private void enviarNotificacion(String receptor, final String nombreUsuario, final String mensaje, final String emisor){

        //Se instancia la referencia de la tabla de Tokens
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receptor);
        query.addValueEventListener(new ValueEventListener() {
            //La notificación contendrá los siguientes datos
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    //"New Message" => "Nuevo Mensaje"
                    Data data = new Data(fuser.getUid(), R.drawable.ic_message, nombreUsuario+": "+mensaje, "Nuevo Mensaje",
                            idUsuario,emisor);

                    Emisor emisor = new Emisor(data, token.getToken());

                    apiService.sendNotification(emisor)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
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
        });
    }
    //Este activity muestra el listado del chat del remitente
    private void leerMensajes(final String emisor, final String receptor, final String imageurl){
        mchat = new ArrayList<>();
        //Se referencia la instancia de base de datos y se extrae los datos de la tabla Chats
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                //Solo se obtendrán los mensajes de los chats que coincidan con el remitente o el que recibe el mensaje
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceptor().equals(emisor) && chat.getEmisor().equals(receptor) ||
                            chat.getReceptor().equals(receptor) && chat.getEmisor().equals(emisor)){
                        mchat.add(chat);
                    }
                    //Estos datos de los chats se usan para llenar el message adapter que se usara para llenar el recyclerview
                    mensajeAdapter = new MensajeAdapter(MensajeActivity.this, mchat, imageurl);
                    //Se llama al evento creado en el MensajeAdapter para crear evento al seleccionar un item del mensaje adapter
                    mensajeAdapter.setOnClickListener(new View.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            String texto= mchat.get(recyclerView.getChildAdapterPosition(v)).getMensaje();
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("text",  texto);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MensajeActivity.this, "Mensaje copiado al portapapeles", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(mensajeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //CODIGO PARA ALMACENAR EL USUARIO PREVIO
    private void currentUser(String idUsuario){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", idUsuario);
        editor.apply();
    }
    //Se cambia el estado del usuario cada vez que este ingrese al activity
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("estado", status);

        reference.updateChildren(hashMap);
    }
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
        reference.removeEventListener(seenListener);
        status("Desconectado");
        currentUser("none");
    }
}
