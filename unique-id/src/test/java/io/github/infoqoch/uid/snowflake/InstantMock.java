package io.github.infoqoch.uid.snowflake;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstantMock {
    static void runWithMockInstantNow(long value, Runnable run) {
        Instant mockInstant = mock(Instant.class);
        when(mockInstant.toEpochMilli()).thenReturn(value);
        try (MockedStatic<Instant> ins = Mockito.mockStatic(Instant.class)) {
            ins.when(Instant::now).thenReturn(mockInstant);
            run.run();
        }
    }

    static void runWithMockInstantNow(long[] values, Runnable run) {
        Instant[] instants = new Instant[values.length];
        for (int i = 0; i < values.length; i++) {
            Instant mock = mock(Instant.class);
            when(mock.toEpochMilli()).thenReturn(values[i]);
            instants[i] = mock;
        }

        try (MockedStatic<Instant> ins = Mockito.mockStatic(Instant.class)) {
            OngoingStubbing<Object> when = ins.when(Instant::now);
            for (Instant i : instants) {
                when = when.thenReturn(i);
            }
            run.run();
        }
    }
}
