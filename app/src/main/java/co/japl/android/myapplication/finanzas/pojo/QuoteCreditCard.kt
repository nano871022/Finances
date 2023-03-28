package co.japl.android.myapplication.finanzas.pojo

import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.utils.CalcEnum
import java.math.BigDecimal
import java.util.*

class QuoteCreditCard {
    lateinit var value:Optional<BigDecimal>
    lateinit var period:Optional<Long>
    lateinit var tax:Optional<Double>
    lateinit var response:Optional<BigDecimal>
    lateinit var name:Optional<String>
    lateinit var type:CalcEnum
    lateinit var interestValue:Optional<BigDecimal>
    lateinit var capitalValue:Optional<BigDecimal>
    lateinit var kindOfTax:Optional<String>

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmName("getInterestValue1")
    fun getInterestValue():Optional<BigDecimal> {
        if(this::interestValue.isInitialized){
            return interestValue
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmName("getCapital1")
    fun getCapitalValue():Optional<BigDecimal> {
        if(this::capitalValue.isInitialized){
            return capitalValue
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @JvmName("getResponse1")
    fun getResponse():Optional<BigDecimal> {
        if(this::response.isInitialized){
            return response
        }
        return Optional.empty()
    }

}