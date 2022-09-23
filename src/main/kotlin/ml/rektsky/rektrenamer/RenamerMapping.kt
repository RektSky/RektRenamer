package ml.rektsky.rektrenamer

import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import ml.rektsky.rektrenamer.inheritance.InheritanceMap
import ml.rektsky.rektrenamer.inheritance.InheritanceProvider
import ml.rektsky.rektrenamer.member.MemberMap
import ml.rektsky.rektrenamer.member.MemberProvider
import ml.rektsky.rektrenamer.modifiable.ModifiableMap
import ml.rektsky.rektrenamer.modifiable.ModifiableProvider

class RenamerMapping {

    var memberMap = MemberMap()
    var fallbackMemberProvider: MemberProvider? = null

    var inheritanceMap = InheritanceMap()
    var fallbackInheritanceProvider: InheritanceProvider? = null

    var modifiableMap = ModifiableMap()
    var fallbackModifiableProvider: ModifiableProvider? = null

    private val fields = HashMap<FieldInfo, String>()
    private val methods = HashMap<MethodInfo, String>()
    private val classes = HashMap<String, String>()

    /**
     * Check if the method remapping will break the inheritance
     */
    fun canRenameMethod(methodInfo: MethodInfo): Boolean {
        return isModifiable(getDeclaringClass(methodInfo))
    }

    fun getRemappedMethodName(methodInfo: MethodInfo): String? {
        return methods[MethodInfo(getDeclaringClass(methodInfo), methodInfo.name, methodInfo.desc)]
    }

    fun getRemappedFieldName(fieldInfo: FieldInfo): String? {
        return fields[fieldInfo]
    }

    fun getRemappedClassName(internalName: String): String? {
        return classes[internalName]
    }

    fun renameField(fieldInfo: FieldInfo, name: String) {
        fields[fieldInfo] = name
    }
    fun renameClass(className: String, name: String) {
        classes[className] = name
    }
    fun renameMethod(methodInfo: MethodInfo, name: String) {
        if (!canRenameMethod(methodInfo)) {
            return
        }
        methods[methodInfo] = name
    }

    fun getDeclaringClass(methodInfo: MethodInfo): String {
        var declaringClass: String = methodInfo.owner
        visitAllParents(methodInfo.owner) {
            for (declaredMethod in getDeclaredMethods(it)) {
                if (declaredMethod.name == methodInfo.name && declaredMethod.desc == methodInfo.desc) {
                    declaringClass = it
                    return@visitAllParents true
                }
            }
            false
        }
        return declaringClass
    }

    /**
     * The boolean in this method means if the visitor has done the job. If true, it will stop visiting parents
     */
    fun visitAllParents(internalName: String, visitor: (parent: String) -> Boolean): Boolean {
        for (parent in getParents(internalName)) {
            if (visitor(parent)) {
                return true
            }
        }
        for (parent in getParents(internalName)) {
            if (visitAllParents(parent, visitor)) {
                return true
            }
        }
        return false
    }

    fun getParents(internalName: String): Array<String> {
        var result = inheritanceMap.getParents(internalName)
        if (result == null) {
            result = fallbackInheritanceProvider?.getParents(internalName)
            if (result == null) {
                return arrayOf()
            } else {
                inheritanceMap[internalName] = result
            }
        }
        return result
    }

    fun isModifiable(internalName: String): Boolean {
        var result = modifiableMap.isModifiable(internalName)
        if (result == null) {
            result = fallbackModifiableProvider?.isModifiable(internalName)
            if (result == null) {
                return false
            } else {
                modifiableMap[internalName] = result
            }
        }
        return result
    }

    fun getDeclaredMethods(internalName: String): Array<MethodInfo> {
        var result = memberMap.getDeclaredMethods(internalName)
        if (result == null) {
            result = fallbackMemberProvider?.getDeclaredMethods(internalName)
            if (result == null) {
                return arrayOf()
            } else {
                memberMap.putMethod(internalName, result)
            }
        }
        return result
    }

    fun getDeclaredFields(internalName: String): Array<FieldInfo> {
        var result = memberMap.getDeclaredFields(internalName)
        if (result == null) {
            result = fallbackMemberProvider?.getDeclaredFields(internalName)
            if (result == null) {
                return arrayOf()
            } else {
                memberMap.putFields(internalName, result)
            }
        }
        return result
    }

}