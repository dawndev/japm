package com.github.dawndev.japm.core

import com.github.dawndev.japm.mgr.ConfigMgr
import org.objectweb.asm.*
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.HashSet

class MonitorClassVisitor(cw: ClassWriter) : ClassVisitor(Opcodes.ASM9, cw) {

    private lateinit var className: String
    private var isInterface: Boolean = false
    private val fieldNameList: ArrayList<String> = ArrayList()
    private var superName: String? = null
    private var interfaces: HashSet<String> = HashSet()

    /**
     * 访问类时
     *
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        isInterface = access and Opcodes.ACC_INTERFACE != 0
        this.superName = superName
        this.interfaces.clear()
        if (interfaces != null) {
            for (interfaceName in interfaces) {
                this.interfaces.add(interfaceName)
            }
        }
    }

    /**
     * 访问成员变量时
     *
     * <p>
     *     将field构建下setter和getter避免注入方法
     *
     * @param access
     * @param name
     * @param descriptor
     * @param signature
     * @param value
     * @return
     */
    override fun visitField(access: Int, name: String, descriptor: String?, signature: String?, value: Any?): FieldVisitor {
        val upFieldName = name.substring(0, 1).toUpperCase() + name.substring(1)
        fieldNameList.add("get$upFieldName")
        fieldNameList.add("set$upFieldName")
        fieldNameList.add("is$upFieldName")
        return super.visitField(access, name, descriptor, signature, value)
    }

    /**
     * 访问到方法时
     *
     * <p>
     *     接口中的方法忽略
     *     私有方法、抽象方法、Object的方法、构造方法..
     *
     *
     * @param access
     * @param name
     * @param descriptor
     * @param signature
     * @param exceptions
     * @return
     */
    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {

        if (isInterface || !canInject(access, name)) {
            return super.visitMethod(access, name, descriptor, signature, exceptions)
        }

        if (!ConfigMgr.validateBase(this.superName))
            return super.visitMethod(access, name, descriptor, signature, exceptions)

        if (!ConfigMgr.validateInterface(this.interfaces))
            return super.visitMethod(access, name, descriptor, signature, exceptions)

        totals++
        return MonitorMethodVisitor(
            api,
            cv.visitMethod(access, name, descriptor, signature, exceptions),
            "${className}::${name}"
        )
    }

    /**
     * 是否可以注入
     *
     * @param access
     * @param name
     * @return
     */
    private fun canInject(access: Int, name: String): Boolean {
        if (name.isSpecialMethodName()) {
            return false
        }

        // 不对私有方法进行注入
        if (access and Opcodes.ACC_PRIVATE != 0) {
            return false
        }

        // 不对抽象方法、native方法、桥接方法、合成方法进行注入
        if (access and Opcodes.ACC_ABSTRACT != 0 || access and Opcodes.ACC_NATIVE != 0 || access and Opcodes.ACC_BRIDGE != 0 || access and Opcodes.ACC_SYNTHETIC != 0) {
            return false
        }

        if ("<init>" == name || "<clinit>" == name) {
            return false
        }

        if (name == "main" || name == "premain"
            || name == "getClass" || name == "hashCode"
            || name == "equals"|| name == "clone"|| name == "toString")
            return false

        return !fieldNameList.contains(name)
    }

    /**
     * Is special method name
     *
     * @return
     */
    private fun String.isSpecialMethodName(): Boolean {
        val symbolIndex = this.indexOf('$')
        if (symbolIndex < 0) {
            return false
        }
        val leftParenIndex = this.indexOf('(')
        return leftParenIndex < 0 || symbolIndex < leftParenIndex
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MonitorClassVisitor::class.java)
        var totals = 0
    }
}
