package co.com.japl.finances.iports.outbounds

interface IGmailPort {
    suspend fun loadEmails(sender: String): List<Pair<String, String>>
}
