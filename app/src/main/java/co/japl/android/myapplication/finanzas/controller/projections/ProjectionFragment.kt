package co.japl.android.myapplication.finanzas.controller.projections

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import co.com.japl.module.paid.controllers.projections.forms.ProjectionFormViewModel
import co.com.japl.module.paid.views.projections.form.ProjectionForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentProjectionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectionFragment : Fragment() {

    @Inject lateinit var svc: IProjectionFormPort

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentProjectionBinding.inflate(inflater)
        val id:Int? = null
        val viewModel = ProjectionFormViewModel(
            context = context?.applicationContext!!,
            id = id,
            projectionSvc = svc,
            navController = findNavController()
        )
        root.composableviewFpj.apply {
            setViewCompositionStrategy(strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed )
            setContent {
                MaterialThemeComposeUI {
                    ProjectionForm(viewModel)
                }
            }
        }
        return root.root
    }
}