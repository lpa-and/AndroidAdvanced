package at.technikumwien.maps.ui.maps

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.lifecycle.*
import at.technikumwien.maps.AppDependencyManager
import at.technikumwien.maps.R
import at.technikumwien.maps.data.local.DrinkingFountainDao
import at.technikumwien.maps.data.model.DrinkingFountain
import at.technikumwien.maps.data.remote.DrinkingFountainApi
import at.technikumwien.maps.ui.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var rootLayout: FrameLayout
    private lateinit var viewModel: MapsViewModel

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        setContentView(R.layout.activity_maps)
        rootLayout = findViewById(R.id.root_layout)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.menu_refresh) {
            // When refresh is pressed, force sync the drinking fountains
            viewModel.syncDrinkingFountains()
            true
        } else super.onOptionsItemSelected(item)

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val viennaLatLng = LatLng(48.239340, 16.377335)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viennaLatLng, 10f))
        setupObservers()
        viewModel.syncDrinkingFountains()
    }

    /** In MVVM the view observes the [LiveData] that the [ViewModel] provides and acts on new data. */
    private fun setupObservers() {
        // When our ViewModel emits new DrinkingFountains, we want to show them
        viewModel.drinkingFountains.observe(this, Observer { drinkingFountains ->
            showDrinkingFountains(drinkingFountains)
        })

        // When our ViewModel emits an error, we cant to show it
        viewModel.errors.observe(this, Observer { error ->
            showError(error)
        })
    }

    private fun showDrinkingFountains(drinkingFountains: List<DrinkingFountain>) {
        Log.d("MapsActivity", "Showing new drinking fountain data")
        googleMap?.run {
            /** Clear everything on the map and add a new marker for each [DrinkingFountain] */
            clear()
            drinkingFountains.forEach { df ->
                addMarker(MarkerOptions().position(df.position).title(df.name))
            }
        }
    }

    // Show a Snackbar with the option to retry
    private fun showError(exception: Exception) {
        Log.e("MapsActivity", "Could not load drinking fountains", exception)
        Snackbar.make(rootLayout, R.string.snackbar_load_retry_message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.snackbar_load_retry_action) { viewModel.syncDrinkingFountains() }
            .show()
    }
}

class MapsViewModel(
    private val drinkingFountainApi: DrinkingFountainApi,
    private val drinkingFountainDao: DrinkingFountainDao
) : ViewModel() {

    constructor(dependencyManager: AppDependencyManager) : this(
        dependencyManager.drinkingFountainApi,
        dependencyManager.drinkingFountainDao
    )

    val drinkingFountains: LiveData<List<DrinkingFountain>> = drinkingFountainDao.loadAll()

    val errors: MutableLiveData<Exception> = MutableLiveData()

    fun syncDrinkingFountains() {
        /** viewModelScope can be used with Coroutines when we want them to be canceled
         * when our ViewModel is cleared, see [onCleared] */
        viewModelScope.launch {
            try {
                // Load drinking fountains from API and store them locally via the DAO
                val drinkingFountains = drinkingFountainApi.getDrinkingFountains().drinkingFountains
                drinkingFountainDao.refresh(drinkingFountains)
            } catch (exception: Exception) {
                errors.value = exception
            }
        }
    }

}