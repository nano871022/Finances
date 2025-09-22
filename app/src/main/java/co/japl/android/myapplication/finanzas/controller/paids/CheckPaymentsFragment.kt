package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentCheckPaymentsBinding
import co.japl.android.myapplication.finanzas.controller.ViewModelFactory
import co.japl.android.myapplication.finanzas.view.checkpaids.CheckList
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class CheckPaymentsFragment : Fragment() {


    @Inject lateinit var svc:ICheckPaymentPort

    val viewModel:CheckListViewModel by viewModels{
        ViewModelFactory(
            owner = this,
            viewModelClass=CheckListViewModel::class.java,
            build={
                CheckListViewModel(it,svc)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentCheckPaymentsBinding.inflate(inflater)
        root.cvComponentFcp.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CheckList(viewModel = viewModel)
                }
            }
        }
        return root.root.rootView
    }


}