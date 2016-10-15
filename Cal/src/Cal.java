import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cal {
	/*
	 *git first change
	 * 对正确表达式合并同类项
	 */
	String expression(String str) {
		String[] arr;
		if(str.charAt(0)!='+'){
			 arr = str.split("\\+"); // 按加号分割成各项
		}else{
			 arr = str.substring(1).split("\\+"); // 按加号分割成各项
		}	
		int size1 = arr.length;
		int coef[] = new int[size1]; // coef记录每项系数
		for (int m = 0; m < coef.length; m++) {
			coef[m] = 1; // coef每项系数初始化为1
		}
		Set var = new HashSet(); // 记录多项式不同变量，用于计算个数
		/* 按*分割每项，记录变量及系数 */
		for (int j = 0; j < arr.length; j++) {
			String arr1[] = arr[j].split("\\*");
			Pattern pattern = Pattern.compile("^-?\\d+$");
			for (int i = 0; i < arr1.length; i++) {
				Matcher isNum = pattern.matcher(arr1[i]);
				if (!isNum.matches()) {
					var.add(arr1[i]);
				} else {
					coef[j] *= Integer.parseInt(arr1[i]);
				}
			}
		}
		String var1[] = new String[var.size()]; // 记录各变量
		int num = -1;
		for (Iterator it = var.iterator(); it.hasNext();) {
			var1[++num] = it.next().toString();
		}
		int size2 = var.size();
		int exp[][] = new int[size1][size2]; // exp记录各小项var1中各变量系数，默认初始化为0
		for (int j = 0; j < arr.length; j++) {
			String arr1[] = arr[j].split("\\*");
			for (int i = 0; i < arr1.length; i++) {
				for (int m = 0; m < var.size(); m++) {
					if (arr1[i].equals(var1[m])) {
						exp[j][m]++;
					}
				}
			}
		}
		/* 合并同类项 */
		for (int i = size1 - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				int flag = 1;
				for (int m = 0; m < size2; m++) {
					if (exp[i][m] != exp[j][m]) {
						flag = 0;
					}
				}
				if (flag == 1) {
					coef[j] += coef[i];
					coef[i] = 0;
				}
			}
		}
		/* 恢复合并同类项后多项式 */
		String str_new = new String();
		for (int i = 0; i < size1; i++) {
			if (coef[i] != 0 && coef[i] != 1 && coef[i] != -1) {
				if (i == 0){
					str_new = str_new + String.valueOf(coef[i]);
				}		
				else
					str_new = str_new + "+" + String.valueOf(coef[i]);
				for (int j = 0; j < size2; j++) {
					if (exp[i][j] != 0 && exp[i][j] != 1)
						str_new += "*" + var1[j] + "^" + exp[i][j];
					else if (exp[i][j] == 1)
						str_new += "*" + var1[j];

				}
			} else if (coef[i] == 1 || coef[i] == -1) { // 处理省略系数为1||常数项为11
				int isNum = 1;
				for (int n = 0; n < size2; n++) {
					if (exp[i][n] != 0)
						isNum = 0;
				}
				if (isNum == 1) {
					if (i == 0)
						str_new = str_new + String.valueOf(coef[i]);
					else
						str_new = str_new + "+" + String.valueOf(coef[i]);
				} else {
					if (i != 0) {
						if (coef[i] == 1)
							str_new = str_new + "+";
						else if (coef[i] == -1)
							str_new = str_new + "+-";
					}

				}
				int isFirst = 1;
				for (int j = 0; j < size2; j++) {
					if (isFirst == 1) { 			// 判断首字符
						if (exp[i][j] != 0 && exp[i][j] != 1) {
							str_new += var1[j] + "^" + exp[i][j];
							isFirst = 0;
						} else if (exp[i][j] == 1) {
							str_new += var1[j];
							isFirst = 0;
						}

					} else {
						if (exp[i][j] != 0 && exp[i][j] != 1)
							str_new += "*" + var1[j] + "^" + exp[i][j];
						else if (exp[i][j] == 1)
							str_new += "*" + var1[j];
					}

				}
			}
		}
		return str_new;
	}

	// 处理^
	String expressextra(String str) {
		Pattern r = Pattern.compile("([A-Za-z]+)\\^([0-9]+)");
		Matcher m = r.matcher(str);
		while (m.find()) {
			String arr[] = m.group().split("\\^");
			String temp = arr[0];
			for (int i = 1; i < Integer.parseInt(arr[1]); i++)
				temp += "*" + arr[0];
			str = str.replace(m.group(), temp);
		}
		return str;
	}

	// 求值，化简
	String simplify(String str, String str_old) {
		String str_new = " ";
		String arr[] = str.split(" |=");
		for (int i = 1; i < arr.length; i = i + 2) {
			if ((arr[i].charAt(0) >= 65 && arr[i].charAt(0) <= 90)
					|| (arr[i].charAt(0) >= 97 && arr[i].charAt(0) <= 122)) {
				if (str_old.contains(arr[i])) {
					str_old = str_old.replace(arr[i], arr[i + 1]);
				} else
					return "Error,no variable";
			} else
				return "Error,wrong variable";
		}
		str_new = str_old;
		return str_new;
	}

	// 求导
	public static String Derivation(String str, String x) {
		String result;
		String fin;
		int len = str.length();
		char[] symbol = new char[len];
		fin = "";
		int start, end;
		start = 0;
		end = 0;

		for (int i = 0; i < len; i++) 
		{
			if (str.charAt(i) == '+' || str.charAt(i) == '-') 
			{
				symbol[i] = str.charAt(i);
				end = i;
				result = str.substring(start, end);
				start = i + 1;
				if(!result.equals(""))
					fin += Der(result, x);
				fin += symbol[i];
			}
		}
		result = str.substring(start, str.length());
		fin += Der(result, x);
		return symbol(fin);
	}

	// Der 函数负责单项式的求导
	public static String Der(String result, String x) {
		String a = "";
		boolean power = false;
		String test = x + '^';
		int location = result.indexOf(test);

		if (location != -1)
			power = true;

		if (!power) {
			int start = 0, end = 0;
			int num = 0, ele_num = 0;
			String[] element = new String[result.length()];
			for (int i = 0; i < result.length(); i++) {
				if (result.charAt(i) == '*') {
					end = i;
					element[ele_num] = result.substring(start, end);
					ele_num++;
					start = i + 1;
				}
			}

			element[ele_num] = result.substring(start, result.length());
			ele_num++;

			for (int i = 0; i < ele_num; i++) {
				if (element[i].equals(x))
					num++;
			}

			if (num == 0)
				return a;
			else {
				if (element[0].equals(x)) {
					if (result.equals(x)) {
						a += "1";
					} else {
						String finish = result.substring(element[0].length() + 1, result.length());
						a += String.valueOf(num);
						a += "*";
						a += finish;
					}
				} else {
					int local = result.indexOf(x);
					if (local == result.length() - x.length()) {

						String finstr = result.substring(0, local - 1);
						a += String.valueOf(num);
						a += "*";
						a += finstr;
					} else {
						String front = result.substring(0, local);
						String behind = result.substring(local + x.length() + 1, result.length());
						a += String.valueOf(num);
						a += "*";
						a += front;
						a += behind;
					}
				}
				return a;
			}
		} else {
			for (int i = location; i < result.length(); i++) {
				if (result.charAt(i) == '^') {
					location = i + 1;
					break;
				}
			}

			String power_num;
			int ioo;
			for (ioo = location; ioo < result.length(); ioo++) {
				if (result.charAt(ioo) >= '0' && result.charAt(ioo) <= '9')
					;
				else
					break;
			}
			power_num = result.substring(location, ioo);
			int number = Integer.parseInt(power_num);
			a += String.valueOf(number);
			a += '*';
			number--;
			a += result.substring(0, location);
			a += String.valueOf(number);
			a += result.substring(ioo, result.length());

		}
		return a;

	}

	// symbol 函数负责处理加减符号相连接的情况 比如将"+2*x+--+3x" 处理为"2*x+3x"
	public static String symbol(String fin) {
		while (fin.charAt(0) == '+' || fin.charAt(0) == '-')
			fin = fin.substring(1, fin.length());

		for (int i = 1; i < fin.length() - 1; i++) {
			if ((fin.charAt(i) == '+' || fin.charAt(i) == '-')
					&& (fin.charAt(i + 1) == '+' || fin.charAt(i + 1) == '-')) {
				fin = fin.substring(0, i) + fin.substring(i + 1, fin.length());
				i--;
			}
		}
		if (fin.charAt(fin.length() - 1) == '+' || fin.charAt(fin.length() - 1) == '-')
			fin = fin.substring(0, fin.length() - 1);
		return fin;
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String str, str1 = " ", str_temp;
		Cal poly = new Cal();
		while (true) {
			str = scan.nextLine();
			long startTime = System.currentTimeMillis();// 获取当前时间
			if (str.indexOf("!") == -1) {
				// 1.判断多项式给输出
				str = str.replaceAll(" ", ""); // 去空格
				str = str.replaceAll("	", ""); // 去tab
				Pattern r0 = Pattern.compile(
						"^(((-?[0-9]+)|[A-Za-z]+)(\\+|-|\\*|\\^))?((([0-9]+)|[A-Za-z]+)(\\+|-|\\*|\\^))+(([0-9]+)|[A-Za-z]+)$");
				Matcher m0 = r0.matcher(str);
				if (m0.matches()) {
					str = poly.expressextra(str);
					str = str.replaceAll("-", "+-1*");
					str = poly.expression(str);
					str1 = poly.expressextra(str);
					str1 = str1.replaceAll("\\+-", "+-1*");
					str = str.replaceAll("\\+-", "-");
					System.out.println(str);
				} else
					System.out.println("Error polynomials");
			} else {
				Pattern r = Pattern.compile("^!simplify(\\s+[A-Za-z]+=-?\\d+\\s*)*$");
				Matcher m = r.matcher(str);
				Pattern r1 = Pattern.compile("^!d/d\\s+[A-Za-z]+$");
				Matcher m1 = r1.matcher(str);
				if (m.matches()) {					//2.求值命令
					str_temp = poly.simplify(str, str1);
					str_temp = poly.expression(str_temp);
					str_temp = str_temp.replaceAll("\\+-", "-");
					System.out.println(str_temp);
				} else if (m1.matches()) {			//3.求导命令
					String var_arr[] = str.split(" ");
					str_temp = str1.replaceAll("\\+-", "-");
					if (str1.contains(var_arr[1])) {
						str_temp = poly.Derivation(str_temp, var_arr[1]);
						str_temp = str_temp.replaceAll("-", "+-1*");
						str_temp = poly.expression(str_temp);
						str_temp = str_temp.replaceAll("\\+-", "-");
						System.out.println(str_temp);

					} else {
						System.out.println("Error,no variable");
					}
				} else
					System.out.println("Error command");
			}
			//测试时间
			/*long endTime = System.currentTimeMillis();
			System.out.println("start time:" + startTime + "ms");
			System.out.println("end   time:" + endTime + "ms");
			System.out.println("total time:" + (endTime - startTime) + "ms");*/
		}
	}
}