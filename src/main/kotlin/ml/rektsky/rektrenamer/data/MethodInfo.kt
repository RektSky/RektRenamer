package ml.rektsky.rektrenamer.data

import ml.rektsky.rektrenamer.utils.ASMUtils
import org.objectweb.asm.tree.MethodNode
import java.lang.reflect.Method

data class MethodInfo(val owner: String, val name: String, val desc: String) {

    constructor(method: Method) : this(method.declaringClass.name.replace(".", "/"), method.name, ASMUtils.generateMethodDesc(method)) {

    }

    fun matches(methodNode: MethodNode): Boolean {
        return methodNode.name == name && methodNode.desc == desc
    }

}