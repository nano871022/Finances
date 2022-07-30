package co.japl.android.myapplication.bussiness.interfaces

import java.time.LocalDateTime

interface ConfigSvc {

    fun variableTaxCreditMonthly(): Double

    fun nextCutOff(cutOffDay:Int):LocalDateTime
}