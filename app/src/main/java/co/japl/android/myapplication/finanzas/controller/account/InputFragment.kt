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
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentInputBinding
import co.japl.android.myapplication.finanzas.putParams.InputListParams
import co.japl.android.myapplication.finanzas.view.accounts.inputs.form.InputForm
import co.japl.android.myapplication.finanzas.view.accounts.inputs.form.InputViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputFragment : Fragment(){

    private var accountCode:Int = 0

    @Inject lateinit var service:IInputPort

    lateinit var _binding : FragmentInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater,container,false)
        accountCode = arguments?.let { InputListParams.download(it) }?:0
        val viewModel = InputViewModel(accountCode,null,service,findNavController())
        _binding.cvComponentFi.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    InputForm(viewModel = viewModel)
                }
            }
        }
        return _binding.root.rootView
    }
}