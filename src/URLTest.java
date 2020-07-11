import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author sun
 * @date 2020-07-08 20:08
 * @description
 */
public class URLTest {

    public static void main(String[] args) throws Exception {
        String orginal = "URL 参数";
        // URL 编码
        String encode = URLEncoder.encode(orginal, "UTF-8");
        System.out.println(encode); // URL+%E5%8F%82%E6%95%B0

        // URL解码
        String decode = URLDecoder.decode(encode, "UTF-8");
        System.out.println(decode); // URL 参数
    }
}
