package co.japl.android.finances.services.cache.interfaces

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO

interface ISimulatorCreditCache {
    fun save(dto: SimulatorCreditDTO):Long

    fun findById(id:Long): SimulatorCreditDTO?
}