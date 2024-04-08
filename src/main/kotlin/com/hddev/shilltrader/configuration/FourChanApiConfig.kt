package com.hddev.shilltrader.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FourChanConfig {
    @Value("fourchan.api.baseUrl")
    lateinit var baseUrl: String

    @Value("fourchan.api.bizEndpoint")
    lateinit var bizEndpoint: String
}