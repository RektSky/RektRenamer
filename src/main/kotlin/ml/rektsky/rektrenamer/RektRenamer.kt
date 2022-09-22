package ml.rektsky.rektrenamer

import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode

class RektRenamer(val renamerMapping: RenamerMapping): Remapper() {

    override fun mapMethodName(owner: String, name: String, descriptor: String): String {
        return renamerMapping.getRemappedMethodName(MethodInfo(owner, name, descriptor))?:name
    }


    override fun mapFieldName(owner: String, name: String, descriptor: String?): String {
        return renamerMapping.getRemappedFieldName(FieldInfo(owner, name))?:name
    }

    override fun map(internalName: String): String {
        return renamerMapping.getRemappedClassName(internalName)?:internalName
    }

}

typealias ClassRepo = MutableMap<String, ClassNode>