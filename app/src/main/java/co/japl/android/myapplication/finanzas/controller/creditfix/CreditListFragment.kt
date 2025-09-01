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
import co.com.japl.module.credit.controllers.recap.RecapViewModel
import co.com.japl.module.credit.views.recap.Recap
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentCreditListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class CreditListFragment : Fragment(){
    private lateinit var period:YearMonth
    @Inject lateinit var creditList:ICreditPort


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root =  FragmentCreditListBinding. inflate(inflater, container, false)
        period = YearMonth.now()
        val viewModel = RecapViewModel(creditList,period,findNavController())
        root.cvComponentCl.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI  {
                    Recap(viewModel)
                }
            }
        }
        return root.root
    }
}