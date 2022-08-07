/*
Library TIME - Load the processor clock
Copyright 2013 Eduardo Moura Sales Martins (edimartin@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package edk.watch;

import java.util.*;
import java.time.*;

public class Time {
	// save the milliseconds
	private long timeStart;
	private long saveTimeDistance;
	private boolean overflow;
	private LocalDateTime systemClock;
	// Time zone and calendar to get the GMTOffset
	private TimeZone tz;
	private Calendar cal;

	public static final float microsecond = 0.000001f;
	public static final float second = 1000000;
	public static final short monthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * save the time to count after the timeStart
	 */
	// get day of the year
	private static long getDayOfYear(int dayOfMonth, int month, long year) {
		long ret = 0;
		if (month < 12) {
			ret = dayOfMonth;
			for (int i = 0; i < month; i++) {
				ret += edk.watch.Time.monthDays[i];
			}
			// test if need add the bisext year day
			if (month > 2) {
				// calculate if add the bisext day
				if (edk.watch.Time.isBisext(year)) {
					ret++;
				}
			}
		}
		return ret;
	}

	private static boolean isBisext(long year) {
		if (!(year % 4 > 1) && (year % 100 > 1)) {
			return true;
		}
		return false;
	}

	// Constructor for the time class
	public Time() {
		this.timeStart = 0;
		this.saveTimeDistance = 0;
		this.overflow = false;

		this.start();
		this.clockLoadLocalTime();
	}

	public void start() {
		this.overflow = false;
		this.timeStart = edk.watch.Time.getMicrosecondsReal();
	}

	public void remove(long microseconds) {
		this.timeStart += microseconds;
	}

	public long getMicroseconds() {
		long temp = edk.watch.Time.getMicrosecondsReal();
		if (this.timeStart > temp) {
			// occur overflow
			this.overflow = true;
			return (0xFFFFFFFF - this.timeStart) + temp;
		} else {
			return (temp - this.timeStart);
		}
	}

	public static long getMicrosecondsReal() {
		//
		return System.currentTimeMillis() * 1000;
	}

	// test if occur overflow
	public boolean overflowOccurred() {
		this.getMicroseconds();
		boolean ret = this.overflow;
		this.overflow = false;
		return ret;
	}

	public float getEstimativeFrame() {
		long mili = this.getMicroseconds();
		if (mili > 0)
			return (edk.watch.Time.second / (float) mili);
		return 0.0f;
	}

	// save the distance
	public void saveDistance() {
		this.saveTimeDistance = this.getMicroseconds();
	}

	// paste the distance
	public void pasteDistance() {
		this.timeStart -= this.saveTimeDistance;
		this.saveTimeDistance = 0;
	}

	public static void sleepProcessMiliseconds(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleepProcessMicroseconds(long microseconds) {
		//
		long milliseconds = microseconds / 1000;
		int nanoseconds = (int) (microseconds % 1000);

		try {
			// Thread.sleep(0,nanoseconds);
			Thread.sleep(milliseconds, nanoseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// get seconds since epoch
	public static long getTimeSinceEpoch() {
		return (long) Instant.now().getEpochSecond();
	}

	public static long getTimeSinceEpoch(int hour, int minute, int second, int dayOfMonth, int month, long year) {
		if (year > 1970)
			year -= 1970;
		if (dayOfMonth > 0)
			dayOfMonth--;
		if (month > 0)
			month--;
		long ret = (long) second + ((long) minute * 60) + ((long) hour * 3600)
				+ ((long) (edk.watch.Time.getDayOfYear(dayOfMonth, month, year) * 86400)) + ((long) (year) * 31536000)
				+ (((long) (year) / 4) * 86400);
		//
		return ret;
	}

	public void clockLoadGMTime() {
		this.clockLoadGMTime(edk.watch.Time.getTimeSinceEpoch());
	}

	public void clockLoadGMTime(long timeSinceEpoch) {
		this.tz = TimeZone.getTimeZone("Greenwich");
		this.cal = GregorianCalendar.getInstance(this.tz);
		this.systemClock = LocalDateTime.ofEpochSecond(
				timeSinceEpoch + (this.tz.getOffset(this.cal.getTimeInMillis()) / 1000), 0, ZoneOffset.UTC);
	}

	public void clockLoadLocalTime() {
		this.clockLoadLocalTime(edk.watch.Time.getTimeSinceEpoch());
	}

	public void clockLoadLocalTime(long timeSinceEpoch) {
		this.tz = TimeZone.getDefault();
		this.cal = GregorianCalendar.getInstance(this.tz);
		this.systemClock = LocalDateTime.ofEpochSecond(
				timeSinceEpoch + (this.tz.getOffset(this.cal.getTimeInMillis()) / 1000), 0, ZoneOffset.UTC);
	}

	public long clockGetMillisecond() {
		return this.systemClock.getNano() / 1000000;
	}

	public int clockGetSecond() {
		return this.systemClock.getSecond();
	}

	public int clockGetMinute() {
		return this.systemClock.getMinute();
	}

	public int clockGetHour() {
		return this.systemClock.getHour();
	}

	public int clockGetDayOfMonth() {
		return this.systemClock.getDayOfMonth();
	}

	public int clockGetDayOfWeek() {
		return this.systemClock.getDayOfWeek().getValue();
	}

	public int clockGetDayOfYear() {
		return this.systemClock.getDayOfYear();
	}

	public int clockGetMonth() {
		return this.systemClock.getMonthValue();
	}

	public int clockGetYear() {
		return this.systemClock.getYear();
	}

	public int clockGetGMTOff() {
		return this.tz.getOffset(this.cal.getTimeInMillis()) / 1000;
	}

	public String clockGetTimezoneAbreviation() {
		return this.tz.getID();
		// return this.tz.getDisplayName();
	}
}
