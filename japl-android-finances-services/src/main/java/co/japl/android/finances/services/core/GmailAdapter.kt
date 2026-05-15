package co.japl.android.finances.services.core

import android.content.Context
import android.util.Log
import co.com.japl.finances.iports.outbounds.IGmailPort
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GmailAdapter @Inject constructor(private val context: Context) : IGmailPort {

    override suspend fun loadEmails(sender: String): List<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(context) ?: return@withContext emptyList<Pair<String, String>>()
            val credential = GoogleAccountCredential.usingOAuth2(context, listOf(GmailScopes.GMAIL_READONLY))
            credential.selectedAccount = account.account

            val service = Gmail.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("Japl Finances")
                .build()

            val query = "from:$sender"
            val response = service.users().messages().list("me").setQ(query).execute()
            val messages = response.messages ?: return@withContext emptyList<Pair<String, String>>()

            messages.mapNotNull { message ->
                try {
                    val fullMessage = service.users().messages().get("me", message.id).execute()
                    val subject = fullMessage.payload.headers.find { it.name == "Subject" }?.value ?: ""

                    val body = if (fullMessage.payload.parts != null) {
                        fullMessage.payload.parts.firstOrNull { it.mimeType == "text/plain" }?.body?.data
                            ?: fullMessage.payload.parts.firstOrNull { it.mimeType == "text/html" }?.body?.data
                            ?: fullMessage.snippet
                    } else {
                        fullMessage.payload.body?.data ?: fullMessage.snippet
                    }

                    val decodedBody = body?.let {
                        String(com.google.api.client.util.Base64.decodeBase64(it))
                    } ?: fullMessage.snippet

                    Pair(subject, decodedBody)
                } catch (e: Exception) {
                    Log.e("GmailAdapter", "Error fetching message details for ${message.id}", e)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("GmailAdapter", "Error loading emails for $sender", e)
            emptyList<Pair<String, String>>()
        }
    }
}
