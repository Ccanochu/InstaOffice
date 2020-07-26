package com.instaoffice.serpost.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.instaoffice.serpost.Model.Usuario;
import com.instaoffice.serpost.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class FragmentPerfil extends Fragment {
    //Se inicializa las variables que usará el fragment
    CircleImageView image_profile;
    TextView nombreUsuario;
    EditText departamentoUsuario;
    EditText provinciaUsuario;
    EditText sedeUsuario;
    ImageButton guardarCambios;
    //Se inicializa las variables para FIrebase
    DatabaseReference reference;
    FirebaseUser fuser;
    //Se inicializa las variables para el servicio de Storage de Firebase
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        //Se instancia la imagen y el nombre de usuario
        image_profile = view.findViewById(R.id.imagenPerfil);
        nombreUsuario = view.findViewById(R.id.nombreusuario);
        departamentoUsuario = view.findViewById(R.id.departamentoUsuario);
        provinciaUsuario = view.findViewById(R.id.provinciaUsuario);
        sedeUsuario=view.findViewById(R.id.sedeUsuario);
        guardarCambios=view.findViewById(R.id.guardarCambio);

        //Se obtiene la referencia de la instancia de base de datos para subir la foto
        storageReference = FirebaseStorage.getInstance().getReference("imagenes");
        //Se obtiene el usuario atenticado y se busca la referencia del usuario en la tabla de usuarios
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                nombreUsuario.setText(usuario.getNombreUsuario());
                departamentoUsuario.setText(usuario.getDepartamento());
                provinciaUsuario.setText(usuario.getProvincia());
                sedeUsuario.setText(usuario.getSede());
                if (usuario.getImagenURL().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    if(isAdded()) {
                        Glide.with(getContext()).load(usuario.getImagenURL()).into(image_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirImagen();
            }
        });

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_departamento = departamentoUsuario.getText().toString();
                String txt_provincia = provinciaUsuario.getText().toString();
                String txt_sede = sedeUsuario.getText().toString();
                if (TextUtils.isEmpty(txt_departamento) || TextUtils.isEmpty(txt_provincia) || TextUtils.isEmpty(txt_sede)) {
                    Toast.makeText(getContext(), "Se debe llenar todos los casilleros", Toast.LENGTH_SHORT).show();
                }else {
                    actualizar(txt_departamento,txt_provincia,txt_sede);
                    Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    private void actualizar(String txt_departamento, String txt_provincia, String txt_sede) {

        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("departamento", txt_departamento);
        hashMap.put("provincia",txt_provincia);
        hashMap.put("sede",txt_sede);

        reference.updateChildren(hashMap);
    }

    //Este método permite abrir el explorador de archivos para subir una imagen
    private void abrirImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    //Este método permite obtener la extensión del fichero de imagen
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    //Este método permite implementar la lógica de subir la imagen
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Cargando");
        pd.show();
        //Si la ruta imagen no es vacia, se sube la imagen al repositorio de Storage de Firebase
        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imagenURL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No se ha seleccionado imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Subida en progreso", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}
