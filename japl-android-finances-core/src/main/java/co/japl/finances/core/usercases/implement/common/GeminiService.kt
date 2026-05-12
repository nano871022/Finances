package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.ui.Prefs
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import org.json.JSONObject
import org.json.JSONArray

class GeminiService @Inject constructor(private val prefs: Prefs) : ILLMService {
    override suspend fun getAiResponse(prompt: String): Result<String> {
        val apiKey = prefs.llmApiKey
        if (apiKey.isEmpty()) return Result.failure(Exception("API Key is empty"))
        val model = "gemini-2.5-flash-lite"

        return runCatching {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val parts = JSONArray().apply {
                put(JSONObject().apply {
                    put("text", prompt)
                })
            }
            val content = JSONObject().apply {
                put("parts", parts)
            }
            val contents = JSONArray().apply {
                put(content)
            }
            val requestBody = JSONObject().apply {
                put("contents", contents)
            }.toString()

            connection.outputStream.use { it.write(requestBody.toByteArray()) }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                val candidates = jsonResponse.getJSONArray("candidates")
                val contentRes = candidates.getJSONObject(0).getJSONObject("content")
                val partsRes = contentRes.getJSONArray("parts")
                partsRes.getJSONObject(0).getString("text")
            } else {
                throw Exception("Error: ${connection.responseCode} ${connection.errorStream?.bufferedReader()?.readText()}")
            }
        }
    }
}
