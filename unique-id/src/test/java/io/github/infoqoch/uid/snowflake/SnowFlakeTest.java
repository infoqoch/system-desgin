package io.github.infoqoch.uid.snowflake;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

public class SnowFlakeTest {
    @Test
    void stress_test() {
        // given
        long now = Instant.now().toEpochMilli();
        SnowFlake snowFlake = new SnowFlake(5, 10, now);

        // when
        List<Long> result = IntStream.range(0, 100000)
                .parallel()
                .mapToObj(i -> snowFlake.create())
                .toList();

        // then
        assertThat(new HashSet<>(result).size()).isEqualTo(result.size()); // 중복이 없음
        SnowFlakeResolver resolver = new SnowFlakeResolver(now);
        List<SnowFlakeResult> resolved = result.stream()
                .map(resolver::resolve)
                .toList();

        // seq가 4096를 초과하거나 0보다 작아서는 안된다.
        assertThat(resolved.stream()
                .map(SnowFlakeResult::sequence)
                .filter(s -> s >= 0 && s < 4096))
                .size().isEqualTo(100000);


        // 1밀에 4096으로 완전하게 채울 경우 총 25밀의 시간을 소모 한다.
        // 웜업 시간을 고려. 대략 80퍼센트가 4096을 달성하면 성공으로 본다.
        int fastest = (100000 / 4096) + 1; // 25
        assertThat(resolved.stream()
                .collect(groupingBy(SnowFlakeResult::timestamp))
                .values().stream().map(List::size)
                .filter(v -> v == 4096)
                .count()).isGreaterThan((int) (fastest*0.8));
    }

    @Test
    void temporary_test() throws InterruptedException {
        // given
        long now = Instant.now().toEpochMilli();
        SnowFlake snowFlake = new SnowFlake(5, 10, now);

        // when
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            if(i%10==0) {
                sleep(1);
            }
            result.add(snowFlake.create());
        }

        // then
        assertThat(new HashSet<>(result).size()).isEqualTo(result.size()); // 중복이 없음

        // 각 타임스탬프마다 10개씩 존재 해야 한다.
        SnowFlakeResolver resolver = new SnowFlakeResolver(now);
        assertThat(result.stream().map(resolver::resolve)
                .collect(groupingBy(SnowFlakeResult::timestamp))
                .values().stream().map(List::size)
                .filter(s -> s != 10)
                .count()).isEqualTo(0);
    }
}
