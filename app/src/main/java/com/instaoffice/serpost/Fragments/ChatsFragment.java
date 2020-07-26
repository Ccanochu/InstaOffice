package com.instaoffice.serpost.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.instaoffice.serpost.Adapter.UsuarioAdapter;
import com.instaoffice.serpost.Model.Chatlist;
import com.instaoffice.serpost.Model.Usuario;
import com.instaoffice.serpost.Notifications.Token;
import com.instaoffice.serpost.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    //Se crean las variables de los elementos del Activity
    //Se crea el recycler view
    private RecyclerView recyclerView;
    //Se crea el adaptador de usuario y la lista de usuarios
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> mUsuarios;
    //Se crea las variables para el Firebase
    FirebaseUser fuser;
    DatabaseReference reference;
    //Se crea la lista de Chats
    private List<Chatlist> listaUsuariosChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        //Se instancia los elementos del fragment. En este caso el recyclerview
        recyclerView = view.findViewById(R.id.recycler_view_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Se llama la instancia del usuario autenticado
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //Se crea una lista de usuario
        listaUsuariosChat = new ArrayList<>();
        //Se llama la instancia de base de datos de la tabla ChatList
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        //Si quieres leer datos de una ruta de acceso y detectar los posibles cambios, usa el método siguiente
        reference.addValueEventListener(new ValueEventListener() {
            //Puedes usar el método onDataChange() para leer una instantánea estática del contenido
            // de una ruta de acceso determinada y ver cómo se encontraba en el momento del evento.
            //En este caso se llaman todos los chat list disponibles del usuario autenticado
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuariosChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    listaUsuariosChat.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Se llama al metodo actualizar TOken
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }
    //Se actualiza el token
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
    //Se obtiene las listas de chat del usuario
    private void chatList() {
        mUsuarios = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsuarios.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    for (Chatlist chatlist : listaUsuariosChat){
                        if (usuario.getId().equals(chatlist.getId())){
                            mUsuarios.add(usuario);
                        }
                    }
                }
                usuarioAdapter = new UsuarioAdapter(getContext(), mUsuarios, true);
                recyclerView.setAdapter(usuarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
