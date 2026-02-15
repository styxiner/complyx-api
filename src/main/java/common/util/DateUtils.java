package common.util;

import java.time.Instant;

public class DateUtils {
	private DateUtils() {
		
	}	
	public static Instant nowUtc() {
		return Instant.now();
	}

}
