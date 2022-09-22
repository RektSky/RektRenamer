package ml.rektsky.rektrenamer.member

import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo


class MemberMap {

    private val methods = HashMap<String, Array<MethodInfo>>()
    private val fields = HashMap<String, Array<FieldInfo>>()

    fun getDeclaredMethods(internalName: String): Array<MethodInfo>? {
        return methods[internalName]
    }
    fun getDeclaredFields(internalName: String): Array<FieldInfo>? {
        return fields[internalName]
    }

    fun putMethod(internalName: String, array: Array<MethodInfo>) {
        methods[internalName] = array
    }
    fun putFields(internalName: String, array: Array<FieldInfo>) {
        fields[internalName] = array
    }

}