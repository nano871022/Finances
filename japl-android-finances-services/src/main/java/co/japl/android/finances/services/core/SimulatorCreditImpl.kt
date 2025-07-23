package co.japl.android.finances.services.core

import android.util.Log
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.outbounds.ISimulatorCreditPort
import co.japl.android.finances.services.cache.interfaces.ISimulatorCreditCache
import co.japl.android.finances.services.core.mapper.SimulatorCreditMapper
import co.japl.android.finances.services.dao.interfaces.ISimulatorCreditDAO
import co.japl.android.finances.services.enums.CalcEnum
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

class SimulatorCreditImpl @Inject constructor(private val simulatorSvc: ISimulatorCreditDAO,private val simulatorCache: ISimulatorCreditCache):
    ISimulatorCreditPort {

    override fun save(dto: SimulatorCreditDTO,cache:Boolean): Long {
        if(cache){
            return simulatorCache.save(dto)
        }else {
            val entity = SimulatorCreditMapper.mapper(dto)
            return simulatorSvc.save(entity)
        }
    }

    override fun getAll(isVariable:Boolean): List<SimulatorCreditDTO> {
        if(isVariable) {
            return simulatorSvc.getByVariable().map { SimulatorCreditMapper.mapper(it) }
        }else {
            return simulatorSvc.getByFix().map { SimulatorCreditMapper.mapper(it) }
        }
    }

    override fun delete(code: Int): Boolean {
        return simulatorSvc.delete(code)
    }

    override fun findByCode(
        code: Int,
        cache: Boolean
    ): SimulatorCreditDTO? {
        if(cache){
            return simulatorCache.findById(code.toLong())
        }else{
            simulatorSvc.get(code).getOrNull()?.let {
                return@findByCode SimulatorCreditMapper.mapper(it)
            }
        }
        return null
    }
}