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
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.module.paid.controllers.projections.list.ProjectionListViewModel
import co.com.japl.module.paid.views.projections.list.ProjectionList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentListProjectionBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListProjectFragment : Fragment(){

    @Inject lateinit var svc: IProjectionListPort

    val viewModel: ProjectionListViewModel by viewModels {
        ViewModelFactory(
            owner=this,
            viewModelClass=ProjectionListViewModel::class.java,
            build = {
                ProjectionListViewModel(context?.applicationContext!!,svc)
            }
        )
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentListProjectionBinding.inflate(inflater)
        root.composeviewFlp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    ProjectionList(viewModel=viewModel,findNavController())
                }
            }
        }
        return root.root
    }


}