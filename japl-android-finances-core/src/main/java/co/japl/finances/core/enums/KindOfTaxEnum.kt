package co.japl.finances.core.enums

enum class KindOfTaxEnum (val value:String){
    ANUAL_EFFECTIVE("EA"),
    MONTHLY_EFFECTIVE("EM"),
    ANUAL_NOMINAL("NA"),
    MONTLY_NOMINAL("NM");

    fun getName():String{
        return value
    }
}