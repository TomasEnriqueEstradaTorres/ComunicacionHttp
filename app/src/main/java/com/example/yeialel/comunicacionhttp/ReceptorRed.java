package com.example.yeialel.comunicacionhttp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ReceptorRed extends BroadcastReceiver {
    /**Es paa poder recibir el context de la actividad donde se mostrada el mensaje
     * y para que el metodo 'getSystemService' que tambien hereda de 'context' funcione     */
    private Context context;
    //Sirven para verificar el estado de la red
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    //CONSTRUCTOR - recibe un context de la activity principal
    public ReceptorRed(Context context) {
        this.context = context;
    }


    /** El metodo llamara a otro metodo que verifica el estado de la red      */
    @Override
    public void onReceive(Context context, Intent intent) {
        actualizaEstadoRed();
    }

    public void actualizaEstadoRed(){
        try {
            // El metodo 'getSystemService()' funciona porque tambien hereda de 'Context'
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                // Si hay conexión a Internet en este momento - Con el 'context' estamos diciendo donde se mostrada el mensaje
                Toast mensajeEstadoRed = Toast.makeText(context, "ESTADO DE LA RED: " + networkInfo.getState(), Toast.LENGTH_LONG);
                mensajeEstadoRed.show();

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // sirve para ver a que red estas conectado
                    Toast mensajeNombreWifi = Toast.makeText(context, "Nombre RED WI-FI: " + networkInfo.getExtraInfo(), Toast.LENGTH_LONG);
                    mensajeNombreWifi.setGravity(Gravity.TOP | Gravity.CENTER, 0, 1500);
                    mensajeNombreWifi.show();
                }
            } else {
                // el objeto es nulo cuando no hay red, por lo tanto este mensaje no se muestra y salta un error que sera controlado abajo
                Toast toast = Toast.makeText(context, "ESTADO DE LA RED: " + networkInfo.getState(), Toast.LENGTH_LONG);
                toast.show();
                Log.i("ERROR EN LA RED", "no hay conexion:" + networkInfo.getState());  // <<<-- funciona internamiente
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(context, "ESTADO DE LA RED: NO CONECTADO", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 1800);
            toast.show();
            //Mensaje interno
            Log.i("-------------> ERROR <--------------", "no hay conexion: " + e);
        }
    }


}
