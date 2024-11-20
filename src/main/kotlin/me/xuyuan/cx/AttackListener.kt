package me.xuyuan.cx

import me.xuyuan.cx.utils.DamageTracker
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import org.slf4j.LoggerFactory

object AttackListener {

    fun init() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(
            ServerLivingEntityEvents.AllowDamage { entity: LivingEntity, source: DamageSource, amount: Float ->
                // Log
                val logger = LoggerFactory.getLogger("cx")

                // Filter out for only players
                if (entity is ServerPlayerEntity) {
                    logger.info("Player damage. ${entity.name}")
                    DamageTracker.takeDamage(entity)
                }

                true
            }
        )
    }

}