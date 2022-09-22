package ml.rektsky.rektrenamer.inheritance.impl

import ml.rektsky.rektrenamer.inheritance.InheritanceProvider

class ReflectionInheritanceProvider(val classLoader: ClassLoader): InheritanceProvider {

    override fun getParents(className: String): Array<String>? {
        try {
            val output = ArrayList<String>()
            val clazz = classLoader.loadClass(className.replace("/", "."))
            if (clazz.superclass != null) {
                output.add(clazz.superclass.name.replace(".", "/"))
            }
            for (ifce in clazz.interfaces) {
                output.add(ifce.name.replace(".", "/"))
            }
            return output.toTypedArray()
        } catch (_: ClassNotFoundException) {
            return null
        }

    }

}