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
import co.com.japl.module.paid.controllers.sms.list.SmsViewModel
import co.com.japl.module.paid.controllers.sms.list.SmsViewModelFactory
import co.com.japl.module.paid.views.sms.list.SMS
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSmsPaidListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsListFragment : Fragment() {
    @Inject
    lateinit var smsSvc: ISMSPaidPort
    @Inject
    lateinit var accountSvc: IAccountPort

    private val viewModel: SmsViewModel by viewModels(
        factoryProducer = {
            SmsViewModelFactory(smsSvc, accountSvc)
        }
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentSmsPaidListBinding.inflate(inflater)
        root.cvComposeFspl.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    SMS(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return root.root
    }
}