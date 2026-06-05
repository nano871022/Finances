package co.com.japl.module.paid.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IProjectionListPort
import co.com.japl.module.paid.controllers.projections.list.ProjectionListViewModel
import co.com.japl.module.paid.views.projections.list.ProjectionList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListProjectFragment : Fragment(){

    @Inject lateinit var svc: IProjectionListPort


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ProjectionListViewModel(context?.applicationContext!!,svc,findNavController())
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    ProjectionList(viewModel=viewModel)
                }
            }
        }
    }


}
