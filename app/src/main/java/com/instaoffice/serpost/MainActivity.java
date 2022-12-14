package com.instaoffice.serpost;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/
import com.instaoffice.serpost.Fragments.ChatsFragment;
import com.instaoffice.serpost.Fragments.FragmentPerfil;
import com.instaoffice.serpost.Fragments.FragmentUsuarios;
import com.instaoffice.serpost.Model.Chat;
import com.instaoffice.serpost.Model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    //Se crea las variables que se usaran en el activity principal
    CircleImageView imagenPerfil;
    TextView nombreusuario;
    //se crea las variables para Firebase
    /*FirebaseUser firebaseUser;
    DatabaseReference reference;*/

//1.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //2.-
        //Se instancia los elementos de la barra superior a la presente clase
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        imagenPerfil = findViewById(R.id.imagenPerfil);
        nombreusuario = findViewById(R.id.nombreusuario);
//3.-PARA LLENAR NUESTRA BARRA SUPERIOR CON LOS DATOS DEL USUARIO LOGEADO
        //Obtener usuario con sesi??n activa
        /*firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseUser.getUid());*/
        //Si quieres leer datos de una ruta de acceso y detectar los posibles cambios, usa el m??todo siguietne
        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            //Puedes usar el m??todo onDataChange() para leer una instant??nea est??tica del contenido
            // de una ruta de acceso determinada y ver c??mo se encontraba en el momento del evento.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                nombreusuario.setText(usuario.getNombreUsuario());
                if (usuario.getImagenURL().equals("default")){
                    imagenPerfil.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(usuario.getImagenURL()).into(imagenPerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
// LAS VISTAS CON LAS QUE SE VA TRABAJAR
        //Se inicializa el tab_Layout_main que contiene las opciones para navegar |Chats|Usuario|Perfil
        //El view_pager_main que mostrar?? el contenido a escoger del tab_layout.
        // Permite agregar fragments
        final TabLayout tabLayout = findViewById(R.id.tab_layout_main);
        final ViewPager viewPager = findViewById(R.id.view_pager_main);

 //4.- CONTAR LOS MSJS NO VISTOS
        //Se obtiene la referencia de la instacia de base de datos de Chat
        //Se obtiene los datos de chat
        /*reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            //
            //L??gica para configurar la cantidad de mensajes no vistos
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int sinVer = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceptor().equals(firebaseUser.getUid()) && !chat.isVisto()){
                        sinVer++;
                    }
                }

                //Se especifica la cantidad de mensajes sin leer seg??n la condicional
                // SI ES QUE TENGO CHATS SIN VER O NO
                if (sinVer == 0){
                    //viewPagerAdapter : VA CREAR LOS FRAGMENTS
                    viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                } else {
                    viewPagerAdapter.addFragment(new ChatsFragment(), "("+sinVer+") Chats");
                }

                //Se a??ade los fragment de Usuarios y Perfil al view Pager
                viewPagerAdapter.addFragment(new FragmentUsuarios(), "Usuarios");
                viewPagerAdapter.addFragment(new FragmentPerfil(), "Perfil");
                //Se a??ade el contenido agregado al ViewPager
                viewPager.setAdapter(viewPagerAdapter);
                //Se a??ade las opciones a??adidas al viewPager[Chats|Usuarios|Perfil]
                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }
    //Se a??ade el men?? a la barra
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //Se configura la l??gica a las opciones del men??
    //Cerrar sesi??n
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.logout:
                //FirebaseAuth.getInstance().signOut();
                //Al cerrar sesi??n se env??a a la p??gina de Start
                //Se activa Flag_Activity_Clear_Top
                //Si est?? configurado, y la actividad que se est?? iniciando ya se
                // est?? ejecutando en la tarea actual, en lugar de iniciar una nueva
                // instancia de esa actividad, todas las dem??s actividades se cerrar??n y
                // esta intenci??n se entregar?? al (ahora en arriba) actividad anterior como una nueva intenci??n.
                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
        }

        return false;
    }
    //Se configura los par??metros del ViewPagerAdapter dentro de esta clase
    class ViewPagerAdapter extends FragmentPagerAdapter {
        //Se crean las variables
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    //Se obtiene y cambia el cambioEstado del usuario
    //|Activo|Offline|
    private void cambioEstado(String estado){
        //reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("estado", estado);

        //reference.updateChildren(hashMap);
    }
    //Cuando la aplicaci??n est?? en primer plano en el m??vil
    @Override
    protected void onResume() {
        super.onResume();
        cambioEstado("En l??nea");
    }
    //Cuando la aplicaci??n est?? cerrada o en segundo plano en el m??vil
    @Override
    protected void onPause() {
        super.onPause();
        cambioEstado("Desconectado");
    }
}
