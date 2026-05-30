package co.com.japl.module.credit.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.com.japl.module.credit.controllers.list.ListViewModel
import co.com.japl.module.credit.views.CreditList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import javax.inject.Inject

@AndroidEntryPoint
class MonthlyCreditListFragment : Fragment() {

    @Inject
    lateinit var factory: ListViewModel.Factory

    private val viewModel: ListViewModel by viewModels {
        ViewModelFactory(
            owner = this,
            viewModelClass = ListViewModel::class.java,
            build = {
                factory.create(YearMonth.now(), findNavController())
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    CreditList(viewModel)
                }
            }
        }
    }
}
