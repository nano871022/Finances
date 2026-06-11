package co.japl.android.myapplication.finanzas.widgets

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.finanzas.controller.widget.MonthlyToPayViewModel
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import co.japl.android.myapplication.finanzas.view.widgets.RecapToPayWidget
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate


class CardMonthlyToPayWidget: GlanceAppWidget()  {

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = Prefs(context)
        val vm = MonthlyToPayViewModel(context,prefs)
        vm.load()
        provideContent {
            RecapToPayWidget(vm)
        }
    }
}