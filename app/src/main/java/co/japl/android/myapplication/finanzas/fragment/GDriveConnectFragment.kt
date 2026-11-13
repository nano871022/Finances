package co.japl.android.myapplication.finanzas.fragment

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.inbounds.common.IGoogleSignInService
import co.com.japl.ui.Prefs
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.controller.google.GoogleAuthBackupRestoreViewModel
import co.japl.android.myapplication.finanzas.view.google.GoogleAuthBackupRestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GDriveConnectFragment : Fragment() {
    @Inject lateinit var loginSvc: IGoogleSignInService
    @Inject lateinit var driveSvc: IGoogleDriveService

    @Inject
    lateinit var dbConnect: SQLiteOpenHelper

    @Suppress("NewApi")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = GoogleAuthBackupRestoreViewModel(
            requireActivity(),
            loginSvc,
            driveSvc,
            Prefs(requireContext().applicationContext)
        )
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    GoogleAuthBackupRestore(viewModel)
                }
            }
        }
    }
}