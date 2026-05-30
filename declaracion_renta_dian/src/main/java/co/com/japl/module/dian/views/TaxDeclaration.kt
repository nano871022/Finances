package co.com.japl.module.dian.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.module.dian.controllers.TaxDeclarationViewModel
import co.com.japl.finances.iports.dtos.FinancialItemDTO
import co.com.japl.module.declaracion_renta_dian.R
import co.com.japl.ui.theme.values.Dimensions
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxDeclaration(viewModel: TaxDeclarationViewModel) {
    val declaration by viewModel.declarationState.collectAsState()
    val showCalculatedOnly by viewModel.showCalculatedOnly.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.tax_declaration_projection)) },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.show_calculated), style = MaterialTheme.typography.bodySmall)
                        Switch(checked = showCalculatedOnly, onCheckedChange = { viewModel.toggleShowCalculatedOnly(it) })
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            declaration?.let { decl ->
                if (decl.isObligatedToFile) {
                    ObligationBanner(decl.obligationReasons)
                }

                LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = Dimensions.PADDING_STANDARD)) {
                    item { SectionHeader(stringResource(R.string.section_patrimony)) }
                    item {
                        TaxLineItem("29", stringResource(R.string.gross_patrimony), decl.grossPatrimony, decl.assetDetails, showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("30", stringResource(R.string.debts), decl.debts, decl.debtDetails, showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("31", stringResource(R.string.net_patrimony), decl.netPatrimony, emptyList(), showCalculatedOnly, isBold = true)
                    }

                    item { SectionHeader(stringResource(R.string.section_income)) }

                    item {
                        TaxLineItem("32", stringResource(R.string.gross_income), decl.grossIncome, decl.incomeDetails, showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("33", stringResource(R.string.non_constitutive_income), decl.nonConstitutiveIncome, emptyList(), showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("34", stringResource(R.string.net_income), decl.netIncome, emptyList(), showCalculatedOnly, isBold = true)
                    }
                    item {
                        TaxLineItem("37", stringResource(R.string.tot_tax_exen), decl.exemptIncome25, emptyList(), showCalculatedOnly, isBold=true)
                    }
                    item {
                        TaxLineItem("40", stringResource(R.string.tot_deductions_imp), decl.deductions, decl.deductionDetails, showCalculatedOnly, isBold=true)
                    }
                    item {
                        TaxLineItem("41", stringResource(R.string.limited_exemptions), decl.limitedExemptionsAndDeductions, emptyList(), showCalculatedOnly)
                    }

                    item {
                        TaxLineItem("42", stringResource(R.string.tax_liq_ord), decl.taxLiqOrd, emptyList(), showCalculatedOnly, isBold = true)
                    }

                    item {
                        SectionHeader(stringResource(R.string.section_tax_capital))
                    }

                    item {
                        TaxLineItem("58", stringResource(R.string.gross_capital), decl.grossCapital, emptyList(), showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("59", stringResource(R.string.non_constitutive_capital), decl.nonConstitutiveCapital, emptyList(), showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("61", stringResource(R.string.tax_liq), decl.taxLiqCapital, emptyList(), showCalculatedOnly)
                    }

                    item {
                        TaxLineItem("70", stringResource(R.string.limited_exemptions), decl.taxLiqCapital, emptyList(), showCalculatedOnly)
                    }

                    item {
                        TaxLineItem("72", stringResource(R.string.tax_liq_ord), decl.taxLiqCapital, emptyList(), showCalculatedOnly)
                    }


                    item {
                        TaxLineItem("91", stringResource(R.string.tax_income_liq), decl.taxLiqGeneral, emptyList(), showCalculatedOnly)
                    }

                    item {
                        TaxLineItem("92", stringResource(R.string.tax_income_imp), decl.limitedExemptionsAndDeductions, emptyList(), showCalculatedOnly)
                    }

                    item {
                        TaxLineItem("93", stringResource(R.string.tax_income_ord), decl.taxLiqCedGen, emptyList(), showCalculatedOnly)
                    }

                    item {
                        TaxLineItem("97", stringResource(R.string.tax_income_grav), decl.taxLiqCedGen, emptyList(), showCalculatedOnly)
                    }

                    item {
                            SectionHeader(stringResource(R.string.section_settlement))
                    }
                    item {
                        TaxLineItem("117", stringResource(R.string.tax_on_income), decl.taxOnTaxableBase, emptyList(), showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("122", stringResource(R.string.tax_on_income_grav), decl.taxOnTaxableBase, emptyList(), showCalculatedOnly, isBold=true)
                    }
                    item {
                        TaxLineItem("127", stringResource(R.string.net_tax_on_income), decl.taxOnTaxableBase, emptyList(), showCalculatedOnly,isBold=true)
                    }
                    item {
                        TaxLineItem("133", stringResource(R.string.withholdings), decl.withholdings, decl.withholdingDetails, showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("134", stringResource(R.string.next_year_advance), decl.nextYearAdvance, emptyList(), showCalculatedOnly)
                    }
                    item {
                        TaxLineItem("137", stringResource(R.string.balance_to_pay), decl.balanceToPay, emptyList(), showCalculatedOnly, isBold = true, color = MaterialTheme.colorScheme.error)
                    }
                    item {
                        TaxLineItem("138", stringResource(R.string.balance_in_favor), decl.balanceInFavor, emptyList(), showCalculatedOnly, isBold = true, color = Color.Green)
                    }
                }
            }
        }
    }
}

@Composable
fun ObligationBanner(reasons: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(Dimensions.PADDING_STANDARD),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(Dimensions.PADDING_STANDARD)) {
            Text(stringResource(R.string.obligated_to_file), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
            reasons.forEach { reason ->
                Text("• $reason", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(vertical = Dimensions.PADDING_VIEW_SPACE)
    )
}

@Composable
fun TaxLineItem(
    line: String,
    label: String,
    value: BigDecimal,
    details: List<FinancialItemDTO>,
    showCalculatedOnly: Boolean,
    isBold: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    if (showCalculatedOnly && value == BigDecimal.ZERO) return

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = Dimensions.PADDING_SHORT)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = details.isNotEmpty()) { expanded = !expanded }
                .padding(vertical = Dimensions.PADDING_SHORT),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = line, modifier = Modifier.width(Dimensions.TAX_LINE_NUMBER_WIDTH), style = MaterialTheme.typography.bodySmall)
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = if (isBold) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                color = color
            )
            Text(
                text = NumbersUtil.COPtoString(value),
                style = if (isBold) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                color = color
            )
            if (details.isNotEmpty()) {
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
            } else {
                Spacer(modifier = Modifier.width(Dimensions.ICON_SIZE_MEDIUM))
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = Dimensions.TAX_LINE_NUMBER_WIDTH, end = Dimensions.ICON_SIZE_MEDIUM, bottom = Dimensions.PADDING_VIEW_SPACE)) {
                details.forEach { item ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = Dimensions.PADDING_SHORT)) {
                        Text(item.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                        Text(NumbersUtil.COPtoString(item.value), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        HorizontalDivider(thickness = Dimensions.DIVIDER_THICKNESS, color = MaterialTheme.colorScheme.outlineVariant)
    }
}
