import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cal {
  private static Scanner scan;

  /*
  *git first change
  *对正确表达式合并同类项
  *123
  */
  String expression(final String str) {
    String strTemp = str;
    String[] arr;
    if ( strTemp.charAt(0) != '+' ) {
      arr = strTemp.split("\\+");//按加号分割成各项
    } else {
      arr = strTemp.substring(1).split("\\+"); // 按加号分割成各项
    }
    int size1 = arr.length;
    int[] coef = new int[size1]; // coef记录每项系数
    for (int m = 0; m < coef.length; m++) {
      coef[m] = 1; // coef每项系数初始化为1
    }
    Set<String> var = new HashSet<String>(); // 记录多项式不同变量，用于计算个数
    /* 按*分割每项，记录变量及系数 */
    for (int j = 0; j < arr.length; j++) {
      String[] arr1 = arr[j].split("\\*");
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
    String[] var1 = new String[var.size()]; // 记录各变量
    int num = -1;
    for (Iterator<String> it = var.iterator(); it.hasNext();) {
      var1[++num] = it.next().toString();
    }
    int size2 = var.size();
    int[][] exp = new int[size1][size2]; // exp记录各小项var1中各变量系数，默认初始化为0
    for (int j = 0; j < arr.length; j++) {
      String[] arr1 = arr[j].split("\\*");
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
    String strnew = new String();
    for (int i = 0; i < size1; i++) {
      if (coef[i] != 0 && coef[i] != 1 && coef[i] != -1) {
        if (i == 0) {
          strnew = strnew + String.valueOf(coef[i]);
        } else {
          strnew = strnew + "+" + String.valueOf(coef[i]);
        }
        for (int j = 0; j < size2; j++) {
          if (exp[i][j] != 0 && exp[i][j] != 1) {
            strnew += "*" + var1[j] + "^" + exp[i][j];
          } else if (exp[i][j] == 1) {
            strnew += "*" + var1[j];
          }

        }
      } else if (coef[i] == 1 || coef[i] == -1) { // 处理省略系数为1||常数项为11
        int isNum = 1;
        for (int n = 0; n < size2; n++) {
          if (exp[i][n] != 0) {
            isNum = 0;
          }
        }
        if (isNum == 1) {
          if (i == 0) {
            strnew = strnew + String.valueOf(coef[i]);
          } else {
            strnew = strnew + "+" + String.valueOf(coef[i]);
          }
        } else {
          if (i != 0) {
            if (coef[i] == 1) {
              strnew = strnew + "+";
            } else if (coef[i] == -1) {
              strnew = strnew + "+-";
            }
          }

        }
        int isFirst = 1;
        for (int j = 0; j < size2; j++) {
          if (isFirst == 1) {       // 判断首字符
            if (exp[i][j] != 0 && exp[i][j] != 1) {
              strnew += var1[j] + "^" + exp[i][j];
              isFirst = 0;
            } else if (exp[i][j] == 1) {
              strnew += var1[j];
              isFirst = 0;
            }

          } else {
            if (exp[i][j] != 0 && exp[i][j] != 1) {
              strnew += "*" + var1[j] + "^" + exp[i][j];
            } else if (exp[i][j] == 1) {
              strnew += "*" + var1[j];
            }
          }

        }
      }
    }
    return strnew;
  }

  // 处理^
  String expressextra(String str) {
    Pattern r0 = Pattern.compile("([A-Za-z]+)\\^([0-9]+)");
    Matcher m0 = r0.matcher(str);
    while (m0.find()) {
      String[] arr = m0.group().split("\\^");
      String temp = arr[0];
      for (int i = 1; i < Integer.parseInt(arr[1]); i++) {
        temp += "*" + arr[0];
      }
      str = str.replace(m0.group(), temp);
    }
    return str;
  }

  // 求值，化简
  String simplify(String str, String strOld) {
    String strnew = " ";
    String[] arr = str.split(" |=");
    for (int i = 1; i < arr.length; i = i + 2) {
      if ((arr[i].charAt(0) >= 65 && arr[i].charAt(0) <= 90)
          || (arr[i].charAt(0) >= 97 && arr[i].charAt(0) <= 122)) {
        if (strOld.contains(arr[i])) {
          strOld = strOld.replace(arr[i], arr[i + 1]);
        } else {
          return "Error,no variable";
        }
      } else {
        return "Error,wrong variable";
      }
    }
    strnew = strOld;
    return strnew;
  }

  // 求导
  /**.
   * @param str 0
   * @param x0 0
   * @return 0
   */
  public static String derivation(String str, String x0) {
    String result;
    String fin;
    int len = str.length();
    char[] symbol = new char[len];
    fin = "";
    int start = 0;
    int end = 0;

    for (int i = 0; i < len; i++) {
      if (str.charAt(i) == '+' || str.charAt(i) == '-') {
        symbol[i] = str.charAt(i);
        end = i;
        result = str.substring(start, end);
        start = i + 1;
        if ( !result.equals("") ) {
          fin += der(result, x0);
        }
        fin += symbol[i];
      }
    }
    result = str.substring(start, str.length());
    fin += der(result, x0);
    return symbol(fin);
  }

  // Der 函数负责单项式的求导
  /**.
   * @param result 0
   * @param x0 0
   * @return 0
   */
  public static String der(String result, String x0) {
    String a0 = "";
    boolean power = false;
    String test = x0 + '^';
    int location = result.indexOf(test);

    if (location != -1) {
      power = true;
    }

    if (!power) {
      int start = 0;
      int end = 0;
      int num = 0;
      int elenum = 0;
      String[] element = new String[result.length()];
      for (int i = 0; i < result.length(); i++) {
        if (result.charAt(i) == '*') {
          end = i;
          element[elenum] = result.substring(start, end);
          elenum++;
          start = i + 1;
        }
      }

      element[elenum] = result.substring(start, result.length());
      elenum++;

      for (int i = 0; i < elenum; i++) {
        if (element[i].equals(x0)) {
          num++;
        }
      }

      if (num == 0) {
        return a0;
      } else {
        if (element[0].equals(x0)) {
          if (result.equals(x0)) {
            a0 += "1";
          } else {
            String finish = result.substring(element[0].length() + 1, result.length());
            a0 += String.valueOf(num);
            a0 += "*";
            a0 += finish;
          }
        } else {
          int local = result.indexOf(x0);
          if (local == result.length() - x0.length()) {

            String finstr = result.substring(0, local - 1);
            a0 += String.valueOf(num);
            a0 += "*";
            a0 += finstr;
          } else {
            String front = result.substring(0, local);
            a0 += String.valueOf(num);
            a0 += "*";
            a0 += front;
            String behind = result.substring(local + x0.length() + 1, result.length());
            a0 += behind;
          }
        }
        return a0;
      }
    } else {
      for (int i = location; i < result.length(); i++) {
        if (result.charAt(i) == '^') {
          location = i + 1;
          break;
        }
      }

      String powernum;
      int ioo;
      for (ioo = location; ioo < result.length(); ioo++) {
        if (result.charAt(ioo) >= '0' && result.charAt(ioo) <= '9') {
          ;
        } else {
          break;
        }
      }
      powernum = result.substring(location, ioo);
      int number = Integer.parseInt(powernum);
      a0 += String.valueOf(number);
      a0 += '*';
      number--;
      a0 += result.substring(0, location);
      a0 += String.valueOf(number);
      a0 += result.substring(ioo, result.length());

    }
    return a0;

  }

  // symbol 函数负责处理加减符号相连接的情况 比如将"+2*x+--+3x" 处理为"2*x+3x"
  /**.
   * @param fin 0
   * @return 0
   */
  public static String symbol(String fin) {
    while (fin.charAt(0) == '+' || fin.charAt(0) == '-') {
      fin = fin.substring(1, fin.length());
    }

    for (int i = 1; i < fin.length() - 1; i++) {
      if ((fin.charAt(i) == '+' || fin.charAt(i) == '-')
          && (fin.charAt(i + 1) == '+' || fin.charAt(i + 1) == '-')) {
        fin = fin.substring(0, i) + fin.substring(i + 1, fin.length());
        i--;
      }
    }
    if (fin.charAt(fin.length() - 1) == '+' || fin.charAt(fin.length() - 1) == '-') {
      fin = fin.substring(0, fin.length() - 1);
    }
    return fin;
  }

  /**.
   * @param args 0
   */
  public static void main(String[] args) {
    scan = new Scanner(System.in);
    String str;
    String str1 = " ";
    String strtemp;
    Cal poly = new Cal();
    while (true) {
      str = scan.nextLine();
      long startTime = System.currentTimeMillis();// 获取当前时间
      if (str.indexOf("!") == -1) {
        // 1.判断多项式给输出
        str = str.replaceAll(" ", ""); // 去空格
        str = str.replaceAll("  ", ""); // 去tab
        Pattern r0 = Pattern.compile(
            "^(((-?[0-9]+)|[A-Za-z]+)(\\+|-|\\*|\\^))?((([0-9]+)|[A-Za-z]+)"
            + "(\\+|-|\\*|\\^))+(([0-9]+)|[A-Za-z]+)$");
        Matcher m0 = r0.matcher(str);
        if (m0.matches()) {
          str = poly.expressextra(str);
          str = str.replaceAll("-", "+-1*");
          str = poly.expression(str);
          str1 = poly.expressextra(str);
          str1 = str1.replaceAll("\\+-", "+-1*");
          str = str.replaceAll("\\+-", "-");
          System.out.println(str);
        } else {
          System.out.println("Error polynomials");
        }
      } else {
        Pattern r0 = Pattern.compile("^!simplify(\\s+[A-Za-z]+=-?\\d+\\s*)*$");
        Matcher m0 = r0.matcher(str);
        Pattern r1 = Pattern.compile("^!d/d\\s+[A-Za-z]+$");
        Matcher m1 = r1.matcher(str);
        if (m0.matches()) {          //2.求值命令
          strtemp = poly.simplify(str, str1);
          strtemp = poly.expression(strtemp);
          strtemp = strtemp.replaceAll("\\+-", "-");
          System.out.println(strtemp);
        } else if (m1.matches()) {      //3.求导命令
          String[] vararr = str.split(" ");
          strtemp = str1.replaceAll("\\+-", "-");
          if (str1.contains(vararr[1])) {
            strtemp = Cal.derivation(strtemp, vararr[1]);
            strtemp = strtemp.replaceAll("-", "+-1*");
            strtemp = poly.expression(strtemp);
            strtemp = strtemp.replaceAll("\\+-", "-");
            System.out.println(strtemp);

          } else {
            System.out.println("Error,no variable");
          }
        } else {
          System.out.println("Error command");
        }
      }
      //测试时间
      /*long endTime = System.currentTimeMillis();
      System.out.println("start time:" + startTime + "ms");
      System.out.println("end   time:" + endTime + "ms");
      System.out.println("total time:" + (endTime - startTime) + "ms");*/
    }
  }
}