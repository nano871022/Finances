package co.japl.android.myapplication.finanzas.controller

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

class ViewModelFactory  constructor (
    owner: SavedStateRegistryOwner,
    private val viewModelClass:Class<*>,
    private val build: (SavedStateHandle)-> ViewModel
): AbstractSavedStateViewModelFactory(owner,null){

    override fun <T : ViewModel> create(key:String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if(modelClass.isAssignableFrom(viewModelClass)){
            return  build.invoke(handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}