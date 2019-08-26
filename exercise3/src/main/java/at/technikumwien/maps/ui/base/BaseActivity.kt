package at.technikumwien.maps.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import at.technikumwien.maps.AppDependencyManager
import at.technikumwien.maps.MyApplication
import at.technikumwien.maps.util.DependencyManagerViewModelFactory

abstract class BaseActivity : AppCompatActivity() {

    protected val appDependencyManager: AppDependencyManager
        get() = (applicationContext as MyApplication).appDependencyManager

    /** Convenience method to create the ViewModel via [ViewModelProviders] and
     * [DependencyManagerViewModelFactory]
     * reified means that the type is also available at runtime, which also means this extension
     * fun must be inlined. */
    protected inline fun <reified V : ViewModel> createViewModel(): V =
        ViewModelProviders.of(this, DependencyManagerViewModelFactory(appDependencyManager))
            .get(V::class.java)

}
