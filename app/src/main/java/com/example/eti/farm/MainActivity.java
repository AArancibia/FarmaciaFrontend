package com.example.eti.farm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sin accion por el momento", Snackbar.LENGTH_LONG)
                        .setAction("Go", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Thread tr = new Thread() {
            @Override
            public void run() {
                final String resultado = leer();
                Log.d("1", resultado);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("2", resultado);
                        cargaListado(obtDatosJSON(resultado));
                    }
                });
            }
        };
        tr.start();


    }

    public ArrayList<String> obtDatosJSON( String response ) {

        ArrayList<String> listado = new ArrayList<String>();
        Log.d("res", response);
        try {
            JSONObject json = new JSONObject(response);
            Log.d("3", json.toString());
            String texto = "";
            for (int i = 0; i < json.length(); i++) {
                Log.d("json", String.valueOf(i));
                texto = json.getJSONArray("productos").getJSONObject(i).getString("nombre") + " | " +
                        json.getJSONArray("productos").getJSONObject(i).getString("precio");
                //json.getJSONArray("productos").getJSONObject(i).getJSONObject("proveedor").getString("nombreEmpresa") +
                //texto = json.getString("usuarios");
                listado.add(texto);
            }
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return listado;
    }

    public String leer() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("http://192.168.10.154:3000/productos");
        String resultado = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            resultado = EntityUtils.toString(entity,"UTF-8");
        } catch (Exception e) {

        }
        return resultado;
    }

    public void cargaListado( ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        ListView listado = ( ListView ) findViewById(R.id.lstConsulta);
        listado.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(getApplicationContext(), Crear.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
