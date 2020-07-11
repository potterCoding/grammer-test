import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecRSASignature {

    private PublicKey pk;

    private PrivateKey sk;

    public SecRSASignature() throws GeneralSecurityException {
        //生成 KeyPair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair kp = keyGen.generateKeyPair();
        this.sk = kp.getPrivate();
        this.pk = kp.getPublic();
    }

    //从已保存的字节中（例如读取文件）恢复公钥/密钥
    public SecRSASignature(byte[] pk, byte[] sk) throws GeneralSecurityException {
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

    //对消息进行签名
    public byte[] sign(byte[] message) throws GeneralSecurityException {
        //sign by sk
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(this.sk);
        signature.update(message);
        return signature.sign();
    }

    //私用公钥验证签名
    public boolean verify(byte[] message, byte[] sign) throws GeneralSecurityException {
        //verify by pk
        Signature sha1withRSA = Signature.getInstance("SHA1withRSA");
        sha1withRSA.initVerify(this.pk);
        sha1withRSA.update(message);
        return sha1withRSA.verify(sign);
    }

    public static void main(String[] args) throws GeneralSecurityException {
        byte[] message = "Hello,使用SHA1withRSA算法进行数字签名！".getBytes(StandardCharsets.UTF_8);
        SecRSASignature rsas = new SecRSASignature();
        byte[] sign = rsas.sign(message);
        System.out.println("sign: " + Base64.getEncoder().encodeToString(sign));
        boolean verified = rsas.verify(message, sign);
        System.out.println("verified: " + verified);
        //用另一个公钥验证
        boolean verified02 = new SecRSASignature().verify(message, sign);
        System.out.println("verify with another public key: " + verified02);
        //修改原始信息
        message[0] = 100;
        boolean verified03 = rsas.verify(message, sign);
        System.out.println("verify changed message: " + verified03);
    }

}
