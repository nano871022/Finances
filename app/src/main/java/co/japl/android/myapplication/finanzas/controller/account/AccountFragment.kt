package co.japl.android.myapplication.finanzas.controller.account

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAccountBinding
import co.com.japl.module.paid.controllers.accounts.form.AccountViewModel
import co.com.japl.module.paid.views.accounts.form.Account
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    @Inject lateinit var accountSvc:IAccountPort

    private val viewModel: AccountViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = AccountViewModel::class.java,
            build = {
                AccountViewModel(
                    savedStateHandle = it,
                    accountSvc = accountSvc
                )
            }
        )
    }

    private lateinit var _binding : FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater)
        _binding.cvComponentFa.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        _binding.cvComponentFa.apply {
            setContent {
                MaterialThemeComposeUI {
                    Account(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return _binding.root
    }

}