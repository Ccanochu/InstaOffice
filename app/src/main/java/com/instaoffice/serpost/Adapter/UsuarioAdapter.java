package com.instaoffice.serpost.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instaoffice.serpost.MensajeActivity;
import com.instaoffice.serpost.Model.Chat;
import com.instaoffice.serpost.Model.Usuario;
import com.instaoffice.serpost.R;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private Context mContext;
    private List<Usuario> mUsuarios;
    private boolean visto;

    String ultimoMensaje;
    String ultimaFecha;

    public UsuarioAdapter(Context mContext, List<Usuario> mUsuarios, boolean visto){
        this.mUsuarios = mUsuarios;
        this.mContext = mContext;
        this.visto = visto;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.usuario_item, parent, false);
        return new UsuarioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Usuario usuario = mUsuarios.get(position);
        holder.nombreUsuario.setText(usuario.getNombreUsuario());
        if (usuario.getImagenURL().equals("default")){
            holder.imagenPerfil.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(usuario.getImagenURL()).into(holder.imagenPerfil);
        }

        if (visto){
            lastMessage(usuario.getId(), holder.ultimoMensaje,holder.ultimaFecha);
        } else {
            holder.ultimoMensaje.setVisibility(View.GONE);
            holder.ultimaFecha.setVisibility(View.GONE);
        }

        if (visto){
            if (usuario.getEstado().equals("En línea")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MensajeActivity.class);
                intent.putExtra("idUsuario", usuario.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsuarios.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nombreUsuario;
        public ImageView imagenPerfil;
        private ImageView img_on;
        private ImageView img_off;
        private TextView ultimoMensaje;
        private TextView ultimaFecha;

        public ViewHolder(View itemView) {
            super(itemView);

            nombreUsuario = itemView.findViewById(R.id.nombreusuario);
            imagenPerfil = itemView.findViewById(R.id.imagenPerfil);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            ultimoMensaje = itemView.findViewById(R.id.ultimoMensaje);
            ultimaFecha = itemView.findViewById(R.id.ultimaFecha);
        }
    }


    private void lastMessage(final String idUsuario, final TextView ultimoMensaje, final TextView ultimaFecha){
        this.ultimoMensaje = "default";
        this.ultimaFecha ="default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                   if (firebaseUser != null && chat != null) {
                        if (chat.getReceptor().equals(firebaseUser.getUid()) && chat.getEmisor().equals(idUsuario) ||
                                chat.getReceptor().equals(idUsuario) && chat.getEmisor().equals(firebaseUser.getUid())) {
                            UsuarioAdapter.this.ultimoMensaje = chat.getMensaje();
                            UsuarioAdapter.this.ultimaFecha = chat.getFecham();
                        }
                    }
                }

                switch (UsuarioAdapter.this.ultimoMensaje){
                    case  "default":
                        ultimoMensaje.setText("Sin Mensajes");
                        break;

                    default:
                        ultimoMensaje.setText(UsuarioAdapter.this.ultimoMensaje);
                        break;
                }
                switch (UsuarioAdapter.this.ultimaFecha){
                    case  "default":
                        ultimaFecha.setText("Sin Fecha");
                        break;

                    default:
                        ultimaFecha.setText(UsuarioAdapter.this.ultimaFecha);
                        break;
                }
                UsuarioAdapter.this.ultimoMensaje = "default";
                UsuarioAdapter.this.ultimaFecha ="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
