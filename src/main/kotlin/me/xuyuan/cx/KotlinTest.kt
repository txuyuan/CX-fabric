package me.xuyuan.cx

import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.slf4j.LoggerFactory

object KotlinTest {
    fun init(){
            AttackBlockCallback.EVENT.register(AttackBlockCallback { player: PlayerEntity, world: World?, hand: Hand?, pos: BlockPos, direction: Direction? ->
                val logger = LoggerFactory.getLogger("cx")
                logger.info("Attack block event WITH HOT RELOAD: " + player.name + " at location " + pos.toShortString())
                ActionResult.PASS
            })
    }
}