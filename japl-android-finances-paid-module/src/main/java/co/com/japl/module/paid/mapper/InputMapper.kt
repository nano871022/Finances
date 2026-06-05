package co.com.japl.module.paid.mapper

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.utils.NumbersUtil
import java.time.LocalDate

object InputMapper {

     fun mapper(
         id:Int?,
         date:String,
         codeAccount:Int,
         kindOfPayment:String,
         name:String,
         value:String
         ): InputDTO = InputDTO(
             id=id?:0,
             date=DateUtils.toLocalDate(date),
             accountCode=codeAccount,
             kindOf=kindOfPayment,
             name=name,
             value=NumbersUtil.toBigDecimal(value),
             dateStart=LocalDate.now(),
             dateEnd=LocalDate.MAX)

}