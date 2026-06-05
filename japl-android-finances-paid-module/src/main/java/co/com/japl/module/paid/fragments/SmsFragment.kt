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
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.module.paid.controllers.sms.form.SmsViewModel
import co.com.japl.module.paid.views.sms.form.Sms
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.params.SmsPaidParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsFragment : Fragment() {
    @Inject lateinit var smsSvc: ISMSPaidPort
    @Inject lateinit var accountSvc: IAccountPort
    @Inject lateinit var prefs: Prefs

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val code = arguments?.let{SmsPaidParams.download(it)}
        val viewModel = SmsViewModel(
            codeSMS = code,
            svc = smsSvc,
            accountSvc = accountSvc,
            navController = findNavController(),
            prefs = prefs
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    Sms(viewModel = viewModel)
                }
            }
        }
    }
}
