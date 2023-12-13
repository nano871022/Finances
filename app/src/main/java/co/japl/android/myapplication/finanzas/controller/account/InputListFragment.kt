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
import co.japl.android.myapplication.databinding.FragmentInputListBinding
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import co.japl.android.myapplication.finanzas.view.accounts.inputs.lists.InputList
import co.japl.android.myapplication.finanzas.view.accounts.inputs.lists.InputListModelView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputListFragment : Fragment(){
    private var accountCode:Int = 0

    @Inject lateinit var portSvc:IInputPort

    private var _binding: FragmentInputListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputListBinding.inflate(inflater,container,false)
        val root = binding.root
        accountCode = arguments?.let{AccountParams.download(it)}?:0
        binding.listViewComposableIl.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    InputList(modelView = InputListModelView(requireContext(),accountCode,findNavController() ,portSvc))
                }
            }
        }
        return root
    }
}