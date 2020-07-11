import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author sun
 * @date 2020-07-05 17:31
 * @description
 */
public class ZonedDateTimeTest {

    public static void main(String[] args) {
        ZonedDateTime zbj = ZonedDateTime.now(); // 当前时区的日期和时间
        System.out.println(zbj); // 2020-07-05T17:32:40.415+08:00[Asia/Shanghai]

        ZonedDateTime zny = ZonedDateTime.now(ZoneId.of("America/New_York")); // 纽约时区的当前日期和时间
        System.out.println(zny); // 2020-07-05T05:34:29.522-04:00[America/New_York]

        LocalDateTime ldt = LocalDateTime.of(2020, 7, 5, 17, 36, 12);

        //关联到当前默认时区
        ZonedDateTime bj = ldt.atZone(ZoneId.systemDefault());
        System.out.println(bj); // 2020-07-05T17:36:12+08:00[Asia/Shanghai]

        // 转换到纽约时区
        ZonedDateTime zd = bj.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println(zd); // 2020-07-05T17:36:12-04:00[America/New_York]

        //关联到纽约时区
        ZonedDateTime ny = ldt.atZone(ZoneId.of("America/New_York"));
        System.out.println(ny); // 2020-07-05T17:36:12-04:00[America/New_York]

        Instant ins = Instant.now();
        Instant ins2 = ZonedDateTime.now().toInstant();
        ZonedDateTime zdt = ins.atZone(ZoneId.of("Z"));
        //注意是秒
        long epoch = ins.getEpochSecond();
    }

}
