package com.example.ingarukal11.cibertecsemana09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText txtID, txtApellido, txtNombre, txtEdad, txtSexo;
    Button btnConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtID = (EditText)findViewById(R.id.txtID);
        btnConsultar = (Button)findViewById(R.id.btnConsultar);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread tr = new Thread(){
                    @Override
                    public void run(){
                        final String res = consumirJSON(txtID.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mostrarDatos(res);
                            }
                        });
                    }
                };

                tr.start();

            }
        });
    }

    public String consumirJSON(String id){

        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;

        try{

            //url = new URL("http://192.168.1.20:3000/api/v1/users?id=" + id); NodeJS
            url = new URL("http://192.168.1.20:80/serviciosAndroid/consultaCliente.php?id=" + id);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            respuesta = connection.getResponseCode();

            resul = new StringBuilder();

            if( respuesta == HttpURLConnection.HTTP_OK ){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ( (linea = reader.readLine()) != null ){
                    resul.append(linea);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return resul.toString();

    }

    public void mostrarDatos(String rpta){

      txtApellido = (EditText)findViewById(R.id.txtApellido);
      txtNombre = (EditText)findViewById(R.id.txtNombre);
      txtEdad = (EditText)findViewById(R.id.txtEdad);
      txtSexo = (EditText)findViewById(R.id.txtSexo);

        try {

            JSONArray json = new JSONArray(rpta);

            JSONObject cliente = json.getJSONObject(0);

            String apellido = cliente.getString("Apellidos");
            String nombre = cliente.getString("Nombres");
            String edad = cliente.getString("Edad");
            String sexo = cliente.getString("Sexo");

            txtApellido.setText(apellido);
            txtNombre.setText(nombre);
            txtEdad.setText(edad);
            txtSexo.setText(sexo);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
