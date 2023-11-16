package uid;

import org.junit.jupiter.api.Test;

import java.time.Instant;

public class EpochTest {
    @Test
    void test(){
        long from = 1288834974657L;
        long now = 297616116568L;

        System.out.println("트위터 타임 = " + Instant.ofEpochMilli(from));
        System.out.println("트위터 타임에 현재 시간을 더한다 = " + Instant.ofEpochMilli(from+now));
        System.out.println("long의 최대값도 정상적으로 출력한다. 비록 그 때가 2억년이지만 = " + Instant.ofEpochMilli(Long.MAX_VALUE));
        System.out.println(32*32);
    }
}
