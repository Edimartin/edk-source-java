/*
Library EDK - How to use Extensible Development Kit
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

import edk.watch.Time;
import edk.watch.FPS;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.printf("Hello EDK WATCH");
		
		edk.watch.Time clock = new edk.watch.Time();
		clock.start();
		for(int i=0;i<2;i++) {
			System.out.printf("\nClock == %d", clock.getMicroseconds());
			edk.watch.Time.sleepProcessMicroseconds(1000000);
		}
		
		clock.clockLoadGMTime();
		System.out.printf("\nGMT       Time %d-%d-%d %d:%d:%d"
				,clock.clockGetYear()
				,clock.clockGetMonth()
				,clock.clockGetDayOfMonth()
				,clock.clockGetHour()
				,clock.clockGetMinute()
				,clock.clockGetSecond()
				);
		System.out.printf("\nZone clockGetGMTOff == '%s'",clock.clockGetGMTOff());
		System.out.printf("\nZone clockGetTimezoneAbreviation == '%s'",clock.clockGetTimezoneAbreviation());
		
		clock.clockLoadLocalTime();
		System.out.printf("\nLOCALTIME Time %d-%d-%d %d:%d:%d"
				,clock.clockGetYear()
				,clock.clockGetMonth()
				,clock.clockGetDayOfMonth()
				,clock.clockGetHour()
				,clock.clockGetMinute()
				,clock.clockGetSecond()
				);
		System.out.printf("\nZone clockGetGMTOff == '%s'",clock.clockGetGMTOff());
		System.out.printf("\nZone clockGetTimezoneAbreviation == '%s'",clock.clockGetTimezoneAbreviation());
		
		clock.clockLoadLocalTime(0);
		System.out.printf("\nBEGGIN    Time %d-%d-%d %d:%d:%d"
				,clock.clockGetYear()
				,clock.clockGetMonth()
				,clock.clockGetDayOfMonth()
				,clock.clockGetHour()
				,clock.clockGetMinute()
				,clock.clockGetSecond()
				);
		System.out.printf("\nZone clockGetGMTOff == '%s'",clock.clockGetGMTOff());
		System.out.printf("\nZone clockGetTimezoneAbreviation == '%s'",clock.clockGetTimezoneAbreviation());

		//calculate if the time if is bigger than one second
		clock.start();
		int seconds = 5;
		int frames = 0;
		
		//use the fps to wait the thread to have the FPS
		edk.watch.FPS fps = new edk.watch.FPS(60);
		
		fps.start();
		while(seconds>0){
			if(clock.getMicroseconds() > edk.watch.Time.second){
				//passed one second at the time
				
				//decrement the microseconds
				clock.remove((long)edk.watch.Time.second);
				//decrement the second
				seconds--;
				
				//print the time
				System.out.printf("\nFPS == %d",frames);
				frames=0;
			}
			else {
				frames++;
				fps.waitForFPS();
			}
		}
		System.out.printf("\nEND\n");
	}
}
