package com.example.mymaps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mymaps.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    double latitud, longitud;
    SharedPreferences preferences;
    private Button btnLimpiarMarcas, btnGuardarMarca, btnMarcaFavorita;
    Marker currentMarker = null;
    LatLng miUbicacion;
    double valorLat, valorLon;
    LatLng lonlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnLimpiarMarcas = findViewById(R.id.limpiar);
        btnGuardarMarca = findViewById(R.id.favorito);
        btnMarcaFavorita = findViewById(R.id.ubicarfavorito);


        btnLimpiarMarcas.setOnClickListener(this);
        btnGuardarMarca.setOnClickListener(this);
        btnMarcaFavorita.setOnClickListener(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        latitud = Double.parseDouble(getIntent().getStringExtra("latitud"));
        longitud = Double.parseDouble(getIntent().getStringExtra("longitud"));
        // Añade una marca en mi posición actual
        miUbicacion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions()
                .position(miUbicacion)
                .title("Mi ubicacion")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

    }
    @Override
    public void onClick(View v) {

        if (v == btnLimpiarMarcas) {
            mMap.clear();
        }
        if (v == btnGuardarMarca){
            guardarPreferences(lonlat);
        }
        if (v == btnMarcaFavorita){
            Toast.makeText(MapsActivity.this, "Posición Favorita: " + " Latitud: "+ valorLat +" Longitud " + valorLon , Toast.LENGTH_SHORT).show();
            miUbicacion = new LatLng(valorLat, valorLon);
            mMap.addMarker(new MarkerOptions()
                    .position(miUbicacion)
                    .title("Posición Favorita")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.favoritos)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 16));
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(MapsActivity.this, "Click position" + latLng.latitude+latLng.longitude, Toast.LENGTH_SHORT).show();
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Mi ubicacion"));
        lonlat = latLng;
    }

    public void guardarPreferences(LatLng latLng){

        preferences = getSharedPreferences("MyPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putFloat("latitud", (float) latLng.latitude);
        editor.putFloat("longitud", (float) latLng.longitude);
        editor.commit();
        System.out.println(editor.commit());
        leerPreferences();

    }

    public void leerPreferences(){

        valorLat = preferences.getFloat("latitud", 0);
        valorLon = preferences.getFloat("longitud", 0);

        System.out.println("Latitud" + valorLat);
        System.out.println("Longitud" + valorLon);

    }

    public void eliminarMarca(){
        if (currentMarker!=null) {
            currentMarker.remove();
        }
        Toast.makeText(this, "Última marca eliminada.", Toast.LENGTH_LONG).show();
    }
}
