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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IProjectionsPort
import co.com.japl.module.paid.controllers.projections.list.ProjectionsViewModel
import co.com.japl.module.paid.controllers.projections.list.ProjectionsViewModelFactory
import co.com.japl.module.paid.views.projections.list.Projections
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentProjectionsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectionsFragment : Fragment() {
    @Inject
    lateinit var svc: IProjectionsPort
    private val viewModel: ProjectionsViewModel by viewModels(
        factoryProducer = {
            ProjectionsViewModelFactory(svc)
        },
        extrasProducer = {
            val extras = MutableCreationExtras(defaultViewModelCreationExtras)
            extras.apply {
                arguments?.let {
                    set(ViewModelProvider.SavedStateHandleFactory.DEFAULT_ARGS_KEY, it)
                }
            }
            extras
        }
    )


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
