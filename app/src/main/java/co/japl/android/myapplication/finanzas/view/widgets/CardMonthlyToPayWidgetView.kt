package co.japl.android.myapplication.finanzas.view.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.background
import androidx.glance.GlanceTheme
import androidx.glance.layout.Alignment
import androidx.glance.text.FontWeight
import co.japl.android.myapplication.R
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.appwidget.cornerRadius
import co.com.japl.ui.utils.WindowWidthSize
import androidx.glance.LocalSize
import co.com.japl.finances.iports.dtos.RecapDTO
import co.com.japl.ui.theme.WidgetTheme
import co.japl.android.myapplication.finanzas.controller.widget.MonthlyToPayViewModel

@Composable
   fun RecapToPayWidget(vm: MonthlyToPayViewModel){

            GlanceTheme(colors= WidgetTheme.colors) {
                vm.dto?.fold(
                    onSuccess = { recap ->
                        WidgetContent(
                            context = vm.context,
                            recap=recap,
                            value=vm::value
                        )
                    },
                    onFailure = { error ->
                        Log.e("CardMonthlyToPayWidget", "Error fetching recap", error)
                        ErrorContent()
                    }
                )
            }
        }

    @Composable
    private fun ErrorContent() {
        Column(
            modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.errorContainer).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = "Error cargando datos", 
                style = TextStyle(color = GlanceTheme.colors.onErrorContainer, fontWeight = FontWeight.Bold)
            )
        }
    }

    @SuppressLint("UnusedBoxWithConstraintsScope")
    @Composable
    private fun WidgetContent(context: Context,recap: RecapDTO,value:(Double, WindowWidthSize)->String) {
        val size = LocalSize.current
        if(WindowWidthSize.NANO.isEqualTo(size.width)) {
            ContentNano(context, recap,value)
        }else if(WindowWidthSize.MICRO.isEqualTo(size.width)) {
            ContentCompact(context, recap,value)
        }else{
            ContentLarge(context, recap,value)
        }
    }

@Composable
private fun ContentNano(context: Context, recap: RecapDTO, value:(Double, WindowWidthSize)->String) {
    val total = recap.totalPaid + recap.totalQuoteCredit + recap.totalQuoteCreditCard
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(16.dp)
            .cornerRadius(16.dp)
    ) {
        Text(
            text = context.getString(R.string.to_pay),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onSurface,
            ),
            modifier = GlanceModifier.padding(bottom = 10.dp)
        )

        FieldRow(label=R.string.total_paid_sshort,value=value(recap.totalPaid,
            WindowWidthSize.NANO),context=context)
        FieldRow(label=R.string.total_credits_sshort,value= value(recap.totalQuoteCredit,
            WindowWidthSize.NANO),context=context)
        FieldRow(label=R.string.total_quote_credit_card_sshort, value=value(recap.totalQuoteCreditCard,
            WindowWidthSize.NANO),context=context)

        Spacer(
            modifier = GlanceModifier.height(1.dp).fillMaxWidth()
                .background(GlanceTheme.colors.onPrimary)
        )

        FieldRow(label=R.string.total_short,value=value(total, WindowWidthSize.NANO),
            isTotal = true,context=context

        )
    }
}

    @Composable
    private fun ContentCompact(context: Context, recap: RecapDTO, value:(Double, WindowWidthSize)->String) {
        val total = recap.totalPaid + recap.totalQuoteCredit + recap.totalQuoteCreditCard
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(16.dp)
                .cornerRadius(16.dp)
        ) {
            Text(
                text = context.getString(R.string.to_pay),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.onSurface,
                ),
                modifier = GlanceModifier.padding(bottom = 10.dp)
            )

            FieldRow(label=R.string.total_paid_short,value=value(recap.totalPaid,
                WindowWidthSize.COMPACT),context=context)
            FieldRow(label=R.string.total_credits_short,value= value(recap.totalQuoteCredit,
                WindowWidthSize.COMPACT),context=context)
            FieldRow(label=R.string.total_quote_credit_card_short, value=value(recap.totalQuoteCreditCard,
                WindowWidthSize.COMPACT),context=context)

            Spacer(
                modifier = GlanceModifier.height(1.dp).fillMaxWidth()
                    .background(GlanceTheme.colors.onPrimary)
            )

            FieldRow(label=R.string.total,value=value(total, WindowWidthSize.COMPACT),
                isTotal = true,context=context

            )
        }
    }

    @Composable
    private fun ContentLarge(context: Context, recap: RecapDTO,value:(Double, WindowWidthSize)->String){
        val total = recap.totalPaid + recap.totalQuoteCredit + recap.totalQuoteCreditCard
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(16.dp)
                .cornerRadius(16.dp)
        ) {
            Text(
                text = context.getString(R.string.to_pay),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 16.sp
                ),
                modifier = GlanceModifier.padding(bottom = 10.dp)
            )

            FieldRow(label=R.string.total_paid, value=value(recap.totalPaid, WindowWidthSize.LARGE),context=context)
            FieldRow(label=R.string.total_credits, value=value(recap.totalQuoteCredit, WindowWidthSize.LARGE), context=context)
            FieldRow(label=R.string.total_quote_credit_card, value=value(recap.totalQuoteCreditCard, WindowWidthSize.LARGE),context=context)

            Spacer(
                modifier = GlanceModifier.height(1.dp).fillMaxWidth()
                    .background(GlanceTheme.colors.onPrimary)
            )

            FieldRow(
                label=R.string.total,
                value=value(total, WindowWidthSize.LARGE),
                isTotal = true,
                context=context
            )
        }
    }

    @Composable
    private fun FieldRow(@StringRes label: Int, value: String, isTotal: Boolean = false,context:Context) {
        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = context.getString(label),
                style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant),
                modifier = GlanceModifier.defaultWeight()
            )
            Text(
                text = value,
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
                )
            )
        }
    }


