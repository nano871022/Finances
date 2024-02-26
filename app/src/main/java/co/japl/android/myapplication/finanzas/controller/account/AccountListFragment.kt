package co.japl.android.myapplication.finanzas.controller.account

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAccountListBinding
import co.japl.android.myapplication.finanzas.view.accounts.inputs.lists.InputListModelView
import co.japl.android.myapplication.finanzas.view.accounts.lists.AccountList
import co.japl.android.myapplication.finanzas.view.accounts.lists.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountListFragment : Fragment() {
    @Inject lateinit var service:IAccountPort
    @Inject lateinit var inputSvc:IInputPort

    private lateinit var _binding:FragmentAccountListBinding
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountListBinding.inflate(inflater, container, false)
        val viewModel = AccountViewModel(service,inputSvc, findNavController())
        binding.cvAccountList.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    AccountList(viewModel = viewModel)
                }
            }
        }
        return binding.root.rootView
    }




}