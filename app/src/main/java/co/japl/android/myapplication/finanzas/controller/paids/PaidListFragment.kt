package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import co.com.japl.module.paid.controllers.paid.list.PaidViewModel
import co.com.japl.module.paid.controllers.paid.list.PaidViewModelFactory
import co.com.japl.module.paid.views.paid.list.Paid
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentPaidListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaidListFragment : Fragment() {

    @Inject
    lateinit var paidSvc: IPaidPort
    @Inject
    lateinit var prefs: Prefs

    private val viewModel: PaidViewModel by viewModels(
        factoryProducer = {
            PaidViewModelFactory(paidSvc, prefs)
        },
        extrasProducer = {
            val extras = MutableCreationExtras(defaultViewModelCreationExtras)
            extras.apply {
                arguments?.let {
                    set(ViewModelProvider.SavedStateHandleFactory.DEFAULT_ARGS_KEY, it)
                }
            }
            extras
        }
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FragmentPaidListBinding.inflate(inflater)

        root.cvPaidFpl.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Paid(viewModel = viewModel, navController = findNavController())
                }
            }
        }

        return root.root.rootView
    }

}