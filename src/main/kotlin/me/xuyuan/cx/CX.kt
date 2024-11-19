package me.xuyuan.cx

import me.xuyuan.cx.commands.CHomeCommand
import me.xuyuan.cx.utils.CHomeFileHandler
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object CX : ModInitializer {
    private val logger = LoggerFactory.getLogger("cx")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
		CHomeCommand.init()
	}
}