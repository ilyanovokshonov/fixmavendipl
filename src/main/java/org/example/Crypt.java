package org.example;
// класс для генерации хэшей

import java.security.MessageDigest;

public class Crypt {
 
    //функция берет строку и возвращает хэшированную строку
    public static String sha256(String input)
    {
        try { MessageDigest sha = MessageDigest.getInstance("SHA-256");
            int i = 0;
            byte[] hash= sha.digest(input.getBytes("UTF-8"));
 
            // hexHash будет содержать шестнадцатиричный хэш
            StringBuffer hexHash = new StringBuffer();
 
            while (i < hash.length) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) 
                hexHash.append('0');
                hexHash.append(hex);
                i++;
            }
            return hexHash.toString();
        }
        catch (Exception e) {
        throw new RuntimeException(e);
        }
    }
}