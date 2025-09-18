package co.japl.android.myapplication.finanzas.controller.projections

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import co.com.japl.module.paid.controllers.projections.forms.ProjectionFormViewModel
import co.com.japl.module.paid.controllers.projections.forms.ProjectionFormViewModelFactory
import co.com.japl.module.paid.views.projections.form.ProjectionForm
import co.japl.android.myapplication.databinding.FragmentProjectionBinding
import co.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectionFragment : Fragment() {
    @Inject
    lateinit var svc: IProjectionFormPort
    private val viewModel: ProjectionFormViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.apply {
                this.set(ViewModelProvider.SavedStateHandleFactory.DEFAULT_ARGS_KEY, arguments ?: bundleOf())
            }
        },
        factoryProducer = {
            ProjectionFormViewModelFactory(svc)
        }
    )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentProjectionBinding.inflate(inflater)
        root.composableviewFpj.apply {
            setViewCompositionStrategy(strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    ProjectionForm(viewModel, findNavController())
                }
            }
        }
        return root.root
    }
}
