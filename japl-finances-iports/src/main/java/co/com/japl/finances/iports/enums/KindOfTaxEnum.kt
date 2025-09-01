package co.com.japl.finances.iports.enums

import androidx.annotation.StringRes
import co.com.japl.finances.iports.R

enum class KindOfTaxEnum (val value:String, @StringRes val title:Int){
    ANUAL_EFFECTIVE("EA",R.string.anual_effective),
    MONTHLY_EFFECTIVE("EM",R.string.monthly_effective),
    ANUAL_NOMINAL("NA",R.string.anual_nominal),
    MONTLY_NOMINAL("NM",R.string.monthly_nominal);

    fun getName():String{
        return value
    }

    companion object{
        fun findByValue(value:String?): KindOfTaxEnum {
            return KindOfTaxEnum.entries.firstOrNull{ it.value == value }?: KindOfTaxEnum.MONTHLY_EFFECTIVE
        }

        fun findByIndex(index:Int?): KindOfTaxEnum {
            return index?.let{
                KindOfTaxEnum.entries.getOrNull(index)
            }?: KindOfTaxEnum.MONTHLY_EFFECTIVE
        }
    }
}