package me.xuyuan.cx

import me.xuyuan.cx.utils.DamageTracker
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity

object AttackListener {

    fun init() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(
            ServerLivingEntityEvents.AllowDamage { entity: LivingEntity, source: DamageSource, amount: Float ->
                // Filter out for only players
                if (entity is ServerPlayerEntity) {
                    DamageTracker.takeDamage(entity)
                }
                true
            }
        )
    }

}