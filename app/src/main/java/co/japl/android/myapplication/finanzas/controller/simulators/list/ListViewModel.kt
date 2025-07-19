package co.japl.android.myapplication.finanzas.controller.simulators.list

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor():ViewModel() {
    lateinit var navController: NavController
}
