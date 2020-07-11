import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyPair {

    //私钥
    private PrivateKey sk;

    //公钥
    private PublicKey pk;

    //生成公钥/私钥对
    public RSAKeyPair() throws GeneralSecurityException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair kp = keyGen.generateKeyPair();
        this.sk = kp.getPrivate();
        this.pk = kp.getPublic();
    }

    //从已保存的字节中（例如读取文件）恢复公钥/密钥
    public RSAKeyPair(byte[] pk, byte[] sk) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pk);
        this.pk = keyFactory.generatePublic(keySpec);
        PKCS8EncodedKeySpec skSpec = new PKCS8EncodedKeySpec(sk);
        this.sk = keyFactory.generatePrivate(skSpec);
    }

    //把私钥到处为字节
    public byte[] getPrivateKey(){
        return this.sk.getEncoded();
    }

    //把公钥导出为字节
    public byte[] getPublicKey(){
        return this.pk.getEncoded();
    }

    //用公钥加密
    public byte[] encrypt(byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,this.pk);
        return cipher.doFinal(message);
    }

    //用私钥解密
    public byte[] decrypt(byte[] input) throws GeneralSecurityException{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.sk);
        return cipher.doFinal(input);
    }

    public static void main(String[] args) throws Exception {
        //明文
        byte[] plain = "Hello，使用RSA非对称加密算法对数据进行加密".getBytes();
        //创建公钥/私钥 对
        RSAKeyPair rsa = new RSAKeyPair();
        //加密
        byte[] encrypt = rsa.encrypt(plain);
        System.out.println("encrypted: " + Base64.getEncoder().encodeToString(encrypt));
        //解密
        byte[] decrypt = rsa.decrypt(encrypt);
        System.out.println("decrypted: " + new String(decrypt,"UTF-8"));

        //保存公钥/私钥 对
        byte[] sk = rsa.getPrivateKey();
        byte[] pk = rsa.getPublicKey();
        System.out.println("sk: " + Base64.getEncoder().encodeToString(sk));
        System.out.println("pk: " + Base64.getEncoder().encodeToString(pk));

        //重新恢复公钥/私钥
        RSAKeyPair rsaKeyPair = new RSAKeyPair(pk, sk);
        //加密
        byte[] encrypted = rsaKeyPair.encrypt(plain);
        System.out.println("encrypted: " + Base64.getEncoder().encodeToString(encrypted));
        //解密
        byte[] decrypted = rsa.decrypt(encrypted);
        System.out.println("decrypted: " + new String(decrypted,"UTF-8"));


    }

}
