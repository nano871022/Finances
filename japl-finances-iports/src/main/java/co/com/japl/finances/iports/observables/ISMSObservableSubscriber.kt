package co.com.japl.finances.iports.observables

interface ISMSObservableSubscriber {

    fun addObserver(observer: ISMSObserver)
}