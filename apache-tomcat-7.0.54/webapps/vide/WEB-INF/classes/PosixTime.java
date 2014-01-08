import java.util.Calendar;
import java.util.GregorianCalendar;
public class PosixTime {

	public static long convertDate(java.util.Date date){
		Calendar tmp = new GregorianCalendar();
		tmp.setTime(date);
		return tmp.getTimeInMillis()/1000;
	}


	public static java.util.Date convertPosix(long posix){
		Calendar tmp = new GregorianCalendar();
		tmp.setTimeInMillis(posix*1000);
		return tmp.getTime();
	}

	public static java.util.Date addInterval(long posix, String field, int interval){
		Calendar tmp = new GregorianCalendar();
		int tmpField = 0;
		tmp.setTimeInMillis(posix*1000);
		if(field.equals("day")){
			tmpField = Calendar.DAY_OF_MONTH;
		}
		if(field.equals("week")){
			tmpField = Calendar.WEEK_OF_YEAR;
		}
		if(field.equals("month")){
			tmpField = Calendar.MONTH;
		}
		if(field.equals("year")){
			tmpField = Calendar.YEAR;
		}
		if(field.equals("hour")){
			tmpField = Calendar.HOUR;
		}
		if(field.equals("minute")){
			tmpField = Calendar.MINUTE;
		}
		
		if(tmpField!=0)
			tmp.add(tmpField, interval);
		return tmp.getTime();
	}

	public static java.util.Date addInterval(long posix, String field){
		return addInterval(posix, field, 1);
	}

	public static long addIntervalPosix(long posix, String field, int interval){
		return PosixTime.convertDate(addInterval(posix, field, interval));
	}

	public static long addIntervalPosix(long posix, String field){
		return PosixTime.addIntervalPosix(posix, field, 1);
	}
}
