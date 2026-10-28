package co.com.japl.module.creditcard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.module.creditcard.controllers.bought.lists.AllBoughtRecordsViewModel
import co.com.japl.module.creditcard.views.bought.AllBoughtRecordsList
import co.com.japl.ui.theme.MaterialThemeComposeUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllBoughtRecordsFragment : Fragment() {

    @Inject lateinit var boughtSvc: IBoughtListPort
    private lateinit var viewModel: AllBoughtRecordsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = AllBoughtRecordsViewModel(boughtSvc)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    AllBoughtRecordsList(viewModel = viewModel)
                }
            }
        }
    }
}
