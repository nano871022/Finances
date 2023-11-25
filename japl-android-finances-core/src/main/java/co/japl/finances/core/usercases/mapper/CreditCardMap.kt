package co.japl.finances.core.usercases.mapper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.finances.core.dto.CreditCardDTO
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.utils.Config
import java.util.*

class CreditCardMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapper(creditCard:CreditCardDTO): CreditCard {
        val pojo = CreditCard()
        pojo.codeCreditCard = Optional.ofNullable(creditCard.id)
        pojo.nameCreditCard = Optional.ofNullable(creditCard.name)
        Log.d(this.javaClass.name,"CutOffDay: ${creditCard.cutOffDay}")
        pojo.cutoffDay = Optional.ofNullable(creditCard.cutOffDay)
        pojo.cutOff =
            Optional.ofNullable(creditCard.cutOffDay.toInt()
                ?.let { it1 -> Config.nextCutOff( it1) })
        pojo.warningValue = Optional.ofNullable(creditCard.warningValue)
        pojo.maxQuotes = Optional.ofNullable(creditCard.maxQuotes)
        return pojo
    }

}