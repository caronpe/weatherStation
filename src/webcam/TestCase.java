package webcam;

import JMyron.*;

public class TestCase {

	public static void main (String[] args){
		JMyron myron = new JMyron();
		myron.start(300, 300);
		myron.update();
		int[] img = myron.image();
		System.out.println(img);
	}
}
