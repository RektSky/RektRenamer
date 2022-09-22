package ml.rektsky.rektrenamer.member

import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo

interface MemberProvider {

    fun getDeclaredMethods(className: String): Array<MethodInfo>?
    fun getDeclaredFields(className: String): Array<FieldInfo>?


}