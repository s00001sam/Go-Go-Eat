package com.sam.gogoeat.utils

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.sam.gogoeat.utils.Util.getDinstance
import org.junit.Test
import java.net.ConnectException


class UtilTest {

    @Test
    fun `give the totalNumber below 0 to get random number return null`() {
        val total = -1
        val ans = Util.getRandomNum(total)
        assertThat(ans).isEqualTo(null)
    }

    @Test
    fun `give the normal totalNumber to get random number return right integer`() {
        repeat((0..10000).count()) {
            val total = 60
            val ans = Util.getRandomNum(total)
            assertThat(ans).isLessThan(60)
        }
    }

    @Test
    fun `check get distance is return right number`() {
        repeat((0..10000).count()) {
            val location1 = LatLng(24.5, 121.5)
            val location2 = LatLng(23.2, 122.8)
            assertThat(location1.getDinstance(location2)).isEqualTo(195892)
        }
    }

    @Test
    fun `check error string is correct`() {
        val e = ConnectException()
        val isSame = ErrorUtil.getErrorMsg(e) == "no internet access!"
        assertThat(isSame).isTrue()
    }

    @Test
    fun `check if exception is unknown should return unknown error`() {
        val e = Exception()
        val isSame = ErrorUtil.getErrorMsg(e) == "Unknown Error"
        assertThat(isSame).isTrue()
    }
}