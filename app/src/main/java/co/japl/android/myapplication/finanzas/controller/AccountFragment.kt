package co.japl.android.myapplication.finanzas.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.holders.AccountHolder

class AccountFragment : Fragment() , OnClickListener{
    private lateinit var holder:IHolder<AccountDTO>
    private lateinit var accountSvc:SaveSvc<AccountDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_account, container, false)
        accountSvc = AccountImpl(ConnectDB(root.context))
        holder = AccountHolder(root)
        holder.setFields(this)
        holder.cleanField()
        return root
    }

    fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val response = accountSvc.save(dto)
            if(dto.id == 0 && response > 0){
                Toast.makeText(view?.context,R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
                (holder as AccountHolder).list.visibility = View.VISIBLE
            } else if(dto.id > 0 && response > 0){
                Toast.makeText(view?.context,R.string.toast_successful_update,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(view?.context,R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_input_list_acc->{}
            R.id.btn_save_acc->{save()}
        }
    }

}