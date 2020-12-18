import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.*; 

public class AESreceiver {
	
	private static SecretKeySpec secretKey;
    private static byte[] key;
 
 
    public static void makeKey(String myKey) 
    {
        MessageDigest msgD = null;
        try {
            key = myKey.getBytes("UTF-8");
            msgD = MessageDigest.getInstance("SHA-1");
            key = msgD.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encryption(String msg, String secret) 
    {
        try
        {
            makeKey(secret);
            Cipher ciph = Cipher.getInstance("AES/ECB/PKCS5Padding");
            ciph.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(ciph.doFinal(msg.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR with encryption: " + e.toString());
        }
        return null;
    }
    
    public static String decryption(String msg, String secret) 
    {
        try
        {
            makeKey(secret);
            Cipher ciph = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            ciph.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(ciph.doFinal(Base64.getDecoder().decode(msg)));
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR with decryption: " + e.toString());
        }
        return null;
    }
}