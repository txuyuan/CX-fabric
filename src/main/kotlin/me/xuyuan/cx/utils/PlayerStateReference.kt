package me.xuyuan.cx.utils

import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.PersistentState
import net.minecraft.registry.RegistryWrapper
import java.util.UUID

class PlayerStateReference : PersistentState() {
    // Map to store player-specific data
    private val playerData: MutableMap<UUID, Coordinates> = mutableMapOf()

    // Nested class to store coordinates for each player
    data class Coordinates(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0)

    // Update player coordinates
    fun setCoordinates(player: ServerPlayerEntity, x: Double, y: Double, z: Double) {
        playerData[player.uuid] = Coordinates(x, y, z)
        markDirty()  // Mark the state as dirty so it gets saved
    }

    // Get player coordinates
    fun getCoordinates(player: ServerPlayerEntity): Coordinates? {
        return playerData[player.uuid]
    }

    override fun writeNbt(nbt: NbtCompound, registries: RegistryWrapper.WrapperLookup): NbtCompound {
        val playerNbt = NbtCompound()
        playerData.forEach { (uuid, coords) ->
            val playerDataTag = NbtCompound()
            playerDataTag.putDouble("x", coords.x)
            playerDataTag.putDouble("y", coords.y)
            playerDataTag.putDouble("z", coords.z)
            playerNbt.put(uuid.toString(), playerDataTag)
        }
        nbt.put("playerData", playerNbt)
        return nbt
    }

    companion object {
        // Read from NBT to load the player's data
        fun readFromNbt(tag: NbtCompound): PlayerStateReference {
            val state = PlayerStateReference()
            val playerDataTag = tag.getCompound("playerData")
            playerDataTag.keys.forEach { key ->
                val playerData = playerDataTag.getCompound(key)
                val uuid = UUID.fromString(key)
                val coords = Coordinates(
                    x = playerData.getDouble("x"),
                    y = playerData.getDouble("y"),
                    z = playerData.getDouble("z")
                )
                state.playerData[uuid] = coords
            }
            return state
        }
    }
}
