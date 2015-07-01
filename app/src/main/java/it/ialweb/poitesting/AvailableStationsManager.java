package it.ialweb.poitesting;

import java.util.Random;

public class AvailableStationsManager {
    public boolean isAvailable(Station s) {
        return new Random().nextBoolean();
    }
}
