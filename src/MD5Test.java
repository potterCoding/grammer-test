import java.security.MessageDigest;

public class MD5Test {

    public static void main(String[] args) throws Exception {
        MessageDigest digest1 = MessageDigest.getInstance("MD5");
        digest1.update("helloworld".getBytes("UTF-8"));
        byte[] result1 = digest1.digest();
        for (byte b : result1) {
            System.out.print(b + "\t"); // -4	94	3	-115	56	-91	112	50	8	84	65	-25	-2	112	16	-80
        }

        System.out.println();

        //输入的数据可以分片输入,得到的结果是一样的
        MessageDigest digest2 = MessageDigest.getInstance("MD5");
        digest2.update("hello".getBytes("UTF-8"));
        digest2.update("world".getBytes("UTF-8"));
        byte[] result2 = digest2.digest();
        for (byte b : result2) {
            System.out.print(b + "\t"); // -4	94	3	-115	56	-91	112	50	8	84	65	-25	-2	112	16	-80
        }
    }

}
