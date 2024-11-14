package com.elys.pos.util.v1;

import com.elys.pos.util.v1.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidatorUtil {

    public boolean validateUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid uuid: " + uuid);
        }
    }
}
