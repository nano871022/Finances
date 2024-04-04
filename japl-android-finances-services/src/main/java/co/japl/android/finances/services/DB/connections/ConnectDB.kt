package co.japl.android.finances.services.DB.connections

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.utils.DatabaseConstants

class ConnectDB(context: Context):SQLiteOpenHelper(context,
        DatabaseConstants.DATA_BASE_NAME,null, 4_05_05_082) {

    val list = arrayListOf<IConnectDB>(
        CalculationConnectDB(),
                CreditCardConnectDB(),
                CreditCardBoughtConnectDB(),
                TaxConnectDB(),
                CreditCardSettingConnectDB(),
    BuyCreditCardSettingConnectDB(),
    CreditFixConnectDB(),
    AdditionalCreditConnectDB(),
    AddToCapitalConnectDB(),
    PaidConnectDB(),
    InputConnectDB(),
    AccountConnectDB(),
    ProjectionsDB(),
    CheckPaymentConnectDB(),
    CheckQuoteConnectDB(),
    CheckCreditConnectDB(),
    DifferInstallmentConnectDB(),
    GracePeriodConnectDB(),
    AddValueAmortizationConnectDB(),
    ExtraValueAmortizationCreditConnectDB(),
    ExtraValueAmortizationQuoteCreditCardConnectDB(),
    TagConnectDB(),
    TagQuoteCreditCardConnectDB(),
    SmsCreditCardConnectDB(),
    SmsPaidConnectDB()
    )

    override fun onCreate(p0: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== onCreate - Start $p0")
        list.forEach{
            it.onCreate(p0)
        }
        Log.i(this.javaClass.name,"<<<=== onCreate - End")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i(this.javaClass.name,"<<<=== onUpgrade - Start $p1 - $p2")
        list.forEach {
            it.onUpgrade(p0, p1, p2)
        }
        Log.i(this.javaClass.name,"<<<=== onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== onDowngrade - Start $oldVersion - $newVersion")
        list.forEach {
          it.onDowngrade(db, oldVersion, newVersion)
        }
        Log.i(this.javaClass.name,"<<<=== onDowngrade - End")
    }

    fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?){
        Log.i(this.javaClass.name,"<<<=== onRestore - Start ")
        list.forEach {
            it.onRestore(currentDB, fromRestoreDB)
        }
        Log.i(this.javaClass.name,"<<<=== onRestore - End")
    }

}