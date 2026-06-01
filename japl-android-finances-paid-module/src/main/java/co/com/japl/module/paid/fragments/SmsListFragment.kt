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
import co.com.japl.module.paid.controllers.sms.list.SmsViewModel
import co.com.japl.module.paid.views.sms.list.SMS
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsListFragment : Fragment() {
    @Inject lateinit var smsSvc: ISMSPaidPort
    @Inject lateinit var accountSvc: IAccountPort
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = SmsViewModel(
            svc = smsSvc,
            accountSvc = accountSvc,
            navController = findNavController()
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    SMS(viewModel = viewModel)
                }
            }
        }
    }
}
