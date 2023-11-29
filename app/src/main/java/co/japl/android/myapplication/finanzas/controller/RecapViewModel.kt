package co.japl.android.myapplication.finanzas.controller

import androidx.lifecycle.ViewModel

class RecapViewModel  : ViewModel(){

    private var _totalInbound:Double = 0.0
    private var _totalPayment:Double = 0.0
    private var _totalSaved:Double = 0.0
    private var _projectionsValue:Double = 0.0
    private var _totalPayed:Double = 0.0
    private var _totalCredits:Double = 0.0
    private var _totalCreditCard:Double = 0.0
    private var _warningValue:Double = 0.0

    val totalInbound :Double get() = _totalInbound
    val totalPayment : Double get() = _totalPayment
    val totalSaved : Double get() = _totalSaved
    val projectionsValue : Double get() = _projectionsValue
    val totalPayed : Double get() = _totalPayed
    val totalCredits : Double get() = _totalCredits
    val totalCreditCard : Double get() = _totalCreditCard
    val warningValue : Double get() = _warningValue

    fun setTotalInbound(total:Double){
        _totalInbound = total
    }
    fun setTotalPayment(total:Double){
        _totalPayment = total
    }
    fun setTotalSaved(total:Double){
        _totalSaved = total
    }
    fun setProjectionsValue(total:Double){
        _projectionsValue = total
    }
    fun setTotalPayed(total:Double){
        _totalPayed = total
    }
    fun setTotalCredits(total:Double){
        _totalCredits = total
    }
    fun setTotalCreditCard(total:Double) {
        _totalCreditCard = total
    }
    fun setWarningValue(total:Double) {
        _warningValue = total
    }

}