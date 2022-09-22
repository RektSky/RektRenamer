package ml.rektsky.rektrenamer.member.impl

import ml.rektsky.rektrenamer.data.ClassInfo
import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import ml.rektsky.rektrenamer.inheritance.InheritanceProvider
import ml.rektsky.rektrenamer.member.MemberProvider

class ReflectionMemberProvider(val classLoader: ClassLoader): MemberProvider {

    override fun getDeclaredMethods(className: String): Array<MethodInfo>? {
        try {
            val clazz = classLoader.loadClass(className.replace("/", "."))
            return clazz.methods.map { MethodInfo(it) }.toTypedArray()
        } catch (_: ClassNotFoundException) {
            return null
        }

    }

    override fun getDeclaredFields(className: String): Array<FieldInfo>? {
        try {
            val clazz = classLoader.loadClass(className.replace("/", "."))
            return clazz.fields.map { FieldInfo(it) }.toTypedArray()
        } catch (_: ClassNotFoundException) {
            return null
        }
    }

}