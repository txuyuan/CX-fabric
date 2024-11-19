package me.xuyuan.cx.utils

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import org.slf4j.LoggerFactory

object CHomeFileHandler {
    fun saveHome(player: Entity, location: Vec3d): Int {
        val logger = LoggerFactory.getLogger("cx")
        logger.info("Saving home")

        return 1
    }

    fun getHome(player: Entity): Vec3d {
        val logger = LoggerFactory.getLogger("cx")
        logger.info("Getting home")

        // Test returns
        val testx = 25.0
        val testy = 77.0
        val testz = 13.0
        return Vec3d(testx, testy, testz)
    }
}