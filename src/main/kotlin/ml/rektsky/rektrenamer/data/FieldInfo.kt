package ml.rektsky.rektrenamer.data

import org.objectweb.asm.tree.FieldNode
import java.lang.reflect.Field

data class FieldInfo(val owner: String, val name: String) {
    constructor(field: Field) : this(field.declaringClass.name.replace(".", "/"), field.name) {

    }

    fun matches(methodNode: FieldNode): Boolean {
        return methodNode.name == name
    }

}