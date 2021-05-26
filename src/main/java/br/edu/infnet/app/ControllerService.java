package br.edu.infnet.app;

import java.nio.charset.StandardCharsets;

public class ControllerService {
    
    public static String convertToUtf_8 (String utf) {
        
        byte[] data = utf.getBytes(StandardCharsets.ISO_8859_1);
        utf = new String(data, StandardCharsets.UTF_8);
        return utf;
    }
}
