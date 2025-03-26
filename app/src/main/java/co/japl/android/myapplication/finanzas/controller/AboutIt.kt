package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ViewCompositionStrategy
import co.com.japl.homeconnect.about.ui.About
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.BuildConfig
import co.japl.android.myapplication.R
import co.japl.android.myapplication.databinding.FragmentAboutItBinding

class AboutIt : Fragment() {

    private var _bind : FragmentAboutItBinding ? = null
    private val bind get() = _bind!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentAboutItBinding.inflate(inflater,container,false)
        val root = bind.root
        bind.firstCardCompose.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    About(versionDetail = BuildConfig.VERSION_NAME, applicationId = BuildConfig.APPLICATION_ID)
                }
            }
        }
        return root
    }
}