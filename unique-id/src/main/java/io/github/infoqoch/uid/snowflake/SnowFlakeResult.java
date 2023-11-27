package io.github.infoqoch.uid.snowflake;

import lombok.Builder;

@Builder
public record SnowFlakeResult(int node, int app, long timestamp, long realTimestamp, int sequence){}
