package com.github.dawndev.japm

import com.github.dawndev.japm.core.ApmTransformer
import com.github.dawndev.japm.mgr.ConfigMgr
import com.github.dawndev.japm.const.BANNER
import com.github.dawndev.japm.const.StateEnum
import com.github.dawndev.japm.internal.FileUtils
import com.github.dawndev.japm.mgr.MetricsMgr
import com.github.dawndev.japm.mgr.SystemMgr
import org.slf4j.LoggerFactory
import java.lang.instrument.Instrumentation

/**
 * Application
 *
 * @author jdg
 */
object Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)
    var state: StateEnum = StateEnum.Prepare

    fun init() {
        // 当前的包名存一下
        ConfigMgr.init()
        MetricsMgr.init()
        SystemMgr.init()
        state = StateEnum.Init
    }

    fun start(inst: Instrumentation) {
        state = StateEnum.Start
        logger.info(FileUtils.openText(this.javaClass.classLoader.getResource(BANNER)?.path ?: ""))
        val transformer = ApmTransformer()
        inst.addTransformer(transformer)
        state = StateEnum.Running
    }

    fun exit() {
        state = StateEnum.Exit
        logger.error("JAPM发生异常导致了退出")
    }
}

fun main() {
    Application.init()
}