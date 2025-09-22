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
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentInputBinding
import co.com.japl.module.paid.controllers.Inputs.form.InputViewModel
import co.com.japl.module.paid.views.Inputs.form.Input
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputFragment : Fragment(){

    @Inject lateinit var service:IInputPort

    val viewModel: InputViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = InputViewModel::class.java,
            build = {
                InputViewModel(
                    savedStateHandle = it,
                    inputSvc = service
                )
            }
        )
    }
    private lateinit var _binding : FragmentInputBinding

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater,container,false)
        
        _binding.cvComponentFi.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Input(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return _binding.root
    }
}