package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RedeHistory {
	public static void main(String[] args) {
		File f = new File("Hex.txt");
		try {
			Scanner input = new Scanner(f);
			String s = input.nextLine();
			String[] t = s.split("  ");
			System.out.println(t.length);
			for (int i = 0; i <= t.length / 2; i++) {
				System.out.println(t[i * 2]);
				int m, n;
				String[] tmp = t[i * 2].split("坐标");
				tmp = tmp[1].split(" ");
				m = Integer.parseInt(tmp[0]);
				n = Integer.parseInt(tmp[1]);
				System.out.println(m + " " + n);
			}
		
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}