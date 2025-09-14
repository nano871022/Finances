package co.japl.android.myapplication.finanzas.controller.creditfix

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
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.module.credit.controllers.list.AdditionalViewModel
import co.com.japl.module.credit.views.lists.Additional
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAdditionalListBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalListFragment : Fragment() {
    @Inject lateinit var additionalSvc : IAdditional
    private val viewModel: AdditionalViewModel by viewModels {
        ViewModelFactory(this,AdditionalViewModel::class.java){
            AdditionalViewModel(requireContext(),it,additionalSvc)
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentAdditionalListBinding.inflate(inflater)
        root.composeViewFal.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Additional(viewModel,findNavController())
                }
            }
        }
        return root.root
    }


}