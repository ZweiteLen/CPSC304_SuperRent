package ca.ubc.cpsc304.number_generator;

import java.util.Random;

/**
 * The purpose of this class is to generate random numbers for purposes of IDs,
 * such as confirmation number in Reservations.
 *
 * Put all random generator methods here.
 */
public class RandomNumberGenerator {
    private static final String LOG_TAG = RandomNumberGenerator.class.getSimpleName();

    public static int generateRandomReservationNumber() {
        Random random = new Random();
        int n = random.nextInt(100000);

        System.out.println(LOG_TAG + " Random number: " + n);
        return n;
    }
}
