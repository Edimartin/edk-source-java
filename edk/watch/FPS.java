/*
Library FPS - Control frames per second.
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
import edk.watch.Time;

public class FPS {
    /**
     *  FPS user a clock to control the how much time are the FPS.
     */
    private edk.watch.Time clock;

    /**
     *  The frames set how much times the code will be processed in one second.
     */
    private int frames;

    //
    public FPS(){
    	this.clock = new edk.watch.Time();
    	this.setFPS(1);
    	this.start();
    }

    public FPS(int frames){
    	this.clock = new edk.watch.Time();
    	this.setFPS(frames);
    	this.start();
    }


    public void start(){
    	//run start in the time
    	this.clock.start();
    }

    public void setFPS(int frames){
    	//set the fps
    	if(frames>=0) {
    		this.frames=frames;
    	}
    	else {
    		this.frames=0;
    	}
    }

    public int getFPS(){
    	return this.frames;
    }

    public boolean waitForFPS(){
    	boolean ret=false;
        if(this.frames>0){
            //calculate the wait time in microsecons
        	int wait=(int)(((1.0f/(float)frames)*edk.watch.Time.second) - (float)this.clock.getMicroseconds());
            if(wait>0){
                //If the distance bigger than zero. He need wait.
                edk.watch.Time.sleepProcessMicroseconds(wait);
                ret=true;
            }

            //after this he can start a new time count
            start();
        }
        return ret;
    }

}
