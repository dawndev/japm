package com.github.dawndev.japm.core

import com.github.dawndev.japm.mgr.ConfigMgr
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

/**
 * Apm transformer
 *
 * @author Espresso
 */
class ApmTransformer : ClassFileTransformer {

    /**
     * 该方法会在类被加载到 JVM 之前被调用，允许对类文件进行修改
     *
     * @param loader ClassLoader
     * @param className 类的名称
     * @param classBeingRedefined 正在被重新定义的类（如果有的话，否则为 null）
     * @param protectionDomain 类的保护域
     * @param classfileBuffer 原始类文件字节码
     * @return ByteArray? 返回修改后的类文件字节码。如果不需要修改，可以返回 null 或者原始的 classfileBuffer。
     */
    override fun transform(
        loader: ClassLoader?,
        className: String?,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain,
        classfileBuffer: ByteArray
    ): ByteArray? {
        if (loader == null || className == null)
            return null

        if (!loader.validateAPM()) {
            return null
        }

        if (!className.validateAPM()) {
            return null
        }

        val asm = ASMLoader.build {
            classLoader(loader)
            reader(classfileBuffer)
            writer()
            visitor()
        }
        return asm.toBytes()
    }
}

private fun ClassLoader.validateAPM(): Boolean {
    return !(this.javaClass.name == "sun.reflect.DelegatingClassLoader"
            || this.javaClass.name == "javax.management.remote.rmi")
}


private fun String.validateAPM(): Boolean {
    return !(this.indexOf("\$Proxy") != -1
            || this.startsWith("java")
            || this.startsWith("sun")
            || this.startsWith("com/sun")
            || this.startsWith("com/intellij")
            || this.startsWith("org/objectweb/asm")
            || this.startsWith(ConfigMgr.nowPackageName))
}