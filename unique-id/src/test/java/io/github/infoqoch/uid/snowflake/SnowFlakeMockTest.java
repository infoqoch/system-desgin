package io.github.infoqoch.uid.snowflake;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static io.github.infoqoch.uid.snowflake.InstantMock.runWithMockInstantNow;
import static org.assertj.core.api.Assertions.assertThat;

public class SnowFlakeMockTest {
    @Test
    void nodeAndApp(){
        // node, app : 01010, 01100
        int node = 10;
        int app = 12;

        SnowFlake snow = new SnowFlake(node, app, 0L);
        long expected = ((10L << 5) + 12L << 12) + 0L;
        runWithMockInstantNow(0L, () -> assertThat(snow.create()).isEqualTo(expected));
    }

    @Test
    void timestamp(){
        // 10(now) - 1(default) => 9 // 1001
        long defaultTimestamp = 1L;
        long now = 10L;

        SnowFlake snow = new SnowFlake(10, 12, defaultTimestamp);
        long expected = (9L << 22) + ((10L << 5) + 12L << 12) + 0L;
        runWithMockInstantNow(now, () -> assertThat(snow.create()).isEqualTo(expected));
    }

    @Test
    void maxTimestamp(){
        // max(now) - 0(default) => max // 11...11
        long maxTimeStamp = ~(-1L << 41);

        SnowFlake snow = new SnowFlake(10, 12, 0L);
        long expected = (maxTimeStamp << 22)+ ((10L << 5) + 12L <<12) + 0L;
        runWithMockInstantNow(maxTimeStamp, () -> assertThat(snow.create()).isEqualTo(expected));
    }

    /*
    * 시간(Instant.now())는 0인 상태로 4096번 ID를 생성한다.
    * 한 번 더 생성을 요청할 경우 시간이 0을 초과할 때까지 반복문으로 대기한다.
    * {0, 0, 1, 10, 100} 순서대로 Instant.now()를 제공하며
    *   - 1에 달성할 때 반복문은 종료되고
    *   - 다음 시간은 10이 되어야 한다.
    * 이를 통하여 같은 시간일 때 4096을 초과할 경우 while문으로 다음 밀까지 대기함을 확인한다.
    * */
    @Test
    void sequenceOverflow(){
        // given
        SnowFlake snow = new SnowFlake(10, 12, 0L);

        // when - 4096만큼 소모한다.
        for (long l = 0; l < 4096; l++) {
            long expected = ((10L << 5) + 12L <<12) + l;
            runWithMockInstantNow(0L, () -> assertThat(snow.create()).isEqualTo(expected));
        }

        // when - then
        // 0보다 큰 숫자가 나올 때까지 대기
        long expectedAfter1Mill = (1L<<22) + ((10L << 5) + 12L <<12) + 0;
        runWithMockInstantNow(new long[]{0, 0, 1, 10, 100}, () -> {
            assertThat(snow.create()).isEqualTo(expectedAfter1Mill);
            assertThat(Instant.now().toEpochMilli()).isEqualTo(10);
        });
    }
}
