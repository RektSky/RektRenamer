package ml.rektsky.rektrenamer.member.impl

import ml.rektsky.rektrenamer.ClassRepo
import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import ml.rektsky.rektrenamer.inheritance.InheritanceProvider
import ml.rektsky.rektrenamer.member.MemberProvider

class ClassRepoMemberProvider(val classRepo: ClassRepo): MemberProvider {


    override fun getDeclaredMethods(className: String): Array<MethodInfo>? {
        val classNode = classRepo[className]
        if (classNode == null) {
            return null
        }
        return classNode.methods.map { MethodInfo(className, it.name, it.desc) }.toTypedArray()
    }

    override fun getDeclaredFields(className: String): Array<FieldInfo>? {
        val classNode = classRepo[className]
        if (classNode == null) {
            return null
        }
        return classNode.fields.map { FieldInfo(className, it.name) }.toTypedArray()
    }

}