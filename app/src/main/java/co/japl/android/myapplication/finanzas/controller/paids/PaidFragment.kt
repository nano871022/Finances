package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.controllers.paid.form.PaidViewModel
import co.com.japl.module.paid.controllers.paid.form.PaidViewModelFactory
import co.com.japl.module.paid.views.paid.form.Paid
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPaidBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaidFragment : Fragment() {

    @Inject lateinit var paidSvc:IPaidPort
    @Inject lateinit var accountSvc:IAccountPort

    private val viewModel: PaidViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.apply {
                this.set(ViewModelProvider.SavedStateHandleFactory.DEFAULT_ARGS_KEY, arguments ?: bundleOf())
            }
        },
        factoryProducer = {
            PaidViewModelFactory(accountSvc, paidSvc)
        }
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentPaidBinding.inflate( inflater)
        root.cwComposeFp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Paid(viewModel, findNavController())
                }
            }
        }
        return root.root
    }

}