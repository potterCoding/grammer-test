import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author sun
 * @date 2020-07-08 22:09
 * @description
 */
public class AES_ECB_Cipher {

    private static final String CIPHER_NAME = "AES/ECB/PKCS5Padding";

    //加密
    public static byte[] encrypt(byte[] key, byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        //使用加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        //通过doFinal()得到加密后的字节数组
        return cipher.doFinal(input);
    }

    //解密
    public static byte[] decrypt(byte[] key, byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        //使用解密模式
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        //通过doFinal()将密文还原为原文
        return cipher.doFinal(input);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        //原文
        String message = "Hello, World! encrypted using AES";
        System.out.println("Message: " + message); // message: Hello, World! encrypted using AES

        //128位密钥 = 16 bytes key
        byte[] key = "1234567890abcdef".getBytes("UTF-8");
        //加密
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encrypt(key, data);
        //加密后的密文： Encrypted data: g89TtEMHXpwwjrEbXcljDQIUi09dPO9fVx4OgZ7ozsFgo8Zilj6cypxChst75GTR
        System.out.println("Encrypted data: " + Base64.getEncoder().encodeToString(encrypted));
        //解密
        byte[] decrypted = decrypt(key, encrypted);
        //解密后得到结果与原文相同：Decrypted data: Hello, World! encrypted using AES
        System.out.println("Decrypted data: " + new String(decrypted,"UTF-8"));
    }

}
