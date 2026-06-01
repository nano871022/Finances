package co.com.japl.finances.iports.observables

interface ISMSObservablePublicher {

    fun notify(number:String,message: String)

    fun getNumbers():List<String>
}