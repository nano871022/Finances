package co.japl.android.myapplication.bussiness.impl

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.bussiness.mapping.CreditCardBoughtMap
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

class SaveCreditCardBoughtImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<CreditCardBoughtDTO>,
    SearchSvc<CreditCardBoughtDTO> {
    private val COLUMNS_CALC = arrayOf(BaseColumns._ID
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_NAME_ITEM
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_VALUE_ITEM
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_INTEREST
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_BOUGHT_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CUT_OUT_DATE
        ,CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CREATE_DATE)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardBoughtDTO): Boolean {
        Log.v(this.javaClass.name,"<<<=== save - Start")
        try{
        val db = dbConnect.writableDatabase
        val dto = CreditCardBoughtMap().mapping(dto)
        return (db?.insert(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,null,dto)!! > 0).also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== save - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override  fun getAll():List<CreditCardBoughtDTO>{
        Log.v(this.javaClass.name,"<<<=== getAll - Start")
        try {
            val db = dbConnect.readableDatabase

            val cursor = db.query(
                CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
                COLUMNS_CALC,
                null,
                null,
                null,
                null,
                null
            )
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor) {
                while (moveToNext()) {
                    items.add(CreditCardBoughtMap().mapping(this))
                }
            }

            return items.also { Log.v(this.javaClass.name,"${it.size}") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getAll - End")
        }
    }

    override fun delete(id:Int):Boolean{
        Log.v(this.javaClass.name,"<<<=== delete - Start")
        try{
        val db = dbConnect.writableDatabase
        return (db.delete(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
            DatabaseConstants.SQL_DELETE_CALC_ID, arrayOf(id.toString())) > 0).also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== delete - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getToDate(key:Int,cutOffDate: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.v(this.javaClass.name,"<<<=== getToDate - Start")
        try {
            val db = dbConnect.readableDatabase

            val startDate = cutOffDate.minusMonths(1).plusDays(1)
            val startDateStr = DateUtils.localDateTimeToString(startDate)
            val endDateStr = DateUtils.localDateTimeToString(cutOffDate)
            val selectionArgs = arrayOf(startDateStr, endDateStr)
            //selectionArgs.forEach (System.out::println)
            val cursor = db.query(
                CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,
                COLUMNS_CALC,
                " ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ? ",
                arrayOf(key.toString()),
                null,
                null,
                null
            )
            val items = mutableListOf<CreditCardBoughtDTO>()
            with(cursor) {
                while (moveToNext()) {
                    val record = CreditCardBoughtMap().mapping(this)
                    if (record.boughtDate.isBefore(cutOffDate) and record.boughtDate.isAfter(
                            startDate
                        )
                    ) {
                        items.add(record)
                    }
                }
            }

            return items.also { Log.v(this.javaClass.name,"${it.size}") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getToDate - End")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingQuotes(key:Int,cutoffCurrent: LocalDateTime): List<CreditCardBoughtDTO> {
        Log.v(this.javaClass.name,"<<<=== getPendingQuotes - Start")
        try{
        val db = dbConnect.readableDatabase
        val pendingQuotes = cutoffCurrent.minusMonths(1)
        val cursor = db.query(CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,COLUMNS_CALC," ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_MONTH} > ? and ${CreditCardBoughtDB.CreditCardBoughtEntry.COLUMN_CODE_CREDIT_CARD} = ? ",
            arrayOf(1.toString(),key.toString()),null,null,null)
        val items = mutableListOf<CreditCardBoughtDTO>()
        with(cursor){
            while(moveToNext()){
                val record = CreditCardBoughtMap().mapping(this)
                val difference = DateUtils.getMonths(record.boughtDate,cutoffCurrent)
                Log.d(this.javaClass.name," ${record.boughtDate} < $pendingQuotes -  $difference < ${record.month}")
                if(record.boughtDate.isBefore(pendingQuotes) &&
                    difference < record.month ) {
                    Log.d(this.javaClass.name,"Added")
                    items.add(record)
                }
            }
        }
        return items.also { Log.v(this.javaClass.name,"${it.size}") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getPendingQuotes - End")
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapital(key: Int, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getCapital - Start")
        try {
            val list = getToDate(key, cutOff)
            val value = list.stream().map {
                Log.println(Log.DEBUG,this.javaClass.name,"${it.month} ${it.valueItem}")
                if (it.month <= 1) it.valueItem else it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
            }.peek{
                Log.println(Log.DEBUG,this.javaClass.name,"$it")
            }.reduce { val1, val2 -> val1 + val2 }
            return value.also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getCapital - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterest(key: Int, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getInterest - Start")
        try{
        val list = getToDate(key,cutOff)
        val value = list.stream().filter{ it.month > 1}
                                 .map{
                                    Log.println(Log.DEBUG,this.javaClass.name,"${it.valueItem} X ${it.interest}%")
                                     it.valueItem.multiply(it.interest.toBigDecimal().divide(BigDecimal(100),8,RoundingMode.CEILING))
                                 }
            .peek{
                Log.println(Log.DEBUG,this.javaClass.name,"$it")
            }
                                 .reduce{ val1,val2 -> val1+val2}
        return value.also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getInterest - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCapitalPendingQuotes(key: Int, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getCapitalPendingQuotes - Start")
        try{
        val list = getPendingQuotes(key,cutOff)
        val value = list.stream().map {
            val months = DateUtils.getMonths(it.boughtDate,cutOff)
            val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
            val bought = quote.multiply(months.toBigDecimal())
            val capital = it.valueItem.minus(bought)
            if(capital > BigDecimal(0)){
                Log.println(Log.DEBUG,this.javaClass.name," ${it.valueItem}/${it.month} = $quote")
                 quote
            }else {
                BigDecimal(0)
            }
        }.peek{
            Log.println(Log.DEBUG,this.javaClass.name,"$it")
        }.reduce{val1,val2->val1.plus(val2)}
        return value.also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getCapitalPendingQuotes - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInterestPendingQuotes(key: Int, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getInterestPendingQuotes - Start")
        try{
        val list = getPendingQuotes(key,cutOff)
        val value = list.stream().map {
            val months = DateUtils.getMonths(it.boughtDate,cutOff)
            val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
            val bought = quote.multiply(months.toBigDecimal())
            val capital = it.valueItem.minus(bought)
            if(capital > BigDecimal(0)){
                Log.println(Log.DEBUG,this.javaClass.name,"$capital X ${it.interest}%")
                capital.multiply(it.interest.div(100).toBigDecimal())
            }else{
                BigDecimal(0)
            }
        }.peek{
            Log.println(Log.DEBUG,this.javaClass.name,"$it")
        }.reduce{val1,val2->val1.plus(val2)}
        return value.also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getInterestPendingQuotes - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBought(key: Int, cutOff: LocalDateTime): Optional<Long> {
        Log.v(this.javaClass.name,"<<<=== getBought - Start")
        try {
            val list = getToDate(key, cutOff)
            val value = list.stream().filter { it.month <= 1 }.count()
            return Optional.ofNullable(value).also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getBought - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBoughtPendingQuotes(key: Int, cutOff: LocalDateTime): Optional<Long> {
        Log.v(this.javaClass.name,"<<<=== getBoughtPendingQuotes - Start")
        try {
            val list = getPendingQuotes(key, cutOff)
            val value = list.stream().filter {
                val months = DateUtils.getMonths(it.boughtDate,cutOff)
                months <= it.month
            }.count()
            return Optional.ofNullable(value).also{Log.v(this.javaClass.name,"$it")}
        }finally{
            Log.v(this.javaClass.name,"<<<=== getBoughtPendingQuotes - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getBoughtQuotes(key: Int, cutOff: LocalDateTime): Optional<Long> {
        Log.v(this.javaClass.name,"<<<=== getBoughtQuotes - Start")
        try{
        val list = getToDate(key,cutOff)
        val value =  list.stream().filter{ it.month > 1 }.count()
       return Optional.ofNullable(value).also { Log.v(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getBoughtQuotes - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPay(key: Int, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getPendingToPay - Start")
        try{
            val list = getToDate(key,cutOff)
            val value = list.stream().map {
                val months = DateUtils.getMonths(it.boughtDate,cutOff)
                val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
                val bought = quote.multiply(months.toBigDecimal())
                val capital = it.valueItem.minus(bought)
                if(capital > BigDecimal(0) && it.month > 1){
                    capital
                }else{
                    BigDecimal(0)
                }
            }.peek{
                Log.println(Log.DEBUG,this.javaClass.name,"$it")
            }.reduce{val1,val2->val1.plus(val2)}
            return value.also { Log.d(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getPendingToPay - End")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPendingToPayQuotes(key: Int, cutOff: LocalDateTime): Optional<BigDecimal> {
        Log.v(this.javaClass.name,"<<<=== getPendingToPayQuotes - Start")
        try{
            val list = getPendingQuotes(key,cutOff)
            val value = list.stream().map {
                val months = DateUtils.getMonths(it.boughtDate,cutOff)
                val quote = it.valueItem.divide(it.month.toBigDecimal(),8,RoundingMode.CEILING)
                val bought = quote.multiply(months.toBigDecimal())
                Log.d(this.javaClass.name,"${it.valueItem} - $bought")
                val capital = it.valueItem.minus(bought)
                if(capital > BigDecimal(0)){
                    capital
                }else{
                    BigDecimal(0)
                }
            }.peek{
                Log.println(Log.DEBUG,this.javaClass.name,"$it")
            }.reduce{val1,val2->val1.plus(val2)}
            return value.also { Log.d(this.javaClass.name,"$it") }
        }finally{
            Log.v(this.javaClass.name,"<<<=== getPendingToPayQuotes - End")
        }
    }
}