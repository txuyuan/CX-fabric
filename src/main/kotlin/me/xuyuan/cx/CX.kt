package me.xuyuan.cx

import me.xuyuan.cx.commands.CHomeCommand
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object CX : ModInitializer {
    private val logger = LoggerFactory.getLogger("cx")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CHomeCommand.init()
		logger.info("Initialised CX")
	}
}