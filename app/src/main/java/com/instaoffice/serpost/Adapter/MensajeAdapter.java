package com.instaoffice.serpost.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.instaoffice.serpost.Model.Chat;
import com.instaoffice.serpost.R;

import java.util.List;
// TENEMOS 3 RECYCLE VIEW DE USUARIOS 2 UTILIZO CON USUARIO ADAPTER Y EL 3RO ES MSJ ADAPTER
// ESTE VA SER UTILIZADO EN MENSAJE ACTIVITY
//ME VA LISTAR EL MSJ QUE YO ENVIE Y EL QUE RECIBI (POR IZQUIERDA Y POR DERECHA)
public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
//1.-
// AQUI CREO LAS VARIABLES QUE ME VAN A PERMITIR IDENTIFICAR LA POSICIÓN DE LOS MSJS (DE LA IZQUIERDA Y DERECHA)
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;//ME VA LISTAR OBJETOS TIPO CHAT
    private String imagenurl;
    //VARIABLE DE FIREBASE
    //FirebaseUser fuser;
//2.-
//CONSTRUCTOR PARA QUE MSJ ADAPTER SE INSTANCIADO CON ESTOS PARÁMETROS
    public MensajeAdapter(Context mContext, List<Chat> mChat, String imagenurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imagenurl = imagenurl;
    }
// 3.- POSCIÓN DE MSJS DENTRO DEL CHAT
    //AQUÍ SOLO RECIBE EL PARÁMETRO PARA REDIRECCIONAR LOS ELEMENTOS DEL RECYCLER VIEW (MSJS)
    @NonNull
    @Override
    //AQUÍ VA EVALUAR POR CADA CHAT  (SI UN MSJ PERTENECE A UN USUARIO QUE ES EMISOR ESTE
    //VA ESTAR POSCICIONADO A LA IZQUIERDA Y EL OTRO USUARIO COMO RECEPTOR PARA
    //LA DERECHA
    //SE VAN A CREAR LOS ELEMENTOS QUE SE VAN A LISTAR EN EL RECYCLE VIEW (EL ITEM DE LA DERECHA Y EL DE LA IZQUIERDA
     public MensajeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //SI ES A LA DERECHA
        if (viewType == MSG_TYPE_RIGHT) {
            //CREO UN ITEM QUE MA PERMITIR POSICIONAR A LA DERECHA EL MENSAJE
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            //LE ASIGNO EL EVENTO DE CLICK
            view.setOnClickListener(this);
            return new MensajeAdapter.ViewHolder(view);
        } else {
            //SI ES A LA IZQUIERDA
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            view.setOnClickListener(this);
            return new MensajeAdapter.ViewHolder(view);
        }
    }
//4.- ENLAZO LOS DATOS DEL DISEÑO LAS VARIABLES LOCALES
      //Con el objeto View Holder conseguimos que esa referencia se establezca cuando se crea la vista
      // y se guarde para no tener que volver a buscar.
      @Override
      public void onBindViewHolder(@NonNull MensajeAdapter.ViewHolder holder, int position) {
      //ASIGNANDO VALORES A LOS ELEMENTOS DEL DISEÑO
        Chat chat = mChat.get(position);//CREO UN OBJETO DEL TIPO CHAT
        holder.show_message.setText(chat.getMensaje());//AQUI LE AGREGO EL MSJ HACIA EL SHOW MSJ(ES EL MSJ EN SI)
        holder.txt_fecham.setText(chat.getFecham());//VAMOS A GUARDAR EL TEXTO QUE SE OBTENGA , COMO LA FECHA Y LA IMAGEN
        if (imagenurl.equals("default")){ //HACE LA CONSULTA PARA LA IMAGEN , PARA QUE ENVIE LA IMG POR DEFECTO
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imagenurl).into(holder.profile_image);
        }//Y SI HAY UNA RUTA QUE LE PONGA LA IMAGEN DE LA RUTA Y QUE LO PONGA EN EL PROFILE_IMAGE


//5.- VERIFICACIÓN DEL ULTIMO ELEMENTO DE LA LISTA DE CHATS
        //SI POSCIÓN = A TAMAÑO DE LA LISTA DE CHAT -1
        //Aca me dice si la posición está en la lista de chat "11(dependiendo de la cantidad de msjs) -1"
        //posición 10 == 10
        if (position == mChat.size()-1){//SI HA LLEGADO AL ULTIMO MSJ ENVIADO
            if (chat.isVisto()){//Y SI ESE MSJ YA HA SIDO VISUALIZADO
                // y me va devolver si ha sido visto o no (este dato ya lo tenemos de la lista de chat que fue enviada)
                holder.txt_seen.setText("Visto");//QUE ME DEVUELVA VISTO
            } else {
                holder.txt_seen.setText("Enviado");//SI NO "ENVIADO"
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

 //6.-PARA CONTAR LA CANTIDAD DE ELEMENTOS DENTRO DEL CHAT ( MSJS )
    @Override
    // getItemCount : lo que va hacer este método es  contar la cantidad de los elementos( los mensajes) que se encuentran en el chat
    public int getItemCount() {
        return mChat.size();
    }

//7.- EVENTO DE COPIAR A PORTAPAPELES
// CON ESTE EVENTO ES PARA LOS ELEMENTOS DEL RECYCLEVIEW (EN ESTE CASO LOS MSJS DEL CHAT )
// ESTE EVENTO ME VA PERMITIR COPIAR CUALQUIER TEXTO EN PORTAPAPELES
    // le da un evento a cada elemento del recycle view
    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

//8.- POSICIÓN DE LOS ELEMENTOS DEL CHAT
     //AQUÍ CON EL VIEW HOLDER VOY A POSICIONAR CADA ELEMENTO DEL RECYCLE VIEW
    public  class ViewHolder extends RecyclerView.ViewHolder{
        //AQUI CREO LAS VARIABLES QUE ME VAN A PERMITIR POSICIONAR CADA ELEMENTO DENTRO DE LA INTERFAZ
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public TextView txt_fecham;

        public ViewHolder(View itemView) {
            super(itemView);
            //ASIGNANDO VALORES A LOS ELEMENTOS DEL DISEÑO
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.imagenPerfil);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            txt_fecham = itemView.findViewById(R.id.txt_fecham);
        }
    }

//3.1 POSICIÓN DE MSJS DENTRO DEL CHAT (LÓGICA)
// VAMOS A REALIZAR LA COMPARACIÓN PARA DETERMINAR SI ESTE ELEMENTO ES DE LA IZQUIERDA O DERECHA
    @Override
    public int getItemViewType(int position) {
        //EN ESTE CASO LA AUTH PARA EL USUARIO QUE ESTÁ LOGEADO
        /*fuser = FirebaseAuth.getInstance().getCurrentUser();//AQUÍ HAGO 1 CONSULTA A MI BASE DE DATOS
        if (mChat.get(position).getEmisor().equals(fuser.getUid())){//SI EL EMISOR EQUIVALE AL ID DEL USUARIO
            return MSG_TYPE_RIGHT;//ESE MSJ VA PARA LA DERECHA
        } else {
            return MSG_TYPE_LEFT;// DE LO CONTRARIO PARA LA IZQUIERDA
        }*/
        return 0;
    }
}