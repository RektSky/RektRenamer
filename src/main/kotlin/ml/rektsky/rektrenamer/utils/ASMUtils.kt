package ml.rektsky.rektrenamer.utils

import me.fan87.regbex.PrimitiveType
import me.fan87.regbex.utils.MethodArgumentsTypeReader
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object ASMUtils {

    private val classes = HashMap<String, Class<*>>()

    @JvmStatic
    fun parseClass(data: ByteArray): ClassNode {
        val reader = ClassReader(data)
        val classNode = ClassNode()
        reader.accept(classNode, ClassReader.EXPAND_FRAMES)
        return classNode
    }


    @JvmStatic
    fun writeClass(node: ClassNode): ByteArray {
        val writer = PatchedClassWriter0(ClassWriter.COMPUTE_FRAMES)
        node.accept(writer)
        return writer.toByteArray()
    }
    @JvmStatic
    fun writeClassNoVerify(node: ClassNode): ByteArray {
        val writer = PatchedClassWriter(ClassWriter.COMPUTE_FRAMES)
        node.accept(writer)
        return writer.toByteArray()
    }

    @JvmStatic
    fun classToDescType(clazz: Class<*>): String {
        if (clazz.isArray) {
            return "[${classToDescType(clazz.componentType)}"
        }
        for (value in PrimitiveType.values()) {
            if (value.primitiveType == clazz) {
                return value.jvmName
            }
        }
        return "L" + clazz.name.replace(".", "/") + ";"
    }

    @JvmStatic
    inline fun <reified T> classToDescType(): String {
        return classToDescType(T::class.java)
    }

    @JvmStatic
    fun descTypeToJvmType(descType: String): String {
        if (!descType.startsWith("L") || !descType.endsWith(";")) {
            throw IllegalArgumentException("Desc type must be starting with L and ending with ;, but got $descType")
        }
        return descType.let { it.substring(1, it.length - 1) }
    }

    @JvmStatic
    fun generateMethodDesc(method: Method): String {
        return generateMethodDesc(
            classToDescType(method.returnType),
            *method.parameterTypes.map { classToDescType(it) }.toTypedArray()
        )
    }

    @JvmStatic
    fun generateMethodDesc(returnType: String, vararg argumentsTypes: String): String {
        return "(${argumentsTypes.joinToString("")})$returnType";
    }

    @JvmStatic
    fun generateMethodDesc(constructor: Constructor<*>): String {
        return generateMethodDesc("V", *constructor.parameterTypes.map { it.getDescName() }.toTypedArray())
    }

    @JvmStatic
    fun generateToObjectedPrimitiveType(type: PrimitiveType): MethodInsnNode {
        return generateMethodCall(type.objectType.methods.first { it.name == "valueOf" && it.parameterCount == 1 && it.parameterTypes[0] == type.primitiveType })
    }

    //<editor-fold desc="Method Call" defaultstate="collapsed">
    @JvmStatic
    fun generateMethodCall(method: Method): MethodInsnNode {
        if (Modifier.isStatic(method.modifiers)) {
            if (method.declaringClass.isInterface) {
                return MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    method.declaringClass.name.replace(".", "/"),
                    method.name,
                    generateMethodDesc(method),
                    true
                )
            } else {
                return MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    method.declaringClass.name.replace(".", "/"),
                    method.name,
                    generateMethodDesc(method)
                )
            }
        } else {
            if (method.declaringClass.isInterface) {
                return MethodInsnNode(
                    Opcodes.INVOKEVIRTUAL,
                    method.declaringClass.name.replace(".", "/"),
                    method.name,
                    generateMethodDesc(method),
                    true
                )
            } else {
                return MethodInsnNode(
                    Opcodes.INVOKEVIRTUAL,
                    method.declaringClass.name.replace(".", "/"),
                    method.name,
                    generateMethodDesc(method)
                )
            }
        }
    }


    @JvmStatic
    inline fun <reified T> generateGetInstance(): FieldInsnNode {
        return generateGetField(T::class.java.fields.first { it.name == "INSTANCE" })
    }
    //</editor-fold>

    //<editor-fold desc="Fields Set/Get" defaultstate="collapsed">
    @JvmStatic
    fun generateGetField(field: Field): FieldInsnNode {
        return FieldInsnNode(if (Modifier.isStatic(field.modifiers)) Opcodes.GETSTATIC else Opcodes.GETFIELD, field.declaringClass.getJvmTypeName(), field.name, field.type.getDescName())
    }

    @JvmStatic
    fun generatePutField(field: Field): FieldInsnNode {
        return FieldInsnNode(if (Modifier.isStatic(field.modifiers)) Opcodes.PUTSTATIC else Opcodes.PUTFIELD, field.declaringClass.getJvmTypeName(), field.name, field.type.getDescName())
    }

    //</editor-fold>

    @JvmStatic
    fun generateGetCompanion(type: Class<*>): FieldInsnNode {
        return generateGetField(type.getDeclaredField("Companion"))
    }
    @JvmStatic
    inline fun <reified T> generateGetCompanion(): FieldInsnNode {
        return generateGetCompanion(T::class.java)
    }

    @JvmStatic
    fun getReturnType(desc: String): String {
        return desc.split(")")[1]
    }

    @JvmStatic
    fun getParameterTypes(desc: String): Array<String> {
        val reader = MethodArgumentsTypeReader(desc)
        return reader.arguments.toTypedArray()
    }

    class PatchedClassWriter(flags: Int) : ClassWriter(flags) {

        override fun getCommonSuperClass(type1: String?, type2: String?): String {
            return "java/lang/Object"
        }
    }
    class PatchedClassWriter0(flags: Int) : ClassWriter(flags) {}


    fun Class<*>.getJvmTypeName(): String {
        return name.replace(".", "/")
    }
    fun Class<*>.getDescName(): String {
        return classToDescType(this)
    }
    fun MethodNode.getParameterTypes(): Array<String> {
        return getParameterTypes(desc)
    }
    fun InsnList.addMethodCall(method: Method) {
        add(generateMethodCall(method))
    }
    fun InsnList.addGetField(field: Field) {
        add(generateGetField(field))
    }
    fun InsnList.addPutField(field: Field) {
        add(generatePutField(field))
    }
    fun InsnList.addGetCompanion(type: Class<*>) {
        add(generateGetCompanion(type))
    }
    fun InsnList.addThis() {
        add(VarInsnNode(Opcodes.ALOAD, 0))
    }
    fun InsnList.insertInstructions(inclusiveStart: Int, instructions: Iterable<AbstractInsnNode>): InsnList {
        val out = InsnList()
        for (withIndex in this.withIndex()) {
            if (withIndex.index == inclusiveStart) {
                for (instruction in instructions) {
                    out.add(instruction)
                }
            }
            out.add(withIndex.value)
        }
        return out
    }
    fun InsnList.insertInstructions(inclusiveStart: Int, instructions: (InsnList) -> Unit): InsnList {
        val insnList = InsnList()
        instructions(insnList)
        return insertInstructions(inclusiveStart, insnList)
    }
    inline fun <reified T> InsnList.addGetCompanion() {
        add(generateGetCompanion<T>())
    }

    fun ClassNode.getMethod(name: String, desc: String): MethodNode {
        return this.methods.first { it.name == name && it.desc == desc }
    }
    fun ClassNode.getMethod(node: MethodInsnNode): MethodNode {
        return getMethod(node.name, node.desc)
    }
}