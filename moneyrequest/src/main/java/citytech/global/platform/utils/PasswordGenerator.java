package citytech.global.platform.utils;

import java.util.Random;

public class PasswordGenerator {

    public static String generateRandomPassword() {
        String characters = System.getenv("PASSWORD_GENERATOR");
        int passwordLength = 8;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
}
