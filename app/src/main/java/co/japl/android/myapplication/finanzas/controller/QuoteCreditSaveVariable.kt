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
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CalcMap
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICalcSvc
import co.japl.android.myapplication.finanzas.holders.QuoteCreditSaveVariableHolder
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.putParams.QuoteCreditVariablesParams
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuoteCreditSaveVariable :  Fragment(), View.OnClickListener{

    private val mapping = CalcMap()
    private lateinit var holder: IHolder<QuoteCreditCard>

    @Inject lateinit var saveSvc: ICalcSvc

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.quote_credit_save_variable,container,false)
        holder = QuoteCreditSaveVariableHolder(view)
        holder.setFields(this)
        loadValues()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadValues(){
        arguments?.let{
            val quote = QuoteCreditVariablesParams.download(it)
            holder.loadFields(quote.get())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnSave->{
                val values = holder.downLoadFields()
                if(holder.validate() && saveSvc.save(mapping.mapping(values))>0){
                    Snackbar.make(view,R.string.success_save_quote_credit,Snackbar.LENGTH_LONG).setAction(R.string.close,this).show()
                    QuoteCreditVariablesParams.toBack(findNavController())
                }else{
                    Snackbar.make(view,R.string.dont_able_to_save,Snackbar.LENGTH_LONG).setAction(R.string.close,this).show()
                }
            }
            R.id.btnCancel->QuoteCreditVariablesParams.toBack(findNavController())

        }

    }

}