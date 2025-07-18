package co.japl.android.myapplication.finanzas.controller.simulators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import co.japl.android.myapplication.finanzas.controller.simulators.list.ListSimulator
import co.japl.android.myapplication.finanzas.controller.simulators.list.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import javax.inject.Inject

@AndroidEntryPoint
class ListSave : Fragment() {
    private val viewModel: ListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                viewModel.navController = findNavController()
                ListSimulator(viewModel)
            }
        }
    }
}