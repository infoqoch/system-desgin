package io.github.infoqoch.uid.snowflake;

public class SnowFlakeResolver {
    private final static long LENGTH_5;
    private final static long LENGTH_12;
    private final static long LENGTH_41;
    private final long defaultTimestamp;

    static {
        LENGTH_5 = generateOneBits(5);
        LENGTH_12 = generateOneBits(12);
        LENGTH_41 = generateOneBits(41);
    }

    public SnowFlakeResolver(long defaultTimestamp) {
        this.defaultTimestamp = defaultTimestamp;
    }

    public SnowFlakeResult resolve(long l) {
        return SnowFlakeResult.builder()
                .realTimestamp(realTimestamp(l))
                .timestamp(timestamp(l))
                .node(node(l))
                .app(app(l))
                .sequence(sequence(l))
                .build();
    }

    private long realTimestamp(long l) {
        return timestamp(l) + defaultTimestamp;
    }

    private long timestamp(long l) {
        return (l >> 22) & LENGTH_41;
    }

    private int node(long l) {
        return (int) ((l >> 17) & LENGTH_5);
    }

    private int app(long l) {
        return (int) ((l >> 12) & LENGTH_5);
    }

    private int sequence(long l) {
        return (int) (l & LENGTH_12);
    }

    private static long generateOneBits(int length) {
        return ~(-1L << length);
    }
}
