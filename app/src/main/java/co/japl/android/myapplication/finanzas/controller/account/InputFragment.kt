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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.module.paid.controllers.Inputs.form.InputViewModel
import co.com.japl.module.paid.controllers.Inputs.form.InputViewModelFactory
import co.com.japl.module.paid.views.Inputs.form.InputForm
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentInputBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputFragment : Fragment() {

    @Inject
    lateinit var service: IInputPort

    lateinit var _binding: FragmentInputBinding

    private val viewModel: InputViewModel by viewModels(
        factoryProducer = {
            InputViewModelFactory(service)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        _binding.cvComponentFi.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    InputForm(viewModel = viewModel, navController = findNavController())
                }
            }
        }
        return _binding.root.rootView
    }
}