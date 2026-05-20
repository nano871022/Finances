package co.japl.android.finances.services.implement

import android.content.Context
import android.util.Log
import co.com.japl.finances.iports.inbounds.common.IEmailRead
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.Base64
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import java.io.IOException
import javax.inject.Inject

class GmailReadImpl @Inject constructor(
    private val context: Context
) : IEmailRead {

    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val httpTransport = NetHttpTransport()

    override fun getEmails(sender: String, subject: String): List<String> {
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return emptyList()
        val credential =
            GoogleAccountCredential.usingOAuth2(context, listOf(GmailScopes.GMAIL_READONLY))
        credential.selectedAccount = account.account

        val service = Gmail.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName("Finanzas")
            .build()

        val query = "from:$sender"

        return try {
            val response = service.users().messages().list("me").setQ(query).execute()
            Log.d(this.javaClass.name,"Query $query Response: $response")
            val messages = response.messages ?: return emptyList()
            Log.d(this.javaClass.name,"Messages: $messages")

            messages.take(20).mapNotNull { message ->
                val msg =
                    service.users().messages().get("me", message.id).setFormat("full").execute()
                val msgSubject = msg.payload.headers.find { it.name == "Subject" }?.value ?: ""

                // Filtramos por asunto localmente si el query de Gmail no es suficiente
                if (msgSubject.contains(subject, ignoreCase = true) || subject.isEmpty()) {
                    getBody(msg)
                } else {
                    null
                }
            }
        }catch(e: UserRecoverableAuthIOException){
            Log.e(this.javaClass.name,"Error: ${e.message}")
            throw e
        }catch(e: IOException){
            e.printStackTrace()
            emptyList()
        }catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun getBody(message: Message): String {
        val payload = message.payload ?: return ""
        val parts = payload.parts
        
        if (parts == null || parts.isEmpty()) {
            return payload.body?.data?.let { String(Base64.decodeBase64(it)) } ?: ""
        }
        
        fun extract(parts: List<com.google.api.services.gmail.model.MessagePart>): String? {
            for (part in parts) {
                if (part.mimeType == "text/plain" && part.body?.data != null) {
                    return String(Base64.decodeBase64(part.body.data))
                }
                if (part.parts != null) {
                    val res = extract(part.parts)
                    if (res != null) return res
                }
            }
            return null
        }
        
        return extract(parts) ?: payload.body?.data?.let { String(Base64.decodeBase64(it)) } ?: ""
    }
}
