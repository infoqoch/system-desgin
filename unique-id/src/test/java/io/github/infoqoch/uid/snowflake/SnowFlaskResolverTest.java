package io.github.infoqoch.uid.snowflake;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static io.github.infoqoch.uid.snowflake.InstantMock.runWithMockInstantNow;
import static org.assertj.core.api.Assertions.assertThat;

public class SnowFlaskResolverTest {
    @Test
    void test(){
        // given
        SnowFlake snow = new SnowFlake(1, 10, 100);
        AtomicLong l = new AtomicLong();
        runWithMockInstantNow(200L, () -> {l.set(snow.create());});
        assert l.get() == 419602432;

        // when
        SnowFlakeResult r =  new SnowFlakeResolver(100).resolve(l.get());

        // then
        assertThat(r.sequence()).isEqualTo(0);
        assertThat(r.app()).isEqualTo(10);
        assertThat(r.node()).isEqualTo(1);
        assertThat(r.timestamp()).isEqualTo(100L);
        assertThat(r.realTimestamp()).isEqualTo(200L);
    }
}
