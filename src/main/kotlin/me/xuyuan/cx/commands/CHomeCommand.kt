package me.xuyuan.cx.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import me.xuyuan.cx.utils.CHomeFileHandler
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
                    .executes(defaultCommand)
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
            val homepos = CHomeFileHandler.getHome(player)

            player.teleport(homepos.x, homepos.y, homepos.z, true)

            // Return feedback
            context.source.sendFeedback({
                Text.literal("Called /chome home")
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
            CHomeFileHandler.saveHome(player, pos)

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

    private fun executeDefault(context: CommandContext<ServerCommandSource>): Int {
        context.source.sendFeedback({
            Text.literal("Called /chome with no args")
        }, false)
        return 1
    }

    val defaultCommand = Command(CHomeCommand::executeDefault)
}