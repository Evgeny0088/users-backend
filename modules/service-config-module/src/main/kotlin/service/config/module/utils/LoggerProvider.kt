package service.config.module.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LoggerProvider {

    inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)

}