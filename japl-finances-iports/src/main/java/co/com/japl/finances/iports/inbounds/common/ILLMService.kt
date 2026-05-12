package co.com.japl.finances.iports.inbounds.common

interface ILLMService {
    suspend fun getAiResponse(prompt: String): Result<String>
}
