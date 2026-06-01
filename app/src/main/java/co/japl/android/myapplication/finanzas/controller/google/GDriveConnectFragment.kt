package co.japl.android.myapplication.finanzas.controller.google

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.finances.services.implement.GoogleLoginService
import co.japl.android.finances.services.implement.GoogleDriveServiceImpl
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.inbounds.common.IGoogleSignInService
import co.japl.android.myapplication.finanzas.controller.google.views.GoogleAuthBackupRestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GDriveConnectFragment : Fragment() {
    private var service :IGoogleSignInService? = null
    private var driveSvc:IGoogleDriveService? = null

    @Inject lateinit var dbConnect: SQLiteOpenHelper

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        service = GoogleLoginService(requireActivity(), 101)
        driveSvc = GoogleDriveServiceImpl()
        
        val viewModel = GoogleAuthBackupRestoreViewModel(requireActivity(), service, driveSvc)
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialThemeComposeUI {
                    GoogleAuthBackupRestore(viewModel)
                }
            }
        }
    }
}
