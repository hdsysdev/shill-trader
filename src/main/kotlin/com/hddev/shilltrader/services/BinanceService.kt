package com.hddev.shilltrader.service

import com.binance.connector.client.SpotClient
import com.binance.connector.client.impl.SpotClientImpl
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BinanceService {

    @Value("\${binance.api.key}")
    private lateinit var apiKey: String

    @Value("\${binance.api.secret}")
    private lateinit var apiSecret: String

    private lateinit var spotClient: SpotClient
    private val objectMapper = ObjectMapper()

    fun init() {
        spotClient = SpotClientImpl(apiKey, apiSecret)
    }

    fun createOrder(symbol: String, quantity: BigDecimal) {
        val symbolPair = "${symbol}USDT"
        val quoteQuantity = quantity.toPlainString()

        try {
            val params = LinkedHashMap<String, Any>()
            params["symbol"] = symbolPair
            params["side"] = "BUY"
            params["type"] = "MARKET"
            params["quoteOrderQty"] = quoteQuantity

            val response = spotClient.createTrade().newOrder(params)
            val responseJson = objectMapper.readTree(response)

            if (responseJson.has("orderId")) {
                println("Order created successfully for $symbol")
            } else {
                println("Failed to create order for $symbol. Response: $response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}