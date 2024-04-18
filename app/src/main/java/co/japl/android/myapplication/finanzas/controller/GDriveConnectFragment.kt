package co.japl.android.myapplication.finanzas.controller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.databinding.FragmentGDriveConnectBinding
import co.japl.android.myapplication.finanzas.bussiness.config.GoogleDriveConfig
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveService
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleLoginOldService
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleLoginService
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInSimpleImplement
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInWebImplement
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInnImplement
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleDriveService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import co.japl.android.myapplication.finanzas.view.google.GoogleAuthBackupRestore
import co.japl.android.myapplication.finanzas.view.google.GoogleAuthBackupRestoreViewModel
import com.google.android.gms.common.SignInButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import javax.inject.Inject

@AndroidEntryPoint
class GDriveConnectFragment : Fragment() {
    private lateinit var service :IGoogleSignInService
    private lateinit var driveSvc:IGoogleDriveService
    private lateinit var loginSimpleSvc :IGoogleSignInService
    private lateinit var loginWebSvc :IGoogleSignInService

    @Inject lateinit var dbConnect: SQLiteOpenHelper

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = FragmentGDriveConnectBinding.inflate(inflater)
        service = GoogleSignInnImplement(requireActivity())
        driveSvc = GoogleDriveImpl(requireActivity(),dbConnect as ConnectDB)
        loginSimpleSvc = GoogleSignInSimpleImplement(requireActivity())
        loginWebSvc = GoogleSignInWebImplement(requireActivity())
        root.cwComposeFddc.apply {
            val viewModel = GoogleAuthBackupRestoreViewModel(requireActivity(),service,loginSimpleSvc,loginWebSvc,driveSvc)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialThemeComposeUI {
                    GoogleAuthBackupRestore(viewModel)
                }
            }
        }
        return root.root.rootView
    }
}