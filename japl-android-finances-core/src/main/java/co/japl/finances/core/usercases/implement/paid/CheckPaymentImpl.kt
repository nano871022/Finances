package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.com.japl.finances.iports.dtos.PeriodCheckPaymentDTO
import co.com.japl.finances.iports.enums.CheckPaymentsEnum
import co.com.japl.finances.iports.outbounds.IPaidCheckPaymentPort
import co.com.japl.finances.iports.outbounds.IPaidPort
import co.japl.finances.core.usercases.interfaces.paid.ICheckPayment
import java.time.YearMonth
import javax.inject.Inject

class CheckPaymentImpl @Inject constructor(private val svc:IPaidCheckPaymentPort, private val paidSvc:IPaidPort):ICheckPayment {
    override fun getPeriodsPayment(): List<PeriodCheckPaymentDTO> {
        return svc.getPeriodsPayment()
    }

    override fun getCheckPayments(period: YearMonth): List<CheckPaymentDTO> {
        val response = mutableListOf<CheckPaymentDTO>()
        val list =  svc.getCheckPayments(period).takeIf { it.isNotEmpty() }?.map {
            paidSvc.get(it.codPaid.toInt())?.let {paid->
                it.name = paid.itemName
                it.amount = paid.itemValue
            }
            it
        }?: emptyList()
        response.addAll(list)
        paidSvc.findByRecurrent(period).takeIf { it.isNotEmpty() && it.size > list.size }
            ?.filter { paid-> list.none {  it.codPaid == paid.id.toLong() } }
            ?.map {
            CheckPaymentDTO(
                id = 0,
                name = it.itemName,
                amount = it.itemValue,
                date = it.datePaid,
                codPaid = it.id.toLong(),
                period = period,
                type = CheckPaymentsEnum.PAYMENTS
            )
        }?.let(response::addAll)
        return response
    }

    override fun update(check: CheckPaymentDTO): Boolean {
        return svc.save(check).id > 0
    }

    override fun save(check: CheckPaymentDTO): CheckPaymentDTO {
        return svc.save(check)
    }
}