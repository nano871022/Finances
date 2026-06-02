package co.japl.android.finances.services.core

import android.util.Log
import co.com.japl.finances.iports.dtos.LLMConfigDTO
import co.com.japl.finances.iports.enums.LLMType
import co.com.japl.finances.iports.outbounds.ILLMOutboundPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class LLMOutboundAdapter @Inject constructor() : ILLMOutboundPort {

    override suspend fun getAiResponse(config: LLMConfigDTO, prompt: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = config.apiKey
        if (apiKey.isEmpty()) return@withContext Result.failure(Exception("API Key is empty"))

        when (config.type) {
            LLMType.GEMINI -> getGeminiResponse(config, prompt)
            LLMType.DEEPSEEK -> getDeepSeekResponse(config, prompt)
        }
    }

    override suspend fun getModels(config: LLMConfigDTO): Result<List<String>> = withContext(Dispatchers.IO) {
        val apiKey = config.apiKey
        if (apiKey.isEmpty()) return@withContext Result.failure(Exception("API Key is empty"))

        when (config.type) {
            LLMType.GEMINI -> getGeminiModels(config)
            LLMType.DEEPSEEK -> getDeepSeekModels(config)
        }
    }

    private fun getGeminiResponse(config: LLMConfigDTO, prompt: String): Result<String> {
        val model = config.model ?: "gemini-1.5-flash"
        val baseUrl = config.url ?: "https://generativelanguage.googleapis.com/v1beta/models/"

        return runCatching {
            val url = URL("${baseUrl.removeSuffix("/")}/$model:generateContent?key=${config.apiKey}")
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
                Log.d(this.javaClass.name,"AIReponse: $response")
                val jsonResponse = JSONObject(response)
                val candidates = jsonResponse.getJSONArray("candidates")
                val contentRes = candidates.getJSONObject(0).getJSONObject("content")
                val partsRes = contentRes.getJSONArray("parts")
                val text = partsRes.getJSONObject(0).getString("text")
                cleanResponse(text)
            } else {
                throw Exception("Error: ${connection.responseCode} ${connection.errorStream?.bufferedReader()?.readText()}")
            }
        }
    }

    private fun getGeminiModels(config: LLMConfigDTO): Result<List<String>> {
        val baseUrl = config.url ?: "https://generativelanguage.googleapis.com/v1beta/models/"
        return runCatching {
            val url = URL("${baseUrl.removeSuffix("/")}?key=${config.apiKey}")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                val models = jsonResponse.getJSONArray("models")
                val modelList = mutableListOf<String>()
                for (i in 0 until models.length()) {
                    val model = models.getJSONObject(i)
                    val name = model.getString("name").replace("models/", "")
                    modelList.add(name)
                }
                modelList
            } else {
                throw Exception("Error: ${connection.responseCode} ${connection.errorStream?.bufferedReader()?.readText()}")
            }
        }
    }

    private fun getDeepSeekResponse(config: LLMConfigDTO, prompt: String): Result<String> {
        val model = config.model ?: "deepseek-chat"
        val baseUrl = config.url ?: "https://api.deepseek.com/v1/"

        return runCatching {
            val url = URL("${baseUrl.removeSuffix("/")}/chat/completions")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer ${config.apiKey}")
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
                val content = messageObj.getString("content")
                cleanResponse(content)
            } else {
                throw Exception("Error: ${connection.responseCode} ${connection.errorStream?.bufferedReader()?.readText()}")
            }
        }
    }

    private fun getDeepSeekModels(config: LLMConfigDTO): Result<List<String>> {
        val baseUrl = config.url ?: "https://api.deepseek.com/v1/"
        return runCatching {
            val url = URL("${baseUrl.removeSuffix("/")}/models")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer ${config.apiKey}")

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

    internal fun cleanResponse(content: String): String {
        val thinkRegex = Regex("<think>.*?</think>", RegexOption.DOT_MATCHES_ALL)
        var cleaned = content.replace(thinkRegex, "").trim()

        if (cleaned.startsWith("{") && cleaned.endsWith("}")) {
            try {
                val json = JSONObject(cleaned)
                val keys = listOf("response", "answer", "content")
                val thinkingKeys = listOf("thinking", "thought", "reasoning")

                if (thinkingKeys.any { json.has(it) }) {
                    for (key in keys) {
                        if (json.has(key)) {
                            return json.getString(key).trim()
                        }
                    }
                }
            } catch (e: Exception) {
                // Not a JSON or doesn't match expected pattern
            }
        }

        return cleaned
    }
}
