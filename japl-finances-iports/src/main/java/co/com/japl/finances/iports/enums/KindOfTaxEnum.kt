package co.com.japl.finances.iports.enums

enum class KindOfTaxEnum (val value:String){
    ANUAL_EFFECTIVE("EA"),
    MONTHLY_EFFECTIVE("EM"),
    ANUAL_NOMINAL("NA"),
    MONTLY_NOMINAL("NM");

    fun getName():String{
        return value
    }

    companion object{
        fun findByValue(value:String?): KindOfTaxEnum {
            return KindOfTaxEnum.values().firstOrNull{ it.value == value }?: KindOfTaxEnum.MONTHLY_EFFECTIVE
        }
    }
}