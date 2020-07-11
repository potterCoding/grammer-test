import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

class Person {
    public final String name; // 表示人的名字

    public PublicKey publicKey; // 表示这个人的公钥
    public PrivateKey privateKey; // 表示这个人的私钥
    public SecretKey secretKey; //表示最终的密钥

    public Person(String name) {
        this.name = name;
    }

    //生成本地的KeyPair
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            keyGen.initialize(512); //创建一个512位的keyPair
            KeyPair keyPair = keyGen.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateSecreteKey(byte[] recivedPUblickeyBytes) {
        //从byte[]恢复PublcKey
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(recivedPUblickeyBytes);
            KeyFactory kf = KeyFactory.getInstance("DH");
            PublicKey recivedPublicKey = kf.generatePublic(keySpec);
            //生成本地密钥
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(this.privateKey); // 自己的私钥
            keyAgreement.doPhase(recivedPublicKey,true); // 对方的公钥
            //生成AES密钥
            this.secretKey = keyAgreement.generateSecret("AES");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public void printKeys(){
        System.out.printf("Name: %s\n", this.name);
        System.out.printf("private key: %x\n",new BigInteger(1,this.privateKey.getEncoded()));
        System.out.printf("public key: %x\n",new BigInteger(1,this.publicKey.getEncoded()));
        System.out.printf("secrete key: %x\n",new BigInteger(1,this.secretKey.getEncoded()));
    }

    //发送加密信息
    public String sendMessage(String message){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,this.secretKey);
            byte[] data = cipher.doFinal(message.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(data);
        } catch (GeneralSecurityException |IOException e) {
           throw new RuntimeException(e);
        }
    }

    //接收加密信息并解密
    public String reciveMessage(String message){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,this.secretKey);
            byte[] data = cipher.doFinal(Base64.getDecoder().decode(message));
            return new String(data,"UTF-8");
        } catch (GeneralSecurityException |IOException e) {
            throw new RuntimeException(e);
        }
    }

}

public class DH {

    public static void main(String[] args) {
        //Bob和Alice
        Person bob = new Person("Bob");
        Person alice = new Person("Alice");

        //生成各自的keyPair
        bob.generateKeyPair();
        alice.generateKeyPair();

        //双方交换各自的public Key
        //Bob根据Alice的public Key生成自己的本地密钥
        bob.generateSecreteKey(alice.publicKey.getEncoded());
        //Alice根据Bob的public Key生成自己的本地密钥
        alice.generateSecreteKey(bob.publicKey.getEncoded());

        //检查双方的本地密钥是否相同
        bob.printKeys();
        alice.printKeys();

        //双方的SecretKey相同，后续通信将使用SecretKey作为密钥进行AES加解密
        String msgBobToAlice = bob.sendMessage("Hello, Alice!");
        System.out.println("Bob -> Alice: " + msgBobToAlice);
        String aliceDecrypted = alice.reciveMessage(msgBobToAlice);
        System.out.println("Alice decrypted: " + aliceDecrypted);
    }

}
