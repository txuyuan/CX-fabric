package me.xuyuan.cx.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.core.config.ConfigurationException
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.math.log

object ConfigHandler {

    @Serializable
    data class Config(
        val chome: CHome
    )

    @Serializable
    data class CHome(
        val enabled: Boolean,
        val cooldown: Int
    )

    var CONFIG:Config = Config(
        chome = CHome(
            enabled = true,
            cooldown = 20
        )
    )

    private fun createDefaultFile(configFile: File){
        // Take unaltered default value of CONFIG, and write to file
        val configJson = Json{ prettyPrint = true }.encodeToString(CONFIG)
        configFile.writeText(configJson)

        val logger = LoggerFactory.getLogger("cx")
        logger.info("Created defaulted config file")
    }

    fun load(){
        val logger = LoggerFactory.getLogger("cx")

        // Load file
        val configFolder = File("config/CX")
        if (!configFolder.exists())
            configFolder.mkdirs()
        val configFile = File("config/CX/config.json")
        if (!configFile.exists())
            createDefaultFile(configFile)
        if (!configFile.canRead()) {
            logger.error("Cannot read config file!!")
            throw ConfigurationException("Cannot read config file! No permission or otherwise unable")
        }

        // Read file
        val configText = configFile.readText()
        val config = Json.decodeFromString<Config>(configText)
        CONFIG = config

        logger.info("Loaded config successfully")
    }
}