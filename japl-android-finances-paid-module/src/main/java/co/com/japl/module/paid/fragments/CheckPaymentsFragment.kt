package co.com.japl.module.paid.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.com.japl.finances.iports.inbounds.common.ICheckPaymentPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
//import co.com.japl.module.paid.views.checkpaids.CheckList
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class CheckPaymentsFragment : Fragment() {


    @Inject lateinit var svc:ICheckPaymentPort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val period = YearMonth.now()
        val viewModel = CheckListViewModel(period,svc)
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    //CheckList(viewModel = viewModel)
                }
            }
        }
    }


}
