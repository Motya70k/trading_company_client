package com.example.trading_company_client.data.repository

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.trading_company_client.constants.URL
import com.example.trading_company_client.data.model.requests.LoginRequest
import com.example.trading_company_client.data.model.response.BaseResponse
import com.example.trading_company_client.domain.repository.LoginRequestRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

class LoginRequestRepositoryImpl : LoginRequestRepository {

    @OptIn(UnstableApi::class)
    override suspend fun employeeLogin(loginRequest: LoginRequest): BaseResponse? {
        return try {
            val client = HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
            val response: HttpResponse = client.post(URL.BASE_URL + "login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }

            val responseBody = response.bodyAsText()
            Log.d("LoginActivity", "Server response: $responseBody")
            val baseResponse = Json.decodeFromString(BaseResponse.serializer(), responseBody)
            val token = baseResponse.message
            Log.d("LoginActivity", "Deserialized token: $token")
            baseResponse.token = token
            baseResponse

        } catch (e: Exception) {
            null
        }
    }
}