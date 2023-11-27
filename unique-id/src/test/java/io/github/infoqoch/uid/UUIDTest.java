package io.github.infoqoch.uid;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UUIDTest {
    @Test
    void hex_only(){
        Set<String> chars = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            UUID.randomUUID().toString()
                    .chars()
                    .filter(v -> v != '-')
                    .mapToObj(Character::toString)
                    .forEach(chars::add);
        }

        assertThat(chars).containsOnly("a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }
}
