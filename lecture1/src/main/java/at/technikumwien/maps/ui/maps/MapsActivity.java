package at.technikumwien.maps.ui.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import at.technikumwien.maps.R;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.ui.base.BaseActivity;

public class MapsActivity extends BaseActivity<MapsView, MapsPresenter> implements MapsView, OnMapReadyCallback {

    private FrameLayout rootLayout;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rootLayout = findViewById(R.id.root_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @NonNull
    @Override // from mosby
    public MapsPresenter createPresenter() {
        return new MapsPresenter(getAppDependencyManager().getDrinkingFountainRepo());
    }

    @Override
    public void onDataLoaded(List<DrinkingFountain> drinkingFountainList) {
        Log.i("MapsActivity", "List loaded: " + drinkingFountainList.size() + " items");

        googleMap.clear();
        for(DrinkingFountain df: drinkingFountainList) {
            googleMap.addMarker(new MarkerOptions().position(df.position()).title(df.name()));
        }
    }

    @Override
    public void onDataLoadError(Exception exception) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadData();
            }
        };

        Snackbar.make(rootLayout, "Loading data failed", Snackbar.LENGTH_LONG)
                .setAction("Retry", clickListener)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // When we arrive here, the Google Map is loaded
        this.googleMap = googleMap;
        Log.i("MapsActivity", "Map ready");

        LatLng vienna = new LatLng(48.239340, 16.377335);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vienna, 10));

        // presenter from Mosby
        presenter.loadData();
    }
}
