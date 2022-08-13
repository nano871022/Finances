package co.japl.android.myapplication.controller

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.impl.SaveImpl
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CalcMap
import co.japl.android.myapplication.finanzas.holders.QuoteCreditdCardSaveHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.putParams.QuoteCreditParams
import com.google.android.material.snackbar.Snackbar

class QuoteCreditSave :  Fragment(), View.OnClickListener{
    private lateinit var saveSvc: SaveSvc<CalcDTO>
    private lateinit var holder:IHolder<QuoteCreditCard>
    private val mapping = CalcMap()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.quote_credit_save, container, false)
        holder = QuoteCreditdCardSaveHolder(rootView)
        saveSvc = SaveImpl(ConnectDB(rootView.context))
        holder.setFields(this)
        loadValues()
        return rootView

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadValues(){
        arguments?.let {
            val quote = QuoteCreditParams.download(it)
            holder.loadFields(quote.get())
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun save(view:View){
        if(holder.validate()  ) {
            if (saveSvc.save(mapping.mapping(holder.downLoadFields()))) {
                Snackbar.make(view, R.string.success_save_quote_credit, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close, View.OnClickListener {
                    }).show()
                QuoteCreditParams.toBack(findNavController())
            } else {
                Snackbar.make(view, R.string.dont_able_to_save, Snackbar.LENGTH_LONG)
                    .setAction(R.string.close, this).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnSave->save(view)
            R.id.btnCancel->QuoteCreditParams.toBack(findNavController())

        }

    }

}