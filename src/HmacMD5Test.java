import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * @author sun
 * @date 2020-07-08 21:39
 * @description
 */
public class HmacMD5Test {

    public static byte[] hmac(String  hmacAlgorithm, SecretKey secertKey, byte[] input) throws Exception {
        Mac mac = Mac.getInstance(hmacAlgorithm);
        mac.init(secertKey);
        mac.update(input);
        return mac.doFinal();
    }

    public static void main(String[] args) throws Exception {
        String algorithm = "HmacSHA1";
        //原始数据
        String data = "helloworld";
        //随机生成一个key
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecretKey key = keyGenerator.generateKey();
        //将key转为byte[]，打印key
        byte[] encoded = key.getEncoded();
        System.out.println(String.format("Key: %0" + (encoded.length * 2)));
        byte[] result = hmac(algorithm, key, data.getBytes("UTF-8"));
        System.out.println(String.format("Hash: %0" + (result.length * 2)));
    }

}
