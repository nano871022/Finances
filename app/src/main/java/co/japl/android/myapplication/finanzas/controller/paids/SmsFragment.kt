package co.japl.android.myapplication.finanzas.controller.paids

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
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.module.paid.controllers.sms.form.SmsViewModel
import co.com.japl.module.paid.controllers.sms.form.SmsViewModelFactory
import co.com.japl.module.paid.views.sms.form.Sms
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentSmsPaidBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsFragment : Fragment() {
    @Inject
    lateinit var smsSvc: ISMSPaidPort
    @Inject
    lateinit var accountSvc: IAccountPort

    private val viewModel: SmsViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.apply {
                this.set(ViewModelProvider.SavedStateHandleFactory.DEFAULT_ARGS_KEY, arguments ?: bundleOf())
            }
        },
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
        val root = FragmentSmsPaidBinding.inflate(inflater)
        root.cvComposeFsp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Sms(viewModel = viewModel, findNavController())
                }
            }
        }
        return root.root
    }
}