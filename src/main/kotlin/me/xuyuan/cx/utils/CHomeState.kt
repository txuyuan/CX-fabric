package me.xuyuan.cx.utils

import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.*
import java.util.function.Supplier

class CHomeState: PersistentState() {
    // Store as map of player UUIDs and Vec3d instances
    private val playerData: MutableMap<UUID,Vec3d> = mutableMapOf()

    // Map from state to NBT
    override fun writeNbt(nbt: NbtCompound, registries: RegistryWrapper.WrapperLookup): NbtCompound {
        val playerNbt = NbtCompound()
        for ((uuid, location) in playerData) {
            val playerTag = NbtCompound()
            playerTag.putDouble("x", location.x)
            playerTag.putDouble("y", location.y)
            playerTag.putDouble("z", location.z)
            playerNbt.put(uuid.toString(), playerTag)
        }
        nbt.put("chome_homes", playerNbt)
        return nbt
    }

    // Map from NBT back to state
    companion object {
        fun createFromNbt(tag: NbtCompound, registryLookup: WrapperLookup?): CHomeState {
            // Init object reference
            val state = CHomeState()

            // Grab NBT data
            val playerNbt = tag.getCompound("chome_homes")

            // Map data
            for (key in playerNbt.keys) {
                val uuid = UUID.fromString(key)
                val locationTag = playerNbt.getCompound(key)
                val location = Vec3d(
                    locationTag.getDouble("x"),
                    locationTag.getDouble("y"),
                    locationTag.getDouble("z")
                )
                state.playerData[uuid] = location
            }

            return state
        }

        private val type: Type<CHomeState> = Type<CHomeState>(
            Supplier<CHomeState> { CHomeState() },  // If there's no 'StateSaverAndLoader' yet create one
            CHomeState::createFromNbt,  // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
        )

        fun getCHomeState(server: MinecraftServer): CHomeState {
            val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
            val state = persistentStateManager.getOrCreate(type, "cx")
            state.markDirty()
            return state
        }
    }

    fun getHome(player: ServerPlayerEntity): Vec3d? {
        return playerData[player.uuid]
    }

    fun setHome(player: ServerPlayerEntity, location: Vec3d) {
        playerData[player.uuid] = location
        markDirty()
    }
}