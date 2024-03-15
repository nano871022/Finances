package co.japl.android.finances.services.core

import android.database.CursorWindowAllocationException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.core.mapper.BuyCreditCardSettingMapper
import co.japl.android.finances.services.interfaces.IBuyCreditCardSettingSvc
import co.com.japl.finances.iports.outbounds.IBuyCreditCardSettingPort
import co.com.japl.finances.iports.dtos.BuyCreditCardSettingDTO
import javax.inject.Inject

class BuyCreditCardSettingImpl @Inject constructor(private val buyCCSettingSvc:IBuyCreditCardSettingSvc) : IBuyCreditCardSettingPort {
    override fun getAll(): List<BuyCreditCardSettingDTO> {
        return buyCCSettingSvc.getAll().map(BuyCreditCardSettingMapper::mapper)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(id: Int): BuyCreditCardSettingDTO? {
        try {
            val buyCCSetting = buyCCSettingSvc.get(id)
            if (buyCCSetting.isPresent) {
                return BuyCreditCardSettingMapper.mapper(buyCCSetting.get())
            }
        }catch (e:CursorWindowAllocationException){
            Log.e(javaClass.name,e.message,e)
        }
        return null
    }

    override fun create(dto: BuyCreditCardSettingDTO): Int {
        require(dto.id == 0){"id must be 0"}
        return buyCCSettingSvc.save(BuyCreditCardSettingMapper.mapper(dto)).toInt()
    }

    override fun update(dto: BuyCreditCardSettingDTO): Boolean {
        require(dto.id > 0){"id must be > 0"}
        return buyCCSettingSvc.save(BuyCreditCardSettingMapper.mapper(dto)) > 0
    }

    override fun delete(id: Int): Boolean {
        return buyCCSettingSvc.delete(id)
    }
}