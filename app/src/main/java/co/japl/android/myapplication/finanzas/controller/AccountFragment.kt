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
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AccountImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAccountSvc
import co.japl.android.myapplication.finanzas.holders.AccountHolder
import co.japl.android.myapplication.finanzas.putParams.AccountListParams
import co.japl.android.myapplication.finanzas.putParams.AccountParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() , OnClickListener{
    private lateinit var holder: IHolder<AccountDTO>
    @Inject lateinit var accountSvc:IAccountSvc
    private var idAccount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_account, container, false)
        holder = AccountHolder(root)
        holder.setFields(this)
        holder.cleanField()
        load()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun load(){
        arguments?.let {
            idAccount = AccountListParams.download(it)
            Log.d(javaClass.name,"Id $idAccount")
            if(idAccount > 0){
                accountSvc.get(idAccount).ifPresent {
                    holder.loadFields(it)
                }
            }
        }
    }

    fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val response = accountSvc.save(dto)
            if(dto.id == 0 && response > 0){
                idAccount = response.toInt()
                Toast.makeText(view?.context,R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
                (holder as AccountHolder).list.visibility = View.VISIBLE
            } else if(dto.id > 0 && response > 0){
                idAccount = dto.id
                Toast.makeText(view?.context,R.string.toast_successful_update,Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(view?.context,R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_input_list_acc->AccountParams.newInstance(idAccount,findNavController())
            R.id.btn_save_acc->{save()}
        }
    }

}