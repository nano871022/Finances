package co.com.japl.module.paid.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.module.paid.params.AccountParams
import co.com.japl.module.paid.views.Inputs.list.InputList
import co.com.japl.module.paid.controllers.Inputs.list.InputListModelView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputListFragment : Fragment(){
    private var accountCode:Int = 0

    @Inject lateinit var portSvc:IInputPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountCode = arguments?.let{AccountParams.download(it)}?:0
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    InputList(modelView = InputListModelView(requireContext(),accountCode,findNavController() ,portSvc))
                }
            }
        }
    }
}
