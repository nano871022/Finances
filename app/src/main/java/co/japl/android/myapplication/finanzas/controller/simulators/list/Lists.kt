package co.japl.android.myapplication.finanzas.controller.simulators.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.com.japl.module.creditcard.views.simulator.SimulatorList
import co.com.japl.module.credit.views.simulator.SimulatorList

@Composable
fun ListSimulator(viewModel:ListViewModel){
    Column {
        Text(text = stringResource(id = R.string.credit_card_simulator_list))
        SimulatorList(navController = viewModel.navController)
        Text(text = stringResource(id = R.string.credit_simulator_list))
        SimulatorList(navController = viewModel.navController)
    }
}
