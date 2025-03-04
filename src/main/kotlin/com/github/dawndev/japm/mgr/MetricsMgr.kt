package com.github.dawndev.japm.mgr

import com.github.dawndev.japm.monitor.TimeMonitor

object MetricsMgr {
    fun init() {
        TimeMonitor.init()
    }
}