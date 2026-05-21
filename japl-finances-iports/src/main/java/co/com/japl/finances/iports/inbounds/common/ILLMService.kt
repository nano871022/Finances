package co.com.japl.finances.iports.inbounds.common

interface ILLMService {
    suspend fun getAiResponse(prompt: String, model: String? = null): Result<String>
    suspend fun getModels(): Result<List<String>>
}
