package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.CalcDB
import co.japl.android.finances.services.dto.CalcDTO
import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import co.japl.android.finances.services.dto.QuoteCreditCard
import co.japl.android.finances.services.enums.KindOfTaxEnum
import co.japl.android.finances.services.enums.CalcEnum
import java.math.BigDecimal

class CalcMap {
    fun mapping(dto:CalcDTO ):ContentValues{
        return ContentValues().apply {
            put(CalcDB.CalcEntry.COLUMN_ALIAS,dto.name)
            put(CalcDB.CalcEntry.COLUMN_INTEREST,dto.interest)
            put(CalcDB.CalcEntry.COLUMN_PERIOD,dto.period)
            put(CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT,dto.quoteCredit.toDouble())
            put(CalcDB.CalcEntry.COLUMN_VALUE_CREDIT,dto.valueCredit.toDouble())
            put(CalcDB.CalcEntry.COLUMN_TYPE,dto.type)
            put(CalcDB.CalcEntry.COLUMN_INTEREST_VALUE,dto.interestValue.toDouble())
            put(CalcDB.CalcEntry.COLUMN_CAPITAL_VALUE,dto.capitalValue.toDouble())
            put(CalcDB.CalcEntry.COLUMN_KIND_OF_TAX,dto.kindOfTax)
        }
    }

    fun mapping(cursor:Cursor):CalcDTO{
        val name = cursor.getString(1)
        val value = cursor.getString(6).toString().toBigDecimal()
        val interest = cursor.getString(3).toDouble()
        val period = cursor.getString(4).toLong()
        val quote = cursor.getString(5).toBigDecimal()
        var type = cursor.getString(2)
        var interestValue = cursor.getString(7).toBigDecimal()
        var capitalValue = cursor.getString(8).toBigDecimal()
        val kindOfTax = cursor.getString(9)
        if(type == null || type.isBlank()){
            type = "fix"
        }
        val id = cursor.getString(0).toInt()
        return CalcDTO(name
            ,value
            ,interest
            ,period
            ,quote
            ,type
            ,id
            ,interestValue
            ,capitalValue
            ,kindOfTax)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun mapping(quoteCreditCard: QuoteCreditCard):CalcDTO{
        return CalcDTO(quoteCreditCard?.name?.orElse("Empty Name")?:"Empty Name"
            ,quoteCreditCard.value.orElse(BigDecimal.ZERO)
            ,quoteCreditCard.tax.orElse(0.0)
            ,quoteCreditCard.period.orElse(0)
            ,quoteCreditCard.response.orElse(BigDecimal.ZERO)
            , quoteCreditCard?.type?.toString()?: CalcEnum.FIX.toString()
            ,0
            , quoteCreditCard.interestValue.orElse(BigDecimal.ZERO)
            , quoteCreditCard.capitalValue.orElse(BigDecimal.ZERO)
            , quoteCreditCard.kindOfTax.orElse(KindOfTaxEnum.EM.name)
        )
    }

    fun mapping(creditCardBought:CreditCardBoughtDTO,quoteValue:BigDecimal,interestValue:BigDecimal,capitalValue:BigDecimal,kindOfTax: KindOfTaxEnum):CalcDTO{
        return CalcDTO(creditCardBought.nameItem,
        creditCardBought.valueItem,
        creditCardBought.interest,
        creditCardBought.month.toLong(),
        quoteValue,
        CalcEnum.VARIABLE.toString(),
        creditCardBought.id,
        interestValue,
        capitalValue,
        kindOfTax.name)

    }
}