package icu.takeneko.neko.medibox.nfc

class NfcException(message: String): RuntimeException(message){
    constructor(message: String, e:Exception) : this(message) {
        super.initCause(e)
    }
}