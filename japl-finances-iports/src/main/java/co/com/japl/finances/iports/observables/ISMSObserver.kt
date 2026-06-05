package co.com.japl.finances.iports.observables

interface ISMSObserver {

    fun notify(phoneNumber:String,message: String)

    /*
    * Number of the phone to check if it receive a sms
    * */
    fun getNumber():List<String>
}