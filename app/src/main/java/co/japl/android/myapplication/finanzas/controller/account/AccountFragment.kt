package co.japl.android.myapplication.finanzas.controller.account

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentAccountBinding
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import co.japl.android.myapplication.finanzas.view.accounts.form.AccountForm
import co.japl.android.myapplication.finanzas.view.accounts.form.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    @Inject lateinit var accountSvc:IAccountPort
    private var idAccount = 0
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
        arguments?.let {
            idAccount = AccountParams.download(it)
        }

        val viewModel = AccountViewModel(idAccount,accountSvc,findNavController())
        _binding.cvComponentFa.apply {
            setContent {
                MaterialThemeComposeUI {
                    AccountForm(viewModel = viewModel)
                }
            }
        }
        return _binding.root.rootView
    }

}