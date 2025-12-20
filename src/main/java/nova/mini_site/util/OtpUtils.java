package nova.mini_site.util;

import java.security.SecureRandom;

/**
 * Utilitaires pour générer des OTP numériques.
 */
public final class OtpUtils {

    private static final SecureRandom RNG = new SecureRandom();

    private OtpUtils() {
    }

    /**
     * Génère un OTP à 6 chiffres (000000 → 999999) sous forme de String.
     */
    public static String genererOtp() {
        int value = RNG.nextInt(1_000_000);
        return String.format("%06d", value);
    }
}
