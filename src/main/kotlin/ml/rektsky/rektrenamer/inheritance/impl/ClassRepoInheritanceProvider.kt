package ml.rektsky.rektrenamer.inheritance.impl

import ml.rektsky.rektrenamer.ClassRepo
import ml.rektsky.rektrenamer.inheritance.InheritanceProvider

class ClassRepoInheritanceProvider(val classRepo: ClassRepo): InheritanceProvider {

    override fun getParents(className: String): Array<String>? {
        val classNode = classRepo[className]
        if (classNode == null) {
            return null
        }
        val output = ArrayList<String>()
        if (classNode.superName != null) {
            output.add(classNode.superName)
        }
        for (ifce in classNode.interfaces) {
            output.add(ifce)
        }
        return output.toTypedArray()

    }

}