package me.xuyuan.cx

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import me.xuyuan.cx.utils.CHomeStateHandler
import me.xuyuan.cx.utils.DamageTracker
import me.xuyuan.cx.visuals.TeleportParticles
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object CHomeCommand {
    fun init(){
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            dispatcher.register(

                CommandManager.literal("chome")
                    .then(
                        CommandManager.literal("home")
                            .executes(homeCommand)
                    )
                    .then(
                        CommandManager.literal("sethome")
                            .executes(sethomeCommand)
                    )
                    .executes(helpCommand)
            )
        }
    }


    private fun executeHome(context: CommandContext<ServerCommandSource>): Int {
        try {
            // Log
            val logger = LoggerFactory.getLogger("cx")
            logger.info("${context.source.name}: Teleported to their home")

            // Check damage
            val player = context.source.player!!
            val damageCooldown = DamageTracker.getRemainingCooldown(player!!)
            if (damageCooldown != null) {
                context.source.sendFeedback({
                    Text.literal("You last took damage ${String.format("%.1f", damageCooldown)} seconds ago. " +
                            "Please wait for ${DamageTracker.DAMAGECOOLDOWN} seconds to pass.")
                }, true)
                return 1
            }

            // Check for existence of home
            val home = CHomeStateHandler.getHome(player)
            if (home == null) {
                context.source.sendFeedback({
                    Text.literal("You have not set your home. Use /chome sethome at your preferred home")
                }, false)
                return 1
            }

            player.teleport(home.x, home.y, home.z, true)

            context.source.sendFeedback({
                Text.literal("Teleported to home")
            }, false)

        } catch (e: Exception) {
            context.source.sendFeedback({
                Text.literal("CX: An error occured: ${e.message}")
            }, true)
        }

        return 1
    }

    val homeCommand = Command(CHomeCommand::executeHome)

    private fun executeSethome(context: CommandContext<ServerCommandSource>): Int {
        try {
            // Log
            val logger = LoggerFactory.getLogger("cx")
            logger.info("${context.source.name}: Set their home")

            // Execute save
            val player = context.source.player!!
            val pos = player.pos
            CHomeStateHandler.saveHome(player, pos)

            // Spawn fancy particles
            TeleportParticles.spawn(player)

            context.source.sendFeedback({
                Text.literal("Set your home")
            }, false)

        } catch (e: Exception) {
            context.source.sendFeedback({
                Text.literal("CX: An error occured: ${e.message}")
            }, true)
        }

        return 1
    }

    val sethomeCommand = Command(CHomeCommand::executeSethome)

    private fun executeHelp(context: CommandContext<ServerCommandSource>): Int {
        context.source.sendFeedback({
            Text.literal("Usage: ${context.command} [home | sethome]")
        }, false)
        return 1
    }

    val helpCommand = Command(CHomeCommand::executeHelp)
}