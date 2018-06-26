package util;

public class ShowTime {
	public static String setTime(String last) {
		String tmp;
		String[] s = last.split("：");
		int min = Integer.parseInt(s[0]);
		int sec = Integer.parseInt(s[1]);
		if (++sec == 60) {
			min++;
			sec = 0;
		}
		if (min < 10) {
			tmp = "0" + min + "：";
		} else {
			tmp = min + "：";
		}
		if (sec < 10) {
			tmp += "0" + sec;
		} else {
			tmp += sec;
		}

		return tmp;
	}

	public static String initTime() {
		return "00：00";
	}

	public static String getAllTime(String s1, String s2) {
		String[] m1 = s1.split("：");
		String[] m2 = s2.split("：");

		int min1 = Integer.parseInt(m1[0]);
		int sec1 = Integer.parseInt(m1[1]);
		int min2 = Integer.parseInt(m2[0]);
		int sec2 = Integer.parseInt(m2[1]);
		int tmp = sec1 + sec2;
		min1 += tmp / 60;
		sec1 = tmp % 60;
		min1 += min2;
		String re;
		if (min1 != 0) {
			re = min1 + "分 ";
		} else {
			re = "";
		}

		if (sec1 != 0) {
			re = re + sec1 + "秒";
		}

		return re;
	}
}