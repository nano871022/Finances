package co.japl.android.myapplication.finanzas.controller.projections

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.paid.controllers.projections.list.ProjectionsViewModel
import co.com.japl.module.paid.views.projections.list.Projections
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentProjectionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectionsFragment : Fragment(){

    val viewModel : ProjectionsViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = ProjectionsViewModel::class.java,
            build = {
                ProjectionsViewModel(
                    projectionSvc = svc,
                    savedStateHandler = it,
                    navController = findNavController()
                )
            })
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentProjectionsBinding.inflate(inflater)
        root.componentviewPjt.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Projections(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return root.root
    }
}
