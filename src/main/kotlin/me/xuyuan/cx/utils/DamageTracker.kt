package me.xuyuan.cx.utils

import net.minecraft.server.network.ServerPlayerEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

object DamageTracker {
    // Main record store, kept in memory
    val damageEvents: MutableMap<UUID, LocalDateTime> = mutableMapOf()

    // Cleanup is invoked every time a new damage event occurs. That is enough to keep it from building in memory
    private fun cleanHistory() {
        val now = LocalDateTime.now()
        val zoneId = ZoneId.of("Europe/Tallinn")
        val nowEpoch = now.atZone(zoneId).toInstant().toEpochMilli()

        // Check every item in the record
        for ((uuid, datetime) in damageEvents) {
            val recordEpoch = datetime.atZone(zoneId).toInstant().toEpochMilli()
            val diff = (nowEpoch - recordEpoch).toDouble() / 1000

            if (diff > ConfigHandler.CONFIG.chome.cooldown){
                damageEvents.remove(uuid)
            }
        }
    }
    fun takeDamage(player: ServerPlayerEntity) {
        val now = LocalDateTime.now()
        damageEvents[player.uuid] = now
        cleanHistory()
    }

    fun getRemainingCooldown(player: ServerPlayerEntity): Double? {
        val now = LocalDateTime.now()
        val lastDamagedTime = damageEvents.get(player.uuid)

        if (lastDamagedTime != null) {
            // Calculate difference in time
            val zoneId = ZoneId.of("Europe/Tallinn")
            val nowEpoch = now.atZone(zoneId).toInstant().toEpochMilli()
            val lastDamagedEpoch = lastDamagedTime.atZone(zoneId).toInstant().toEpochMilli()
            val diff = (nowEpoch - lastDamagedEpoch).toDouble() / 1000

            // Calculate cooldown
            if (diff < ConfigHandler.CONFIG.chome.cooldown){
                return diff
            }
        }

        // Default value
        return null
    }
}