package android.util

import java.util.Base64.getDecoder
import java.util.Base64.getEncoder

public object Base64 {
    @JvmStatic
    public fun encodeToString(input: ByteArray?, flags: Int): String {
        return getEncoder().encodeToString(input)
    }

    @JvmStatic
    public fun decode(str: String?, flags: Int): ByteArray {
        return getDecoder().decode(str)
    }
}