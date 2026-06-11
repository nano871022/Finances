package co.japl.android.myapplication.finanzas.controller.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import co.japl.android.myapplication.finanzas.widgets.CardMonthlyToPayWidget
import dagger.hilt.android.EntryPointAccessors

class MonthlyToPayWidgetReceiver  : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CardMonthlyToPayWidget()
}