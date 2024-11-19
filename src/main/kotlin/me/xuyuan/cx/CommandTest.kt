package me.xuyuan.cx

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object CommandTest {

    val testx = 25.0
    val testy = 77.0
    val testz = 13.0

    private fun executeHome(context: CommandContext<ServerCommandSource>): Int {
        val logger = LoggerFactory.getLogger("cx")
        logger.info("Invoked home command")

        context.source.sendFeedback({
            Text.literal("Called /chome home")
        }, false)

        val player = context.source.player!!
        val x = testx
        val y = testy
        val z = testz
        player.teleport(x, y, z, true)
        return 1
    }
    val homeCommand = Command(::executeHome)

//    private fun executeCommandWithArg(context: CommandContext<ServerCommandSource>): Int {
//        val logger = LoggerFactory.getLogger("cx")
//        logger.info("Invoked command with argument")
//
//        val value = StringArgumentType.getString(context, "arg1")
//        context.source.sendFeedback({
//            Text.literal("Called /chome with arg ${value}")
//        }, false)
//        return 1
//    }
//    val argsCommand = Command(::executeCommandWithArg)

    private fun executeDefault(context: CommandContext<ServerCommandSource>): Int {
        context.source.sendFeedback({
            Text.literal("Called /chome with no args")
        }, false)
        return 1
    }
    val defaultCommand = Command(::executeDefault)

    fun init(){
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            dispatcher.register(
                CommandManager.literal("chome")
//                    .then(
//                        argument<ServerCommandSource?, String?>("arg1", StringArgumentType.string())
//                            .executes(argsCommand)
//                    )
                    .then(
                        CommandManager.literal("home")
                            .executes(homeCommand)
                    )
                    .executes(defaultCommand)
            )
        }
    }
}