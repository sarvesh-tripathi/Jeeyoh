import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;




public class Test {
	public static void main(String[] hh) {
		Calendar cal = new GregorianCalendar();
		String dateStr = "2014-07-19T20:00:00Z";
		
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		
		TimeZone timeZone = TimeZone.getTimeZone("PST");
		//simple.setTimeZone(timeZone);
		Date date = new Date();
		try {
			date = simple.parse(dateStr);
			System.out.println("date: " + date);
			
			/*String defaultTimezone = TimeZone.getDefault().getID();
			date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(dateStr.replaceAll("Z$", "+0000"));

			System.out.println("string: " + dateStr);
			System.out.println("defaultTimezone: " + defaultTimezone);
			System.out.println("date: " + (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).format(date));
			System.out.println("date = " + date);*/
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Calendar with default timezone = " + cal);
		cal.setTimeZone(timeZone);
		cal.set(Calendar.ZONE_OFFSET, timeZone.getRawOffset());
		//System.out.println("Calendar with EST timezone = " + cal);
		System.out.println("dst offset = " + timeZone.getDSTSavings());
		Calendar cal1 = Calendar.getInstance(timeZone);
		
		/*cal1.set(Calendar.DST_OFFSET, timeZone.getDSTSavings());
		System.out.println("Calendar 1 with EST timezone = " + cal1);
		cal1.set(Calendar.DAY_OF_MONTH, date.getDate());
		cal1.set(Calendar.MONTH, date.getMonth());
		cal1.set(Calendar.YEAR, date.getYear());
		cal1.set(Calendar.HOUR_OF_DAY, date.getHours());
		cal1.set(Calendar.MINUTE, date.getMinutes());*/
		
		cal1.setTime(date);
		
		System.out.println("Calendar 1 with EST timezone = " + cal1);
	}

}
