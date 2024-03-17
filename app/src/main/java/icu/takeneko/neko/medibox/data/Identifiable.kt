package icu.takeneko.neko.medibox.data

import java.lang.Exception

interface Identifiable<T> {
    fun id(): Int

    fun match(id: Int): T

    class NotIdentifiableIdException(val id: Int, val identifiable: Identifiable<*>) :
        Exception("id $id not found in Identifiable object: ${identifiable.javaClass.name}")
}