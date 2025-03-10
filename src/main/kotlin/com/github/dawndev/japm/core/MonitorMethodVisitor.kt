package com.github.dawndev.japm.core

import com.github.dawndev.japm.const.FUNC_VOID_STRING
import com.github.dawndev.japm.const.INSTANCE
import com.github.dawndev.japm.const.METHOD_OVER
import com.github.dawndev.japm.const.METHOD_START
import com.github.dawndev.japm.monitor.TimeMonitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Monitor method visitor
 *
 * @param api
 * @param mv
 */
class MonitorMethodVisitor(
    api: Int,
    mv: MethodVisitor,
    private val key: String
) : MethodVisitor(api, mv) {

    /**
     * 方法开始时回调
     *
     */
    override fun visitCode() {
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            TimeMonitor.clazzName,
            INSTANCE,
            "L${TimeMonitor.clazzName};"
        )

        // LDC
        mv.visitLdcInsn(key)

        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            TimeMonitor.clazzName,
            METHOD_START,
            FUNC_VOID_STRING,
            false
        )
    }

    /**
     * Visit insn
     * <p>
     *     在每个 return 指令前插入 TimeMonitor.end
     *
     * @param opcode
     */
    override fun visitInsn(opcode: Int) {
        if (opcode in Opcodes.IRETURN..Opcodes.RETURN) {

            mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                TimeMonitor.clazzName,
                INSTANCE,
                "L${TimeMonitor.clazzName};"
            )

            // LDC
            mv.visitLdcInsn(key)

            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                TimeMonitor.clazzName,
                METHOD_OVER,
                FUNC_VOID_STRING,
                false
            )
        }
        super.visitInsn(opcode)
    }
}