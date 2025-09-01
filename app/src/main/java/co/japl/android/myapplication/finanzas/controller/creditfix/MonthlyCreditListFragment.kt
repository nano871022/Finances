package co.japl.android.myapplication.finanzas.controller.creditfix

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import co.com.japl.finances.iports.inbounds.credit.IPeriodGracePort
import co.com.japl.module.credit.controllers.list.ListViewModel
import co.com.japl.module.credit.views.CreditList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentMonthlyCreditListBinding
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class MonthlyCreditListFragment : Fragment(){
    private lateinit var credit:CreditDTO

    @Inject lateinit var creditPort:ICreditPort
    @Inject lateinit var periodGracePort:IPeriodGracePort

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FragmentMonthlyCreditListBinding.inflate(inflater,container,false)
        val viewModel = ListViewModel(YearMonth.now(),creditPort,periodGracePort,findNavController())
        root.cvMcl.apply{
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    CreditList(viewModel)
                }

            }
        }
        return root.root
    }
}