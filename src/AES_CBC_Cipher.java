import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author sun
 * @date 2020-07-08 22:28
 * @description
 */
public class AES_CBC_Cipher {

    private static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";

    //加密
    public static byte[] encrypt(byte[] key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        //CBC模式需要生成一个16字节的initiallization vector
        SecureRandom sr = SecureRandom.getInstanceStrong();
        //获取向量，即16位字节的随机数
        byte[] iv = sr.generateSeed(16);
        //把字节数组转为IvParameterSpec对象
        IvParameterSpec ivps = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivps);
        byte[] data = cipher.doFinal(input);
        //IV不需要保密，把IV和密文一起返回
        return join(iv, data);
    }

    private static byte[] join(byte[] iv, byte[] data) {
        byte[] r = new byte[iv.length + data.length];
        System.arraycopy(iv, 0 ,r, 0, iv.length);
        System.arraycopy(data, 0 ,r, iv.length, data.length);
        return r;
    }

    //解密
    public static byte[] decrypt(byte[] key, byte[] input) throws Exception {
        //把input分割成iv和密文
        byte[] iv = new byte[16];
        byte[] data = new byte[input.length - 16];
        System.arraycopy(input, 0 ,iv, 0, 16);
        System.arraycopy(input, 16 ,data, 0, data.length);

        //解密
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivps = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivps);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        //原文
        String message = "Hello, World! encrypted using AES";
        System.out.println("Message: " + message); // message: Hello, World! encrypted using AES

        //128位密钥 = 16 bytes key
        byte[] key = "1234567890abcdef".getBytes("UTF-8");
        //加密
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encrypt(key, data);
        //加密后的密文： Encrypted data: 3iwMkdAqR0eQYQqaxOEKao+N0gSp/05i+mULmLvndSKq4Z2xz122wmFARWbAwF6dElmnceO/x5pJHcwXSr8inQ==
        System.out.println("Encrypted data: " + Base64.getEncoder().encodeToString(encrypted));
        //解密
        byte[] decrypted = decrypt(key, encrypted);
        //解密后得到结果与原文相同：Decrypted data: Hello, World! encrypted using AES
        System.out.println("Decrypted data: " + new String(decrypted,"UTF-8"));
    }

}
