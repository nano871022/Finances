package co.japl.android.finances.services.interfaces

import java.time.LocalDateTime

interface ConfigSvc {

    fun variableTaxCreditMonthly(): Double

    fun nextCutOff(cutOffDay:Int):LocalDateTime
}