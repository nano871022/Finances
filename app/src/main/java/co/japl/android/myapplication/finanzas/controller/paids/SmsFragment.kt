package co.japl.android.myapplication.finanzas.controller.paids

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
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.module.paid.controllers.sms.form.SmsViewModel
import co.com.japl.module.paid.views.sms.form.Sms
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSmsPaidBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.putParams.SmsPaidParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsFragment : Fragment() {
    @Inject lateinit var smsSvc: ISMSPaidPort
    @Inject lateinit var accountSvc: IAccountPort

    val viewModel: SmsViewModel by viewModels{
        ViewModelFactory(
            owner=this,
            viewModelClass=SmsViewModel::class.java,
            build = {
                SmsViewModel(
                    savedStateHandle=it,
                    svc = smsSvc,
                    accountSvc = accountSvc,
                )
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentSmsPaidBinding.inflate(inflater)
        root.cvComposeFsp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Sms(viewModel = viewModel,findNavController())
                }
            }
        }
        return root.root.rootView
    }
}