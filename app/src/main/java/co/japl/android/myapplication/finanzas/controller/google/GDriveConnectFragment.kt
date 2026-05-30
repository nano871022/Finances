package co.japl.android.myapplication.finanzas.controller.google

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.ConnectDB
import co.com.japl.module.credit.databinding.FragmentGDriveConnectBinding
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInSimpleImplement
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInWebImplement
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInnImplement
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleDriveService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import co.japl.android.myapplication.finanzas.controller.google.views.GoogleAuthBackupRestore
import co.japl.android.myapplication.finanzas.controller.google.views.GoogleAuthBackupRestoreViewModel
import dagger.hilt.android.AndroidEntryPoint
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