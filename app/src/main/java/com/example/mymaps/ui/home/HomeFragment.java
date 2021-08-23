package com.example.mymaps.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymaps.MapsActivity;
import com.example.mymaps.R;
import com.example.mymaps.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnUbicacion, btnMapa;
    private EditText editLatitud, editLongitud, editAltitud;
    private LocationManager locManager;
    private Location loc;
    private double lat, lon, alt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Buscamos en el layout los componentes
        btnUbicacion = root.findViewById(R.id.btn_localizar);
        btnMapa = root.findViewById(R.id.btn_map);
        editLatitud = root.findViewById(R.id.editTextLatitud);
        editLongitud = root.findViewById(R.id.editTextLongitud);
        editAltitud = root.findViewById(R.id.editTextAltitud);
        btnUbicacion.setOnClickListener(this);
        btnMapa.setOnClickListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Método onClick para darle la acción al botón Buscar Localizar
    @Override
    public void onClick(View v) {
        if (v == btnUbicacion) {
            miPosition();
            Toast.makeText(getContext(), "Click en el botón localizar", Toast.LENGTH_LONG).show();
        }
        if (v == btnMapa) {
            comprobar();
        }
    }


    //Método para hallar la posición
    public void miPosition() {

        if (ContextCompat.checkSelfPermission((Activity) getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        //Toast.makeText(getContext(), "Gps Deshabilitado", Toast.LENGTH_LONG).show();

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        lat = loc.getLatitude();
        lon = loc.getLongitude();
        alt = loc.getAltitude();

        editLatitud.setText(lat+"");
        editLongitud.setText(lon+"");
        editAltitud.setText(alt+"");

    }


    public void comprobar(){
        if (!editLongitud.getText().toString().equals("") && !editLatitud.getText().toString().equals("")){
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("latitud", lat + "");
            intent.putExtra("longitud", lon + "");
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"Los campo no estan llenos", Toast.LENGTH_LONG).show();
        }
    }

}
