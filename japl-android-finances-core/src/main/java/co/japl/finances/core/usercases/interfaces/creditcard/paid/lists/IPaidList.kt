package co.japl.finances.core.usercases.interfaces.creditcard.paid.lists

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO

interface IPaidList {
    fun getBoughtPeriodList(idCreditCard: Int,cache:Boolean): List<BoughtCreditCardPeriodDTO>?
}