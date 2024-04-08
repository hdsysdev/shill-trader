package com.hddev.shilltrader.decisions

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TradingDecisionMaker {

    companion object {
        private const val BASE_INVESTMENT_AMOUNT = 100.0
    }

    fun findMostShilledCoin(coinMentions: Map<String, Int>): String? {
        return coinMentions.maxByOrNull { it.value }?.key
    }

    fun calculateInvestmentAmount(shillCount: Int): BigDecimal {
        val weightedAmount = BASE_INVESTMENT_AMOUNT * (shillCount - 10 + 1)
        return BigDecimal.valueOf(weightedAmount)
    }
}
