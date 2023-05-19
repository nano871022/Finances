package co.japl.android.myapplication.finanzas.pojo.mapper

import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.pojo.CheckPaymentsPOJO

class CheckPaymentsMapper {
    public fun mapper(dto:PaidDTO,period:String):CheckPaymentsPOJO =
         CheckPaymentsPOJO(0,dto.id.toLong(), false, dto.name,dto.value,null,period)
}