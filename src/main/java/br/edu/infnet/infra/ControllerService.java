package br.edu.infnet.infra;

import java.nio.charset.StandardCharsets;

public class ControllerService {

    private final static String ValidAlfaCharRegex = "^[,-\\.ÀàÁáÃãÂâÉéÊêÍíÎîÓóÕõÔôÚúÛûÜüÇça-zA-Z0-9\\s]+$";
    private final static String ValidEmailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
//    private final static String ValidEmailRegex = "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";

    public static boolean isValidString(String str) {

        return str.matches(ValidAlfaCharRegex);
    }

    public static boolean isValidEmail(String str) {

        return str.matches(ValidEmailRegex);
    }
    
//    public static String convertToUtf_8 (String utf) {
//
//        byte[] data = utf.getBytes(StandardCharsets.ISO_8859_1);
//        utf = new String(data, StandardCharsets.UTF_8);
//        return utf;
//    }
}
