package io.github.infoqoch.uid.snowflake;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class InstantMockTest {
    @Test
    void runWithMockInstantNow_test(){
        InstantMock.runWithMockInstantNow(123L, () -> assertThat(Instant.now().toEpochMilli()).isEqualTo(123L));
        InstantMock.runWithMockInstantNow(0L, () -> assertThat(Instant.now().toEpochMilli()).isEqualTo(0L));
    }

    @Test
    void runWithMockInstantNowArr_test(){
        InstantMock.runWithMockInstantNow(new long[]{1,2,3}, () -> {
            assertThat(Instant.now().toEpochMilli()).isEqualTo(1L);
            assertThat(Instant.now().toEpochMilli()).isEqualTo(2L);
            assertThat(Instant.now().toEpochMilli()).isEqualTo(3L);
        });
    }
}
