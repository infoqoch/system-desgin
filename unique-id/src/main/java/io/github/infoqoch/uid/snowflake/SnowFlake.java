package io.github.infoqoch.uid.snowflake;


import java.time.Instant;

/*
 * UNUSED_BITS : 1
 * EPOCH_BITS : 41
 * NODE_AND_APP_BITS : 10
 * SEQUENCE_BITS : 12
 * TOTAL : 64
 *
 * UNUSED_BITS은 항상 0이며 이는 Long으로 변환하면 언제나 양수가 된다.
 * */
public class SnowFlake {
    private final long worker;
    private final long defaultTimestamp;
    private volatile int sequence = 0;
    private volatile long latestTimestamp = 0;

    public SnowFlake(int node, int app, long defaultTimestamp) {
        this.worker = ((long) node << 5) + app;
        this.defaultTimestamp = defaultTimestamp;
    }

    public synchronized long create() {
        long currentTimestamp = Instant.now().toEpochMilli();
        if(currentTimestamp != latestTimestamp){
            sequence = 0;
        } else if(sequence > 4095 && currentTimestamp == latestTimestamp){
            while((currentTimestamp = Instant.now().toEpochMilli()) == latestTimestamp);
            sequence = 0;
        }
        latestTimestamp = currentTimestamp;

        return (latestTimestamp - defaultTimestamp) << 22
                | worker <<12
                | sequence++;
    }
}