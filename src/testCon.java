import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.util.Enumeration;
// RXTX
// import javax.comm.*; // SUN


public class testCon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Program started");
		
		//System.out.println(java.library.path);
	    CommPortIdentifier serialPortId;
	    //static CommPortIdentifier sSerialPortId;
	    Enumeration enumComm;
	    //SerialPort serialPort;

	    enumComm = CommPortIdentifier.getPortIdentifiers();
	    while (enumComm.hasMoreElements()) {
	     	serialPortId = (CommPortIdentifier) enumComm.nextElement();
	     	System.out.println(serialPortId.getName());
	     	if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	    		System.out.println(serialPortId.getName());
	    	}
	    }
	    CommPortIdentifier portIdentifier;
	    try {
			 portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0");
		if ( portIdentifier.isCurrentlyOwned() )
	    {
	        System.out.println("Error: Port is currently in use");
	    }
	    else
	    {
	        System.out.println("Connect 1/2");

		System.out.println("Finished successfully");

	    }
	} catch (NoSuchPortException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
}