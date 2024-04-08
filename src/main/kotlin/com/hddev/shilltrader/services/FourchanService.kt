package com.hddev.shilltrader.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service

@Service
class FourchanService {

    companion object {
        private const val FOURCHAN_API_URL = "https://a.4cdn.org/biz/catalog.json"
    }

    fun fetchCatalog(): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(FOURCHAN_API_URL)
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    fun countCoinMentions(catalogJson: String): Map<String, Int> {
        val mapper = jacksonObjectMapper()
        val catalog: List<Map<String, Any>> = mapper.readValue(catalogJson)

        val coinMentions = mutableMapOf<String, Int>()

        catalog.forEach { thread ->
            val sub = thread["sub"] as? String
            sub?.let { subject ->
                val coins = extractCoins(subject)
                coins.forEach { coin ->
                    coinMentions[coin] = coinMentions.getOrDefault(coin, 0) + 1
                }
            }
        }

        return coinMentions
    }

    private fun extractCoins(subject: String): List<String> {
        val coinRegex = Regex("\\b[A-Z]{2,}\\b")
        return coinRegex.findAll(subject).map { it.value }.toList()
    }
}