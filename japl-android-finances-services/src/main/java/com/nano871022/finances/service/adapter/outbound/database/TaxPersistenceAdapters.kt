package com.nano871022.finances.service.adapter.outbound.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.nano871022.finances.iport.dto.PatrimonyAssetDTO
import com.nano871022.finances.iport.ports.outbound.PatrimonyPersistencePort
import co.japl.android.finances.services.interfaces.IConnectDB
import javax.inject.Inject

class PatrimonyPersistenceAdapter @Inject constructor(
    private val db: SQLiteDatabase
) : PatrimonyPersistencePort {

    override suspend fun saveAsset(asset: PatrimonyAssetDTO) {
        val values = ContentValues().apply {
            put("name", asset.name)
            put("value", asset.value.toPlainString())
            put("type", asset.type)
        }
        if (asset.id != null) {
            db.update("dian_manual_patrimony_inputs", values, "id = ?", arrayOf(asset.id.toString()))
        } else {
            db.insert("dian_manual_patrimony_inputs", null, values)
        }
    }

    override suspend fun getAssets(): List<PatrimonyAssetDTO> {
        val cursor = db.query("dian_manual_patrimony_inputs", null, null, null, null, null, null)
        val assets = mutableListOf<PatrimonyAssetDTO>()
        with(cursor) {
            while (moveToNext()) {
                assets.add(
                    PatrimonyAssetDTO(
                        id = getLong(getColumnIndexOrThrow("id")),
                        name = getString(getColumnIndexOrThrow("name")),
                        value = getString(getColumnIndexOrThrow("value")).toBigDecimal(),
                        type = getString(getColumnIndexOrThrow("type"))
                    )
                )
            }
        }
        cursor.close()
        return assets
    }

    override suspend fun deleteAsset(id: Long) {
        db.delete("dian_manual_patrimony_inputs", "id = ?", arrayOf(id.toString()))
    }
}

class TaxHistoryPersistenceAdapter @Inject constructor(
    private val db: SQLiteDatabase
) : com.nano871022.finances.iport.ports.outbound.TaxHistoryPersistencePort {

    override suspend fun saveHistory(history: com.nano871022.finances.iport.dto.TaxHistoryDTO) {
        val values = ContentValues().apply {
            put("fiscal_year", history.fiscalYear)
            put("tax_value_cop", history.taxValueCOP.toPlainString())
            put("date", history.date.toString())
        }
        db.insert("dian_tax_declarations_history", null, values)
    }

    override suspend fun getHistory(): List<com.nano871022.finances.iport.dto.TaxHistoryDTO> {
        val cursor = db.query("dian_tax_declarations_history", null, null, null, null, null, "date DESC")
        val history = mutableListOf<com.nano871022.finances.iport.dto.TaxHistoryDTO>()
        with(cursor) {
            while (moveToNext()) {
                history.add(
                    com.nano871022.finances.iport.dto.TaxHistoryDTO(
                        id = getLong(getColumnIndexOrThrow("id")),
                        fiscalYear = getInt(getColumnIndexOrThrow("fiscal_year")),
                        taxValueCOP = getString(getColumnIndexOrThrow("tax_value_cop")).toBigDecimal(),
                        date = java.time.LocalDate.parse(getString(getColumnIndexOrThrow("date")))
                    )
                )
            }
        }
        cursor.close()
        return history
    }
}
