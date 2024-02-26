package co.japl.android.myapplication.finanzas.bussiness.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class TagDTO(
    val id:Int,
    val create:LocalDate,
    val name:String,
    val active:Boolean
)

object TagDB{
    object Entry{
        const val TABLE_NAME = "TB_TAG"
        const val COLUMN_DATE_CREATE = "dt_create"
        const val COLUMN_NAME = "str_name"
        const val COLUMN_ACTIVE = "nbr_active"
    }
}
