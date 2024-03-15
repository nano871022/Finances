package co.japl.android.finances.services.core

import android.database.CursorWindowAllocationException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.core.mapper.TaxMapper
import co.japl.android.finances.services.dao.interfaces.ITaxDAO
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import javax.inject.Inject

class TaxImpl @Inject constructor(private val  taxSvc: ITaxDAO): ITaxPort {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(codCreditCard: Int, month: Int, year: Int, kind: KindInterestRateEnum): TaxDTO? {
        try {
            val kindOfTax = when(kind) {
                KindInterestRateEnum.CASH_ADVANCE -> co.japl.android.finances.services.enums.TaxEnum.CASH_ADVANCE
                KindInterestRateEnum.WALLET_BUY -> co.japl.android.finances.services.enums.TaxEnum.WALLET_BUY
                else-> co.japl.android.finances.services.enums.TaxEnum.CREDIT_CARD
            }
            val tax = taxSvc.get(
                codCreditCard.toLong(),
                month,
                year,
                kindOfTax
            )
            if (tax.isPresent) {
                return TaxMapper.mapper(tax.get())
            }
        }catch(e: CursorWindowAllocationException){
            Log.e(javaClass.name,e.message,e)
        }
        return null

    }

    override fun getByCreditCard(codCreditCard: Int): List<TaxDTO>? {
        return taxSvc.getAll().filter { it.codCreditCard == codCreditCard }.map { TaxMapper.mapper(it) }
    }

    override fun delete(code: Int): Boolean {
        return taxSvc.delete(code)
    }

    override fun enable(code: Int): Boolean {
        val rate = taxSvc.get(code)
        if(rate.isPresent && rate.get().status == 0.toShort()) {
            rate.get().status = 1
            return taxSvc.save(rate.get()) > 0
        }
        return false
    }

    override fun disable(code: Int): Boolean {
        val rate = taxSvc.get(code)
        if(rate.isPresent && rate.get().status == 1.toShort()) {
            rate.get().status = 0
            return taxSvc.save(rate.get()) > 0
        }
        return false
    }

    override fun create(dto: TaxDTO): Boolean {
        require(dto.id == 0, {"The id must be 0 to create new record"})
        return taxSvc.save(TaxMapper.mapper(dto)) > 0
    }

    override fun update(dto: TaxDTO): Boolean {
        require(dto.id != 0, {"The id must not be 0 to update record"})
        return taxSvc.save(TaxMapper.mapper(dto)) > 0
    }

    override fun getById(codeCreditRate: Int): TaxDTO? {
        val response = taxSvc.get(codeCreditRate)
        if(response.isPresent){
            return TaxMapper.mapper(response.get())
        }
        return null
    }
}