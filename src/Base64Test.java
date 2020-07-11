import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author sun
 * @date 2020-07-08 20:20
 * @description
 */
public class Base64Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String orignal = "Hello\u00ff编码测试";
//        String b64 = Base64.getEncoder().encodeToString(orignal.getBytes("UTF-8"));

        //去掉等号，实际上有没等号在解码时是不影响的
        String b64 = Base64.getEncoder().withoutPadding().encodeToString(orignal.getBytes("UTF-8"));
        System.out.println(b64);

        String ori = new String(Base64.getDecoder().decode(b64), "UTF-8");
        System.out.println(ori);

        //实现URL的Base64编码和解码
        String urlB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(orignal.getBytes("UTF-8"));
        System.out.println(urlB64);

        String urlOri = new String(Base64.getUrlDecoder().decode(urlB64), "UTF-8");
        System.out.println(urlOri);

        //在Java中，使用URL的Base64编码，它会把"+"变为"-",把"/"变为"_"，这样我们在传递URL参数的时候，就不会引起冲突
    }

}
