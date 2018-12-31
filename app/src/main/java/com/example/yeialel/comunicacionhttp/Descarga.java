package com.example.yeialel.comunicacionhttp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Descarga extends AppCompatActivity {

    private TextView htmlDescargado;  // Es donde se guardara el contenido html descargado.
    private String url;  // direccion url recibida


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);

        htmlDescargado = (TextView) findViewById(R.id.textViewHTMLdescargado);
        /**Con esto se le da scroll al texView, pero usando la etiqueta de ScrollView tambien se le
         * puede aplicar el mismo efecto pero con la posibilidad de ver el tamaño del archivo descargado
         * por medio de la barra de desplazamiento vertical   */
        //htmlDescargado.setMovementMethod(new ScrollingMovementMethod());

        // aqui recibiremos el dato enviado desde la clase 'MainActivity.class'
        Intent datoRecibido = getIntent();
        Bundle extra = datoRecibido.getExtras();
        url = (String) extra.get("valorEnviado");  // se obtiene el valor enviado y se pasa a cadena

        //Ejecutamos el AsyncTask con la URL.
        new DescargaTexto().execute(url);  // enviara la direccion web a descargar
        /**http://ca.wikipedia.org/wiki/Tirant_lo_Blanc  <-- no funciona por que falta la 's' en 'http'
         * https://ca.wikipedia.org/wiki/Tirant_lo_Blanc <-- Funciona por que solo se agrega la letra 's' en 'https'
         * http://www.google.com  <-- si funciona       */
    }


    // AsyncTask que descarga texto de la red
    public class DescargaTexto extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Descargamos el texto pasado por argumento
            return descargaTexto(params[0]); // El primer parametro
        }

        @Override
        protected void onPostExecute(String cadenaTexto) {
            // Cuando ha terminado la tarea, ponemos el texto al TextView
            htmlDescargado.setText(cadenaTexto);  // se aplica el codigo descargado al textView
        }
    }

    private String descargaTexto(String URL) {
        int BUFFER_SIZE = 2000;            //Tamaño del buffer de texto
        BufferedInputStream in;    //Flujo de datos de lectura
        try {
            //Abrimos la conexión
            in = AbreConexionHTTP(URL);
        } catch (IOException e) {
            //Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "------------>>>>> ERROR:\n" + e, Toast.LENGTH_LONG).show();
            Log.i("---->>> AVISO <<<-----", "Este es el error:\n" + e);
            e.printStackTrace();
            return "";
        }

        //Obtenemos un flujo de caracteres
        InputStreamReader inputStreamReader = new InputStreamReader(in);

        char[] inputBuffer = new char[BUFFER_SIZE];        //Buffer de caracteres
        int caracteresLeidos;                            //caracteres leídos
        String stringResultado = "";                        //Resultado de la cadena

        try {
            //Mientras hayan leído caracteres
            while ((caracteresLeidos = inputStreamReader.read(inputBuffer)) > 0) {
                //Convertimos los caracteres a String
                String stringLeido = String.copyValueOf(inputBuffer, 0, caracteresLeidos);
                //Añadimos los caracteres leídos al resultado
                stringResultado += stringLeido;
            }
            //Cerramos la conexión
            in.close();
        } catch (IOException e) {
            //excepción
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return "";
        }
        //Volvemos el resultado
        return stringResultado;
    }


    private BufferedInputStream AbreConexionHTTP(String adrecaURL) throws IOException {
        BufferedInputStream in = null;
        int respuesta;
        URL url = new URL(adrecaURL);
        URLConnection conexion = url.openConnection();

        if (!(conexion instanceof HttpURLConnection))
            throw new IOException("No conexión HTTP");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conexion;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            respuesta = httpConn.getResponseCode();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                in = new BufferedInputStream(httpConn.getInputStream());
            }
        } catch (Exception ex) {
            throw new IOException("Error conectando");
        }
        return in;
    }


}
