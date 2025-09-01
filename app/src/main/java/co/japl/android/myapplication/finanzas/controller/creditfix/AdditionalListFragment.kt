package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.credit.IAdditional
import co.com.japl.module.credit.controllers.list.AdditionalViewModel
import co.com.japl.module.credit.views.lists.Additional
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAdditionalListBinding
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalListFragment : Fragment() {
    @Inject lateinit var additionalSvc : IAdditional


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentAdditionalListBinding.inflate(inflater)
        val code = arguments?.let {
            CreditFixParams.downloadAdditionalList(it)
        } ?: 0
        val viewModel = AdditionalViewModel(context = context?.applicationContext!!,code.toInt(),additionalSvc,findNavController())
        root.composeViewFal.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Additional(viewModel)
                }
            }
        }
        return root.root
    }


}