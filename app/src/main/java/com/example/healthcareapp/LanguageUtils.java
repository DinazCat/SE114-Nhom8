package com.example.healthcareapp;

public class LanguageUtils {

    public static Language CURRENT_LANGUAGE = Language.ENGLISH;

    public static String getDatabaseName(String prefix) {
        if (CURRENT_LANGUAGE == Language.ENGLISH) {
            return prefix + "Eng";
        } else return prefix;
    }

    public static void setCurrentLanguage(Language currentLanguage) {
        CURRENT_LANGUAGE = currentLanguage;
    }

    public static Language getCurrentLanguage() {
        return CURRENT_LANGUAGE;
    }

}
