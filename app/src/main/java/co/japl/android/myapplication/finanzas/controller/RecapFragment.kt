package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.databinding.FragmentRecapBinding
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.view.recap.Recap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecapFragment @Inject constructor() : Fragment() {
    @Inject lateinit var recapSvc:IRecapPort

    private var _binding: FragmentRecapBinding? = null
    private val recapViewModel by lazy {RecapViewModel(recapSvc,ApplicationInitial.prefs)}

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecapBinding.inflate(inflater, container, false)
        val root = _binding?.root
        _binding?.firstCardCompose?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    Recap(recapViewModel)
                }
            }
        }
        return root
    }

}