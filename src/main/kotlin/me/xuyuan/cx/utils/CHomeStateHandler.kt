package me.xuyuan.cx.utils

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

object CHomeStateHandler {

    fun saveHome(player: ServerPlayerEntity, location: Vec3d) {
        val state = CHomeState.getCHomeState(player.world.server!!)
        state.setHome(player, location)
    }

    fun getHome(player: ServerPlayerEntity): Vec3d? {
        val state = CHomeState.getCHomeState(player.world.server!!)
        return state.getHome(player)
    }
}