package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.japl.android.credit.view.extavalue.ExtraValueListScreen
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.putParams.ExtraValueListParam
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExtraValueListController : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val params = arguments?.let { ExtraValueListParam.download(it) }
        val creditId = params?.first ?: 0
        val kindOf = params?.second

        return ComposeView(requireContext()).apply {
            id = R.id.compose_view
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                kindOf?.let {
                    ExtraValueListScreen(creditId = creditId, kindOf = kindOf)
                }
            }
        }
    }
}