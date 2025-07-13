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
import co.com.japl.finances.iports.inbounds.credit.IAdditionalFormPort
import co.com.japl.module.credit.controllers.forms.AdditionalFormViewModel
import co.com.japl.module.credit.views.forms.AdditionalForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAdditionalCreditBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalCreditFragment : Fragment(){
    @Inject lateinit var additionalSvc: IAdditionalFormPort

    val viewModel:AdditionalFormViewModel by viewModels{
        ViewModelFactory(
            owner=this,
            viewModelClass=AdditionalFormViewModel::class.java,
            build={
                val additional = arguments?.let {  AdditionalCreditParams.download(it) }
                val idCredit = additional?.first?:0
                val codeCredit = additional?.second?:0
                AdditionalFormViewModel(
                    context=context?.applicationContext!!,
                    savedStateHandle = it,
                    id=idCredit,
                    codeCredit = codeCredit,
                    additionalSvc=additionalSvc,
                    navController = findNavController() )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentAdditionalCreditBinding.inflate(inflater)
        root.composeViewFacf.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed )
            setContent {
                MaterialThemeComposeUI {
                    AdditionalForm(viewModel)
                }
            }
        }

        return root.root
    }
}