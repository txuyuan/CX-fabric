package me.xuyuan.cx.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import me.xuyuan.cx.utils.CHomeStateHandler
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

            // Execute teleport
            val player = context.source.player!!
            val home = CHomeStateHandler.getHome(player)

            // Check for existence of home
            if (home != null) {
                player.teleport(home.x, home.y, home.z, true)

                // Return feedback
                context.source.sendFeedback({
                    Text.literal("Teleported to home")
                }, false)
            } else {
                context.source.sendFeedback({
                    Text.literal("You have not set your home. Use /chome sethome at your preferred home")
                }, false)
            }

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

            // Spawn particles
            TeleportParticles.spawn(player)

            // Return feedback
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