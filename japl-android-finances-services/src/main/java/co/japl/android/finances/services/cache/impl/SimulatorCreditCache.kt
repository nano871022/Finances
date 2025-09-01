package co.japl.android.finances.services.cache.impl

import android.util.Log
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.japl.android.finances.services.cache.interfaces.ISimulatorCreditCache
import javax.inject.Inject

class SimulatorCreditCache @Inject constructor() : ISimulatorCreditCache {
    private val listSimulators= mutableListOf<SimulatorCreditDTO>()
    override fun save(dto: SimulatorCreditDTO): Long {
        if(dto.code > 0){
            Log.d(javaClass.simpleName,"<<<=== Save:: ${dto.code} $listSimulators")
            listSimulators.find { it.code == dto.code }?.let {
                val index = listSimulators.indexOfFirst { it.code == dto.code }
                listSimulators[index] = dto
            }?:listSimulators.add(dto)
            return dto.code.toLong()
        }else {
            val id = listSimulators.size + 1
            listSimulators.add(dto.copy(code = id))
            Log.d(javaClass.simpleName,"<<<=== Save:: $id $listSimulators")
            return id.toLong()
        }
    }

    override fun findById(id: Long): SimulatorCreditDTO? {
        Log.d(javaClass.simpleName,"<<<=== FindById:: $id $listSimulators")
        return listSimulators.firstOrNull { it.code.toLong() == id }
    }

}