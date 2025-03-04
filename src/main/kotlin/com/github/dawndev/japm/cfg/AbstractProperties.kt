package com.github.dawndev.japm.cfg

import com.github.dawndev.japm.internal.PropertiesDelegate


abstract class AbstractProperties(path: String, prefix: String) {
    protected val prop = PropertiesDelegate(path, prefix)
}