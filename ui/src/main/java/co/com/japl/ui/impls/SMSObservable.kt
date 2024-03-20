package co.com.japl.ui.impls

import co.com.japl.ui.interfaces.ISMSObservablePublicher
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver

class SMSObservable : ISMSObservableSubscriber, ISMSObservablePublicher{
    private val observers = mutableListOf<ISMSObserver>()

    override fun addObserver(observer: ISMSObserver) {
        observers.add(observer)
    }

    override fun notify(number:String,message: String) {
        observers.filter{
            it.getNumber().takeIf { it.isNotEmpty() }?.any {  it == number}?:false
        }?.forEach { it.notify(number,message) }
    }

    override fun getNumbers(): List<String> {
        return observers.map { it.getNumber() }.flatten()?: emptyList()
    }
}