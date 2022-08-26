package co.japl.android.myapplication.bussiness.DTO

import android.provider.BaseColumns
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.properties.Delegates

class CreditCardBought {
    var codeCreditCard :Int? = null
    var nameCreditCard:String? = null
    var nameItem  :String? = null
    var valueItem  :BigDecimal? = null
    var interest :Double? = null
    var month:Int? = null
    var boughtDate :LocalDateTime? = null
    var cutOutDate :LocalDateTime? = null
    var createDate :LocalDateTime? = null
    var id :Int? = null
    var recurrent :Short? = null
    var kind :Short? = null
    var quoteValue :BigDecimal? = null


}