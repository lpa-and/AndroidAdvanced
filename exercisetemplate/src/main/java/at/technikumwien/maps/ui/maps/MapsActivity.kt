package at.technikumwien.maps.ui.maps

import android.os.Bundle
import android.widget.FrameLayout
import at.technikumwien.maps.R
import at.technikumwien.maps.ui.base.BaseActivity

class MapsActivity : BaseActivity() {

    private lateinit var rootLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        rootLayout = findViewById(R.id.root_layout)
    }

}