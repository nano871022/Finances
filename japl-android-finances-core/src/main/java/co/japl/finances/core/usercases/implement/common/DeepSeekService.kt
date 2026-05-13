package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.inbounds.common.ILLMService
import co.com.japl.ui.Prefs
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import org.json.JSONObject
import org.json.JSONArray

class DeepSeekService @Inject constructor(private val prefs: Prefs) : ILLMService {
    override suspend fun getAiResponse(prompt: String): Result<String> {
        val apiKey = prefs.llmApiKey
        if (apiKey.isEmpty()) return Result.failure(Exception("API Key is empty"))
        val model = prefs.llmModel.ifEmpty { "deepseek-chat" }

        return runCatching {
            val url = URL("https://api.deepseek.com/v1/chat/completions")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.doOutput = true

            val jsonRequest = JSONObject()
            jsonRequest.put("model", model)
            val messages = JSONArray()
            val message = JSONObject()
            message.put("role", "user")
            message.put("content", prompt)
            messages.put(message)
            jsonRequest.put("messages", messages)

            connection.outputStream.use { it.write(jsonRequest.toString().toByteArray()) }

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                val choices = jsonResponse.getJSONArray("choices")
                val messageObj = choices.getJSONObject(0).getJSONObject("message")
                messageObj.getString("content")
            } else {
                throw Exception("Error: ${connection.responseCode} ${connection.errorStream?.bufferedReader()?.readText()}")
            }
        }
    }

    override suspend fun getModels(): Result<List<String>> {
        val apiKey = prefs.llmApiKey
        if (apiKey.isEmpty()) return Result.failure(Exception("API Key is empty"))

        return runCatching {
            val url = URL("https://api.deepseek.com/v1/models")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                val data = jsonResponse.getJSONArray("data")
                val modelList = mutableListOf<String>()
                for (i in 0 until data.length()) {
                    val model = data.getJSONObject(i)
                    modelList.add(model.getString("id"))
                }
                modelList
            } else {
                throw Exception("Error: ${connection.responseCode} ${connection.errorStream?.bufferedReader()?.readText()}")
            }
        }
    }
}
