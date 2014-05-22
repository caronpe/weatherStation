package communication;


import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import histogram.Histogramme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
	



	public class ArduinoJavaCommunication implements SerialPortEventListener {
		SerialPort serialPort;
	        /** The port we're normally going to use. */
		private static final String PORT_NAMES[] = { 
				"/dev/tty.usbserial-A9007UX1", // Mac OS X
	                        "/dev/ttyACM0", // Raspberry Pi
				"/dev/ttyUSB0", // Linux
				"COM3", // Windows
		};
		
		/**
		* A BufferedReader which will be fed by a InputStreamReader 
		* converting the bytes into characters 
		* making the displayed results codepage independent
		*/
		private BufferedReader input;
		/** The output stream to the port */
		private OutputStream output;
		/** Milliseconds to block while waiting for port open */
		private static final int TIME_OUT = 2000;
		/** Default bits per second for COM port. */
		private static final int DATA_RATE = 9600;
		/** connection with mysql database*/
		private Connection con;
		private Statement stmt;

		public void initialize() throws SQLException {
	                // the next line is for Raspberry Pi and 
	                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
	         System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

			CommPortIdentifier portId = null;
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

			//First, Find an instance of serial port as set in PORT_NAMES.
			while (portEnum.hasMoreElements()) {
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (String portName : PORT_NAMES) {
					if (currPortId.getName().equals(portName)) {
						portId = currPortId;
						break;
					}
				}
			}
			if (portId == null) {
				System.out.println("Could not find COM port.");
				return;
			}

			try {
				// open serial port, and use class name for the appName.
				serialPort = (SerialPort) portId.open(this.getClass().getName(),
						TIME_OUT);

				// set port parameters
				serialPort.setSerialPortParams(DATA_RATE,
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				// open the streams
				input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
				output = serialPort.getOutputStream();

				// add event listeners
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
			//Create the database connection
			this.databaseConnection();
			System.out.println("connection activé");
		}

		/**
		 * This should be called when you stop using the port.
		 * This will prevent port locking on platforms like Linux.
		 */
		public synchronized void close() {
			if (serialPort != null) {
				serialPort.removeEventListener();
				serialPort.close();
			}
		}

		/**
		 * Handle an event on the serial port. Read the data and print it.
		 */
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String inputLine=input.readLine();
					System.out.println(inputLine);
					this.dataStorage(inputLine);
				} catch (Exception e) {
					System.err.println(e.toString());
				}
				
				try {
					Histogramme.createHisto("temperature");
					Histogramme.createHisto("humidity");
					Histogramme.createHisto("light");
					Histogramme.createHisto("noise");
					Histogramme.createHisto("pressure");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
			// Ignore all the other eventTypes, but you should consider the other ones.
		}
		
		public void databaseConnection() throws SQLException{
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb" , "plockyn" , "theo");
			stmt = con.createStatement();			
		}
		
		public void dataStorage(String sentence) throws SQLException{
			String[] temperature, humidity, light, noise, atmosphericPressure;
			String[] data;
			//put data in table, each case like "temperature:10"
			data = sentence.split(";");
			temperature = data[0].split(":");
			humidity  = data[1].split(":");
			light = data[2].split(":");
			noise = data[3].split(":");
			atmosphericPressure  = data[4].split(":");
			//date
			SimpleDateFormat formater = null;
			Date aujourdhui = new Date();
			formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			//upload data in the mysqlDatabase
			String query = "INSERT INTO weather ( date , " +temperature[0]+" , " +humidity[0]+ " , " + light[0] + " , " + noise[0] + " , " + atmosphericPressure[0]+ 
					") VALUES ('" +formater.format(aujourdhui)+"' , '"+temperature[1]+"' , '" +humidity[1]+ "' , '" + light[1] + "' , '" + noise[1] + "' , '" + "1055" + "')";
			
			System.out.println(query);
			stmt.executeUpdate(query);
		}

		public static void main(String[] args) throws Exception {
			ArduinoJavaCommunication main = new ArduinoJavaCommunication();
			main.initialize();
			Thread t=new Thread() {
				public void run() {
					//the following line will keep this app alive for 1000 seconds,
					//waiting for events to occur and responding to them (printing incoming messages to console).
					try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
				}
			};
			t.start();
			System.out.println("Started");
		}
	}

