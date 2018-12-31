package com.example.yeialel.comunicacionhttp;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText ingresarDireccion;  // donde se ingresara la direccion web a descargar
    private Button descargarURL;  // el boton que realiza la descarga
    private String direccionURL;  // almacenara la direccion web que queremos descargar su codigo
    private ReceptorRed receptor;  // Servira para poder verificar el estado de la red
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();

        ingresarDireccion =(EditText) findViewById(R.id.editTextBuscar);
        descargarURL = (Button) findViewById(R.id.buttonDescargar);

        /**con esto llamaremos al metodo que verificara el estado de la red que esta en la clase 'ReceptorRed.class'
         * y le pasamos un 'Context' de la actividad para que el el Toast reciba en donde se mostrada el mensaje y
         * tambien para que el servicio 'ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)'
         * pueda funcionar y verificar es estado de la red.          */
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receptor = new ReceptorRed(this);
        this.registerReceiver(receptor, filter);

        // se pone a la escucha el boton
        descargarURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sera para pasar la url a la activity que tiene el WebView
                Intent EnvioDatosWeb = new Intent(getApplicationContext(), Descarga.class);
                direccionURL = ingresarDireccion.getText().toString();  // recive la direccion web a descargar
                direccionURL = "https://" + direccionURL;

                //Envia el dato de la pagina web a la otra vista 'Descarga.class'
                EnvioDatosWeb.putExtra("valorEnviado", direccionURL);
                startActivity(EnvioDatosWeb);
                // usando '(getApplicationContext()' se asegura que no halla fallos por contenedor
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /**Cuando se reanuda una actividad desde el estado de pausa, el sistema llama al método onResume().
         * Este método cada vez que la actividad pasa a primer plano reiniciara la aplicacion, incluso
         * cuando esta se crea por primera vez. Por ende, se debe implementar onResume() para inicializar los componentes
         * que lancen en onPause() y para realizar otras inicializaciones que deban ejecutarse cada vez que la actividad
         * entre en el estado de reanudación (por ejemplo, activar animaciones e inicializar componentes que solo se
         * usen mientras el usuario ponen en foco a la actividad).       */
        if(receptor != null){
            this.registerReceiver(receptor, intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /**Sirve para que el sistema no consuma recursos mientras la aplicacion se encuentra en pausa.
         * Liberar recursos del sistema, como receptores de mensajes, manejadores de sensores
         * (como GPS [sistema de posicionamiento global]), u otros recursos que puedan afectar la duración
         * de la batería mientras tu actividad está en pausa y el usuario no los necesita.       */
        if(receptor != null){
            this.unregisterReceiver(receptor);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        /**Damos de baja el receptor de broadcast cuando se destruye la aplicación para evitar un
         * consumo excesivo de los recursos del sistema.          */
        if (receptor != null) {
            this.unregisterReceiver(receptor);
        }
    }

}
