package ml.rektsky.rektrenamer.inheritance.impl

import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import ml.rektsky.rektrenamer.member.MemberProvider

class JointMemberProvider: MemberProvider {

    private val providers = ArrayList<MemberProvider>()

    fun addChildren(memberProvider: MemberProvider) {
        providers.add(memberProvider)
    }

    override fun getDeclaredMethods(className: String): Array<MethodInfo>? {
        for (provider in providers) {
            val methods = provider.getDeclaredMethods(className)
            if (methods != null) {
                return methods
            }
        }
        return null
    }

    override fun getDeclaredFields(className: String): Array<FieldInfo>? {
        for (provider in providers) {
            val fields = provider.getDeclaredFields(className)
            if (fields != null) {
                return fields
            }
        }
        return null
    }
}