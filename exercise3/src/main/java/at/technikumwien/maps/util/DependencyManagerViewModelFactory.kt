package at.technikumwien.maps.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.technikumwien.maps.AppDependencyManager

/**
 * This ViewModel Factory can be used to create ViewModels which have a constructor
 * with exactly one parameter [AppDependencyManager]
 */
class DependencyManagerViewModelFactory(
    private val dependencyManager: AppDependencyManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        try {
            modelClass.getConstructor(AppDependencyManager::class.java)
                .newInstance(dependencyManager)
        } catch (exception: Exception) {
            throw RuntimeException("Cannot create an instance of $modelClass", exception)
        }

}