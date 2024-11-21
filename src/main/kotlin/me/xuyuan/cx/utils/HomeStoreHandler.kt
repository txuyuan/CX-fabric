package me.xuyuan.cx.utils

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import org.slf4j.LoggerFactory
import kotlin.math.log

object HomeStoreHandler {

    fun saveHome(player: ServerPlayerEntity, location: Vec3d) {
        val state = HomeStore.getCHomeState(player.world.server!!)
        state.setHome(player, location)

        if (ConfigHandler.CONFIG.debug) {
            val logger = LoggerFactory.getLogger("cx")
            logger.info("Fetched home store: ${state.toString()}")
            logger.info("Set home for ${player.name}")
        }
    }

    fun getHome(player: ServerPlayerEntity): Vec3d? {
        val state = HomeStore.getCHomeState(player.world.server!!)
        val home = state.getHome(player)

        if (ConfigHandler.CONFIG.debug) {
            val logger = LoggerFactory.getLogger("cx")
            logger.info("Fetched home store: ${state.toString()}")
            logger.info("Fetched home for ${player.name}: ${home}")
        }

        return home
    }
}