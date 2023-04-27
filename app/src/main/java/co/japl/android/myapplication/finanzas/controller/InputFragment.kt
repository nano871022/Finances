package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.InputImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.holders.InputHolder
import co.japl.android.myapplication.finanzas.putParams.InputListParams
import java.math.BigDecimal
import java.time.LocalDate

class InputFragment : Fragment(),OnClickListener {

    private lateinit var holder:IHolder<InputDTO>
    private lateinit var service:SaveSvc<InputDTO>
    private var accountCode:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_input, container, false)
        accountCode = arguments?.let { InputListParams.download(it) }?:0
        holder = InputHolder(root,parentFragmentManager)
        service = InputImpl(root,ConnectDB( root.context))
        holder.setFields(this)
        holder.loadFields(getInput(root))
        holder.cleanField()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInput(view:View):InputDTO{
        val id = 0
        val date = LocalDate.MIN
        val accountCode = accountCode
        val kindof = view.resources.getStringArray(R.array.Months)[0]
        val name = ""
        val value = BigDecimal.ZERO
        val start = LocalDate.now()
        val end = LocalDate.of(9999,12,31)
        return InputDTO(id,date,accountCode,kindof,name,value,start,end)
    }

    private fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val saved = service.save(dto)
            if(saved > 0 && dto.id > 0){
                Toast.makeText(context,R.string.toast_successful_update,Toast.LENGTH_LONG).show()
                InputListParams.toBack(findNavController())
            }else if(saved > 0 && dto.id == 0){
                Toast.makeText(context,R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
                InputListParams.toBack(findNavController())
            }else{
                Toast.makeText(context,R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_save_in->save()
            R.id.btn_cancel_in->InputListParams.toBack(findNavController())
        }
    }

}