package com.instaoffice.serpost.Adapter;
// AQUÍ ESTOY USANDO UN ADAPTADOR PROPIO , PARA LLENAR LOS RECLICLE VIEW
//AQUÍ YA ESTOY UTILIZANDO UNO DE LOS MODELOS (USUARIO)
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
/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/
import com.instaoffice.serpost.MensajeActivity;
import com.instaoffice.serpost.Model.Chat;
import com.instaoffice.serpost.Model.Usuario;
import com.instaoffice.serpost.R;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {
//1. PARA LLENAR EL ADAPTADOR NECESITAMOS ESTOS ELEMENTOS
    private Context mContext;
    // QUIERO QUE ESTE LISTADO TENGA EL FORMATO DEL MODELO USUARIOS
    //CADA ELEMENTO DE ESTA LISTA VA TENER TODOS LOS ATRIBUTOS DEL MODELO
    //LA LISTA VA LLEVAR POR NOMBRE "mUsuarios"
    private List<Usuario> mUsuarios;
    private boolean visto;

    String ultimoMensaje;
    String ultimaFecha;

//2.- COSTRUCTOR PARA QUE USUARIO ADPTER SE INSTANCIE CON ELLOS
    public UsuarioAdapter(Context mContext, List<Usuario> mUsuarios, boolean visto){
        this.mUsuarios = mUsuarios;
        this.mContext = mContext;
        this.visto = visto;
    }
//3.- CREO EL DISEÑO PARA ENLAZAR LOS DATOS A la vista
    //PARA LLENAR EL RECYCLE VIEW NECESITO EL MÉTODO onCreateViewHolder
    //ESTE LLENA TODOS LOS ELEMENTOS VISUALES DEL DISEÑO (USUARIO_ITEM)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.usuario_item, parent, false);
       //ESTO LO VOY A GUARDAR EN  VIEW HOLDER Y LO MANDO A VISTA
        return new UsuarioAdapter.ViewHolder(view);
    }

//4.-
    @Override
    //ACA VOY A ENLAZAR LOS ELEMENTOS DE LA BASE DE DATOS  CON LA VISTA (VIEW)
    //ARRIBA SOLO HE CREADO EL DISEÑO Y AQUÍ VOY A ENLAZAR ESOS DATOS AL DISEÑO
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //AQUÍ CREO UN OBJETO DEL TIPO USUARIO CON ESTO  LE VOY A DECIR A LA LISTA QUE CREE
        //AL PRINCIPIO , QUE ME TRAIGA LA POSICIÓN
        //AQUÍ EL NOMBRE DE USUARIO
        final Usuario usuario = mUsuarios.get(position);
        holder.nombreUsuario.setText(usuario.getNombreUsuario());
        //AQUÍ LA IMAGEN
        //SI ES POR DEFAULT, TE VA MANDAR EN ESTE CASO LA IMAGEN DE ANDROID
        if (usuario.getImagenURL().equals("default")){
            holder.imagenPerfil.setImageResource(R.mipmap.ic_launcher);
        } else {
            //PERO SINO , ENTONCES USAMOS LA LIBRERÍA GLIDE PARA INSERTAR UN
            // ELEMENTO DENTRO DE IMAGEN PERFIL
            Glide.with(mContext).load(usuario.getImagenURL()).into(holder.imagenPerfil);
        }

//3.-PARA MOSTRAR EN LISTA DE LOS USUARIOS Y CHATS  (CONSISTENCIACIÓN)
        if (visto){
            lastMessage(usuario.getId(), holder.ultimoMensaje,holder.ultimaFecha);
        //SI NO ES ASÍ , QUE SEA ELIMINADO
        } else {
            holder.ultimoMensaje.setVisibility(View.GONE);
            holder.ultimaFecha.setVisibility(View.GONE);
        }

        // PARA VER SI EL USUARIO ESTÁ ACTIVO
        //SI ES TRUE ( VA CAMBIAR DE COLOR VERDE)
        if (visto){
            if (usuario.getEstado().equals("En línea")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
                //Y SI NO CAMBIA A PLOMO
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        //
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
// EVENTO CUANDO SE HAGA CLICK AL ITEM ( EN EL FRAGMENT CHAT TE VA A REDIRIGIR A CHAT)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //QUIERO QUE CUANDO HAGA CLICK ME MANDE A MENSAJE ACTIVITY
                Intent intent = new Intent(mContext, MensajeActivity.class);
                //Y QUIERO QUE SE CGUARDE EL ID DEL USUARIO A QUIEN LE ESTOY HACIENDO CLICK
                intent.putExtra("idUsuario", usuario.getId());
                mContext.startActivity(intent);
            }
        });
    }

    //ESTO IBA ARRIBA ES LA RELACIÓN DEL DISEÑO  CON LA VISTA
    @Override
    public int getItemCount() {
        return mUsuarios.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
//SON ELEMENTOS PARA INSTANCIAR
        public TextView nombreUsuario;
        public ImageView imagenPerfil;
        private ImageView img_on;
        private ImageView img_off;
        private TextView ultimoMensaje;
        private TextView ultimaFecha;

        public ViewHolder(View itemView) {
            super(itemView);
//AQUÍ SE INSTANCIAN
            nombreUsuario = itemView.findViewById(R.id.nombreusuario);
            imagenPerfil = itemView.findViewById(R.id.imagenPerfil);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            ultimoMensaje = itemView.findViewById(R.id.ultimoMensaje);
            ultimaFecha = itemView.findViewById(R.id.ultimaFecha);
        }
    }

//ESTA ES LA LÓGICA DEL ÚLTIMO MENSAJE
    private void lastMessage(final String idUsuario, final TextView ultimoMensaje, final TextView ultimaFecha){
        this.ultimoMensaje = "default";
        this.ultimaFecha ="default";
        /*final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //VOY A CONSULTAR CON LA TABLA CHATS
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //SENTENCIA REPETITIVA CON SNAPSHOT
                //VA AGREGAR UN EVENTO DE LA CONSULTA
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    // Y PREGUNTA : SI FIREBASEUSER ES DIFERENTE A NULO
                   if (firebaseUser != null && chat != null) {
                       // SI HAY UN ELEMENTO EN EL CHAT EN EL CUAL EL RECEPTOR SEA EL FIREBASEUSER
                       //ESTA ES UNA CONSULTA EN DOBLE SENTIDO : SI ES QUE EL USUARIO ACTUA COMO EMISOR Y RECEPTOR
                       // ENTONCES VA A BUSCAR EL REGISTRO DE CHAT DONDE A Y B HAYAN PARTICIPADO EN AMBOS ROLES
                        if (chat.getReceptor().equals(firebaseUser.getUid()) && chat.getEmisor().equals(idUsuario) ||
                                chat.getReceptor().equals(idUsuario) && chat.getEmisor().equals(firebaseUser.getUid())) {
                            //GUARDA EL ULTIMO MSJ Y LA UTIMA FECHA
                            //AQUI ESTOY UTILIZANDO  EL OBJETO DE CHAT Y LO ESTOY ALMACENANDO
                            UsuarioAdapter.this.ultimoMensaje = chat.getMensaje();
                            UsuarioAdapter.this.ultimaFecha = chat.getFecham();
                        }
                    }
                }
                //EN CASO NO SE HAYA MANDANDO NINGÚN MSJ , ENTONCES NO VA VER NINGUN REGISTRO
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
        });*/
    }
}
