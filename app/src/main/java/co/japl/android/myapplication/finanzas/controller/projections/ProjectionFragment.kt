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
import co.com.japl.module.paid.controllers.projections.forms.ProjectionFormViewModel
import co.com.japl.module.paid.views.projections.form.ProjectionForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentProjectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectionFragment : Fragment() {

    val viewModel : ProjectionFormViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass = ProjectionFormViewModel::class.java,
            build = {
                ProjectionFormViewModel(
                    saveStateHandler = it,
                    context = context?.applicationContext!!,
                    projectionSvc = svc,
                    navController = findNavController()
                )
            })
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentProjectionBinding.inflate(inflater)
        root.composableviewFpj.apply {
            setViewCompositionStrategy(strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed )
            setContent {
                MaterialThemeComposeUI {
                    ProjectionForm(viewModel,findNavController())
                }
            }
        }
        return root.root
    }
}
