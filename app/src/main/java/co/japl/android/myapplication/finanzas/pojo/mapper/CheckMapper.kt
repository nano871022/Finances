package co.japl.android.myapplication.finanzas.pojo.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.enums.CheckPaymentsEnum
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard

class CheckMapper {
    fun mapper(dto:PaidDTO,period:String):CheckPaymentsPOJO =
         CheckPaymentsPOJO(0,dto.id.toLong(), false, dto.name,dto.value,null,period,CheckPaymentsEnum.PAYMENTS)

    fun mapper(dto:CreditDTO,period:String):CheckPaymentsPOJO =
        CheckPaymentsPOJO(0,dto.id.toLong(), false, dto.name,dto.quoteValue,null,period,CheckPaymentsEnum.CREDITS)

    @RequiresApi(Build.VERSION_CODES.N)
    fun mapper(dto:QuoteCreditCard, period:String):CheckPaymentsPOJO =
        CheckPaymentsPOJO(0,dto.creditCardId.get().toLong(), false, dto.name.get(),dto.capitalValue.get() + dto.interestValue.get(),null,period,CheckPaymentsEnum.QUOTE_CREDIT_CARD)
}