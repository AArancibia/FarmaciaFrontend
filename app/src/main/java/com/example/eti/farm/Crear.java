package com.example.eti.farm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Crear extends AppCompatActivity {

    Spinner spProveedor, spCategoria;
    EditText txtNombre, txtPrecio, txtStock, txtDescripcion;
    Button btnAniadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        spProveedor = (Spinner) findViewById(R.id.spProveedor);
        spCategoria = (Spinner) findViewById(R.id.spCategoria);

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);
        txtStock = (EditText) findViewById(R.id.txtStock);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);

        btnAniadir = (Button) findViewById(R.id.btnAniadir);


        //spProveedor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolNames));

        Thread tr = new Thread() {
            @Override
            public void run() {
                final String proveedores = leer("proveedores");
                final String categorias = leer("categorias");
                Log.d("1", proveedores);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("2", proveedores);
                        cargaProveedores(obtDatosJSON(proveedores, "proveedores"));
                        cargaCategorias(obtDatosJSON(categorias, "categorias"));
                    }
                });
            }
        };
        tr.start();




        btnAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = spProveedor.getSelectedItem().toString();
                Log.d("dsfds", text);

                Thread nt = new Thread() {
                    @Override
                    public void run() {
                        try {
                            final String res;
                            res = enviarPOST(txtNombre.getText().toString(), Double.parseDouble(txtPrecio.getText().toString()),
                                    Double.parseDouble(txtStock.getText().toString()), txtDescripcion.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }catch (Exception e) {

                        }
                    }
                };
                nt.start();
            }
        });

    }

    public ArrayList<String> obtDatosJSON( String response, String proveedores ) {

        ArrayList<String> listado = new ArrayList<String>();
        Log.d("res", response);
        try {
            JSONObject json = new JSONObject(response);
            Log.d("3", json.toString());
            String texto = "";
            if ( proveedores.equals("proveedores")) {
                for (int i = 0; i < json.length(); i++) {
                    Log.d("json", String.valueOf(i));
                    texto = json.getJSONArray("proveedores").getJSONObject(i).getString("nombreEmpresa");
                    //json.getJSONArray("productos").getJSONObject(i).getJSONObject("proveedor").getString("nombreEmpresa") +
                    //texto = json.getString("usuarios");
                    listado.add(texto);
                }
            }else {
                for (int i = 0; i < json.length(); i++) {
                    Log.d("json", String.valueOf(i));
                    texto = json.getJSONArray("categorias").getJSONObject(i).getString("descripcion");
                    //json.getJSONArray("productos").getJSONObject(i).getJSONObject("proveedor").getString("nombreEmpresa") +
                    //texto = json.getString("usuarios");
                    listado.add(texto);
                }
            }


        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return listado;
    }

    public String leer( String url ) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpGet = new HttpGet();
        if ( url.equals("proveedores") ) {
            httpGet = new HttpGet("http://192.168.10.154:3000/proveedores");
        }else {
            httpGet = new HttpGet("http://192.168.10.154:3000/categorias");
        }

        String resultado = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            resultado = EntityUtils.toString(entity,"UTF-8");
        } catch (Exception e) {

        }
        return resultado;
    }

    public void cargaProveedores( ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datos);
        Spinner listado = ( Spinner ) findViewById(R.id.spProveedor);
        listado.setAdapter(adapter);
    }

    public void cargaCategorias( ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, datos);
        Spinner listado = ( Spinner ) findViewById(R.id.spCategoria);
        listado.setAdapter(adapter);
    }

    public String enviarPOST(String nombre, Double precio, Double stock, String descripcion ) {
        HttpClient httpClient = new DefaultHttpClient();
        //HttpContext localcontext = new BasicHttpContext();
        HttpResponse response = null;

        HttpPost httpPost = new HttpPost("http://192.168.10.154:3000/productos");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<>(5);
            nameValuePairs.add(new BasicNameValuePair("nombre", "Alexis Joel"));
            nameValuePairs.add(new BasicNameValuePair("email", "aarancibia@gmail.com"));
            nameValuePairs.add(new BasicNameValuePair("password", "1234"));
            nameValuePairs.add(new BasicNameValuePair("img", "tt"));
            nameValuePairs.add(new BasicNameValuePair("role", "USER_ROLE"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = httpClient.execute(httpPost);
        }catch (Exception ex) {

        }
        return response.toString();
    }
}
