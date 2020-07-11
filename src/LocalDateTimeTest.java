import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @author sun
 * @date 2020-07-05 16:34
 * @description
 */
public class LocalDateTimeTest {

    public static void main(String[] args) {
        LocalDate d = LocalDate.now(); // 当前日期
        LocalTime t = LocalTime.now(); //当前时间
        LocalDateTime dt = LocalDateTime.now(); // 当前日期时间
        System.out.println(dt); // 严格按照ISO 8601格式打印 2020-07-05T16:38:37.356

        //指定日期和时间
        LocalDate d2 = LocalDate.of(2020, 7, 5); // 2020-07-05,注意 7=7月
        LocalTime t2 = LocalTime.of(16, 38, 37); // 16:38:37
        LocalDateTime dt2 = LocalDateTime.of(2020, 7, 5,16, 38, 37); // 2020-07-05T16:38:37
        LocalDateTime dt3 = LocalDateTime.of(d2, t2); // 2020-07-05T16:38:37

        //对日期进行格式化
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now())); // 2020-07-05 16:45:08

        //将字符串解析成日期
        LocalDateTime parse = LocalDateTime.parse("2020-07-05 16:45:08", dtf);
        System.out.println(parse); // 2020-07-05T16:45:08


        // +5 天
        LocalDate today = LocalDate.now();
        LocalDate after5Days = today.plusDays(5);
        System.out.println(after5Days); //2020-07-10

        // -2小时
        LocalTime now = LocalTime.now();
        LocalTime before2Hours = now.minusHours(2);
        System.out.println(before2Hours); // 14:59:22.526

        // +1月-2周
        LocalDate date = today.plusMonths(1).minusWeeks(2);
        System.out.println(date); // 2020-07-22

        //本月第一天
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        System.out.println(firstDay); // 2020-07-01
        //把秒和纳秒调整为0
        LocalTime at = LocalTime.now().withSecond(0).withNano(0);
        System.out.println(at); // 17:08

        //本月最后一天
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(lastDay); // 2020-07-31
        //本月第一个周日
        LocalDate firstSunday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));
        System.out.println(firstSunday); //2020-07-05

        LocalDate d01 = LocalDate.of(2020,7,5);
        LocalDate d02 = LocalDate.of(2020,7,4);
        System.out.println(d01.isBefore(d02)); // false
        System.out.println(d01.isAfter(d02)); // true

        LocalDate d03 = LocalDate.of(2020,7,5);
        LocalDate d04 = LocalDate.of(2018,3,28);
        //通过until()方法获取Period对象，判断两个日期相差？年？月？天
        Period period = d03.until(d04);
        System.out.println(period); // P-2Y-3M-8D 表示2020年7月5日到2018年3月28日中相差 2年3个月8天
        //两个日期一共相差多少天？
        long day01 = LocalDate.of(2020, 7, 5).toEpochDay();
        long day02 = LocalDate.of(2018,3,28).toEpochDay();
        System.out.println(day01-day02); // 830

    }

}
