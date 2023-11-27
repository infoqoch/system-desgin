package io.github.infoqoch.uid.binary;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BinaryOperationTest {
    @Test
    void shift_test(){
        assertThat(toBinaryMax8(10 << 0)).isEqualTo("00001010");
        assertThat(toBinaryMax8(10 << 1)).isEqualTo("00010100");
        assertThat(toBinaryMax8(10 << 2)).isEqualTo("00101000");
        assertThat(toBinaryMax8(10 << 3)).isEqualTo("01010000");
        assertThat(toBinaryMax8(10 << 4)).isEqualTo("10100000");

        assertThat(toBinaryMax8(160 >> 0)).isEqualTo("10100000");
        assertThat(toBinaryMax8(160 >> 1)).isEqualTo("01010000");
        assertThat(toBinaryMax8(160 >> 2)).isEqualTo("00101000");
        assertThat(toBinaryMax8(160 >> 3)).isEqualTo("00010100");
        assertThat(toBinaryMax8(160 >> 4)).isEqualTo("00001010");
    }

    @Test
    void xor_test(){
        assertThat(toInt("1111_0000") ^ toInt("1010_1010")).isEqualTo(toInt("0101_1010"));
        assertThat(toInt("1111_1111") ^ toInt("1111_0000")).isEqualTo(toInt("0000_1111"));
    }

    @Test
    void negative_test(){
        assertThat(toBinaryMax8(~toInt("1111_0000"))).isEqualTo("00001111");
        assertThat(toBinaryMax8(~toInt("1010_1010"))).isEqualTo("01010101");
    }

    @Test
    void util_test(){
        assertThat(toBinaryMax8(0)).isEqualTo("00000000");
        assertThat(toBinaryMax8(1)).isEqualTo("00000001");
        assertThat(toBinaryMax8(128)).isEqualTo("10000000");
        assertThat(toBinaryMax8(255)).isEqualTo("11111111");
        assertThat(toBinaryMax8(-1)).isEqualTo("11111111");
        assertThat(toBinaryMax8(256)).isEqualTo("00000000");

        assertThat(toInt("1000_0000")).isEqualTo(128);
        assertThat(toInt("10000000")).isEqualTo(128);
        assertThat(toInt("1000000")).isEqualTo(64);
        assertThat(toInt("000010")).isEqualTo(2);
        assertThat(toInt("000000")).isEqualTo(0);
    }

    // ignore "_"
    static int toInt(String str) {
        return Integer.parseInt(str.replaceAll("_", ""), 2);
    }

    static String toBinaryMax8(int input){
        String sub = String.format("%8s", Integer.toBinaryString(input));
        return sub.substring(sub.length() - 8).replaceAll(" ", "0");
    }
}
