package com.example.eti.farm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsu, txtPas;
    Button btnIngresar;
    String usu = "" , pas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsu = (EditText) findViewById(R.id.txtUsu);
        txtPas = (EditText) findViewById(R.id.txtPas);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usu = txtUsu.getText().toString();
                pas = txtPas.getText().toString();
                Toast.makeText(getApplicationContext(), usu, Toast.LENGTH_LONG).show();
                Thread tr = new Thread() {
                    @Override
                    public void run() {

                        final String resultado = leer(usu, pas);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String bandera = resultado.substring(6,10).trim();
                                if ( bandera.equals("true") ) {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });

    }

    public int obtDatosJson( String response ) {
        int res = 0;
        try {
            JSONArray json = new JSONArray(response);
            if ( json.length() > 0 ) {
                res = 1;
            }
        } catch ( Exception e ){

        }
        return  res;
    }

    public String leer ( String usu, String pas ) {
        HttpClient httpClient = new DefaultHttpClient();
        //HttpContext localcontext = new BasicHttpContext();
        HttpResponse response = null;
        String resultado = null;

        HttpPost httpPost = new HttpPost("http://192.168.10.154:3000/login");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("email", usu));
            nameValuePairs.add(new BasicNameValuePair("password", pas));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            resultado = EntityUtils.toString(entity, "UTF-8");
        }catch (Exception ex) {
            System.out.println(ex);
        }
        return resultado;
    }

}
