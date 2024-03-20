package co.japl.finances.core.usercases.implement.creditcard.bought

import co.com.japl.finances.iports.dtos.CreditCardBoughtDTO
import co.com.japl.finances.iports.outbounds.IQuoteCreditCardPort
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.ITax
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import co.japl.finances.core.utils.DateUtils
import javax.inject.Inject

class BoughtSmsImpl @Inject constructor(private val creditRateSvc:ITax, private val boutSvc:IBought,private val creditCardSvc:ICreditCard,private val boughtSvc: IQuoteCreditCardPort) : IBoughtSms{

    override fun createBySms(dto: CreditCardBoughtDTO): Boolean {
        creditCardSvc.get(dto.codeCreditCard)?.let {cc->
            dto.nameCreditCard = cc.name
            DateUtils.cutOffLastMonth(cc.cutOffDay)?.let{dto.cutOutDate = it}
            creditRateSvc.getLatest(dto.codeCreditCard, dto.kind)?.let {
                dto.interest = it.value
                dto.month = it.period.takeIf { it > 1 }?.let{it.toInt()}?:1
                it.kindOfTax?.let{dto.kindOfTax = it}
            }
        }
        return boughtSvc.findByNameAndBoughtDateAndValue(dto.nameItem,dto.boughtDate,dto.valueItem)?.let{
            return false
        }?:boutSvc.create(dto,false) > 0
    }


}