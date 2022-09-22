package ml.rektsky.rektrenamer.modifiable.impl

import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import ml.rektsky.rektrenamer.member.MemberProvider
import ml.rektsky.rektrenamer.modifiable.ModifiableProvider

class ReflectionModifiableProvider(val classLoader: ClassLoader, val modifiable: Boolean): ModifiableProvider {
    override fun isModifiable(className: String): Boolean? {
        try {
            classLoader.loadClass(className.replace("/", "."))
            return modifiable
        } catch (_: ClassNotFoundException) {
            return null
        }
    }

}