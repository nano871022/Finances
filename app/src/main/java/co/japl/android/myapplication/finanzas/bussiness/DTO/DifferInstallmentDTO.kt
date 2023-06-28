package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class DifferInstallmentDTO(
    var id:Int,
    var create:LocalDate,
    var cdBoughtCreditCard:Long,
    var pendingValuePayable:Double,
    var originValue:Double,
    var newInstallment:Double,
    var oldInstallment:Double
)

object DifferInstallmentDB{
    object Entry{
        const val TABLE_NAME = "TB_DIFFER_INSTALLMENT"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_CODE_QUOTE = "cd_bought_cc"
        const val COLUMN_PENDING_VALUE_PAYABLE = "nbr_pending_payable"
        const val COLUMN_ORIGIN_VALUE = "nbr_origin_value"
        const val COLUMN_NEW_INSTALLMENT = "nbr_new_installment"
        const val COLUMN_OLD_INSTALLMENT = "nbr_old_installment"
    }
}
