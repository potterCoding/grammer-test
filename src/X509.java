import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;

public class X509 {

    private final PrivateKey privateKey;
    public final X509Certificate certificate; // 证书和证书包含的公钥和摘要信息

    public X509(KeyStore keyStore, String certName, String password) {
        try {
            this.privateKey = (PrivateKey) keyStore.getKey(certName,password.toCharArray());
            this.certificate = (X509Certificate) keyStore.getCertificate(certName);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    //加密
    public byte[] encrypt(byte[] message) {
        try {
            //获得加密算法
            Cipher cipher = Cipher.getInstance(this.privateKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE,this.privateKey);
            return cipher.doFinal(message);
        }  catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    //解密
    public byte[] decrypt(byte[] message) {
        try {
            PublicKey publicKey = this.certificate.getPublicKey();
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE,publicKey);
            return cipher.doFinal(message);
        }  catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] sign(byte[] message) {
        try {
            Signature signature = Signature.getInstance(this.certificate.getSigAlgName());
            signature.initSign(this.privateKey);
            signature.update(message);
            return signature.sign();
        }  catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(byte[] message, byte[] sign) {
        try {
            Signature signature = Signature.getInstance(this.certificate.getSigAlgName());
            signature.initVerify(this.certificate);
            signature.update(message);
            return signature.verify(sign);
        }  catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    //Java中的数字证书是存储在keyStore中的
    public static KeyStore loadKeyStore(String keyStoreFile, String password) {
        try (InputStream input = new BufferedInputStream(new FileInputStream(keyStoreFile))) {
            if (input == null) {
                throw new RuntimeException("file not found in classpath: " + keyStoreFile);
            }
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(input, password.toCharArray());
            return ks;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        byte[] message = "Hello, 使用X.509证书进行加密和签名!".getBytes("UTF-8");
        // 读取KeyStore:
        KeyStore ks = loadKeyStore("my.keystore", "123456");
        // 读取证书
        X509 x509 = new X509(ks,"mycert", "123456");
        // 加密:
        byte[] encrypted = x509.encrypt(message);
        System.out.println(String.format("encrypted: %x", new BigInteger(1, encrypted)));
        // 解密:
        byte[] decrypted = x509.decrypt(encrypted);
        System.out.println("decrypted: " + new String(decrypted, "UTF-8"));
        // 签名:
        byte[] sign = x509.sign(message);
        System.out.println(String.format("signature: %x", new BigInteger(1, sign)));
        // 验证签名:
        boolean verified = x509.verify(message, sign);
        System.out.println("verify: " + verified);
    }

}
