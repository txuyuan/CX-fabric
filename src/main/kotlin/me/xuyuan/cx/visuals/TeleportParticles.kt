package me.xuyuan.cx.visuals

import net.minecraft.server.network.ServerPlayerEntity

object TeleportParticles {
    fun spawn(player: ServerPlayerEntity) {
        // I got this line out of the player.teleport() function, in the LivingEntity class
        player.world.sendEntityStatus(player, 46.toByte())
    }
}