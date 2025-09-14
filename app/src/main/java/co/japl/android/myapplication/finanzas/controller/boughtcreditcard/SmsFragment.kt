package co.japl.android.myapplication.finanzas.controller.boughtcreditcard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import androidx.fragment.app.viewModels
import co.com.japl.module.creditcard.controllers.smscreditcard.form.SmsCreditCardViewModel
import co.com.japl.module.creditcard.views.sms.forms.Sms
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSmsCreditCardBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.putParams.SmsCreditCardParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsFragment: Fragment()  {
    @Inject lateinit var creditCardSvc:ICreditCardPort
    @Inject lateinit var smsCreditCardSvc:ISMSCreditCardPort
    private val viewModel:SmsCreditCardViewModel by viewModels {
        ViewModelFactory(this,SmsCreditCardViewModel::class.java){
            SmsCreditCardViewModel(smsCreditCardSvc,creditCardSvc,it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentSmsCreditCardBinding.inflate(inflater,container,false)
        view.cvComposeLscc?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Sms(viewModel,findNavController())
                }
            }

        }
        return view.root.rootView
    }
}