package co.com.japl.module.creditcard.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.creditcard.params.CreditCardQuotesParams
import co.com.japl.module.creditcard.views.bought.BoughtMonthly
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import co.com.japl.ui.Prefs
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.module.creditcard.controllers.bought.lists.ListBoughtViewModel
import co.com.japl.module.creditcard.views.bought.BoughList
import co.com.japl.module.creditcard.views.bought.BoughtListMain
import co.com.japl.module.creditcard.views.bought.SettingsViewModel

@AndroidEntryPoint
class ListBoughtFragment : Fragment() {

    @Inject lateinit var taxSvc: ITaxPort
    @Inject lateinit var boughtListSvc: IBoughtListPort
    @Inject lateinit var creditCardSvc: ICreditCardPort
    @Inject lateinit var differInstallmentSvc: IDifferQuotesPort
    @Inject lateinit var prefs: Prefs
    @Inject lateinit var emailCCSvc: IEmailCreditCardPort
    @Inject lateinit var simulatorSvc: ISimulatorCreditVariablePort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ListBoughtViewModel(
            findNavController(),
            Prefs(requireContext().applicationContext),
            taxSvc,
            boughtListSvc,
            creditCardSvc,
            differInstallmentSvc,
            simulatorSvc = simulatorSvc
        )

        val settingVM = SettingsViewModel(prefs,emailCCSvc)

        arguments?.let {
            val params = CreditCardQuotesParams.Companion.Historical.download(it)
            viewModel.setParams(params)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    BoughtListMain(viewModel, settingVM = settingVM)
                }
            }
        }
    }
}
