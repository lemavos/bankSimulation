package com.bank.client;

import java.util.Random;

public class ValidationCodeGen {
    public static String generateCode() {
        String characters = "1234567890";
        StringBuilder code = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 6; i++) {
            int index = rand.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
}
