package co.com.japl.module.credit.views.creditamortization

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import co.com.japl.module.credit.controllers.creditamortization.CreditAmortizationViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class CreditAmortizationActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val creditCode = intent.getIntExtra("creditCode", 0)
        val lastDate = intent.getStringExtra("lastDate")?.let{ DateUtils.toLocalDate(it)} ?: LocalDate.now()
        setContent {
            MaterialThemeComposeUI {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: CreditAmortizationViewModel = hiltViewModel()
                    viewModel.loadData(creditCode,lastDate)
                    CreditAmortizationScreen(viewModel,
                        goToExtraValues = {},
                        goToAdditional = {})
                }
            }
        }
    }
}
