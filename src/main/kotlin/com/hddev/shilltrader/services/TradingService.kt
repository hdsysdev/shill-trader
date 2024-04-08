package com.hddev.shilltrader.services

import com.hddev.shilltrader.decisions.TradingDecisionMaker
import com.hddev.shilltrader.service.BinanceService
import com.hddev.shilltrader.service.FourchanService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class TradingService(
    private val fourchanService: FourchanService,
    private val binanceService: BinanceService,
    private val tradingDecisionMaker: TradingDecisionMaker
) {

    companion object {
        private const val SHILL_THRESHOLD = 10
    }

    @PostConstruct
    fun startMonitoring() {
        while (true) {
            try {
                val catalogJson = fourchanService.fetchCatalog()
                val coinMentions = fourchanService.countCoinMentions(catalogJson)
                val mostShilledCoin = tradingDecisionMaker.findMostShilledCoin(coinMentions)

                if (mostShilledCoin != null && coinMentions[mostShilledCoin]!! >= SHILL_THRESHOLD) {
                    val investmentAmount = tradingDecisionMaker.calculateInvestmentAmount(coinMentions[mostShilledCoin]!!)
                    binanceService.createOrder(mostShilledCoin, investmentAmount)
                }

                Thread.sleep(60000) // Wait for 1 minute (adjust as needed)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
