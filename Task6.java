import java.util.*;
import java.util.regex.*;

public class Task6 {
    public static void main(String[] args) {
        System.out.println("1. bell: " + bell(4));
        System.out.println("2.1 translateWord: " + translateWord("button"));
        System.out.println("2.2 translateSentense: " + translateSentense("I like to eat honey waffles."));
        System.out.println("3. validColor: " + validColor("rgba(0,0,0,0.1234567)"));
        System.out.println("4. stripUrlParams: " + stripUrlParams("https://edabit.com?a=1&b=2&a=2", "b"));
        System.out.println("5. getHashTags: " + getHashTags("Visualizing Science"));
        System.out.println("6. ulam: " + ulam(206));
        System.out.println("7. longestNonrepeatingSubstring: " + longestNonrepeatingSubstring("abcabcbb"));
        System.out.println("8. convertToRoman: " + convertToRoman(16));
        System.out.println("9. formula: " + formula("12+3=15=3*5"));
        System.out.println("10. palindromedescendant: " + palindromedescendant(11));
    }
    // число Белла
    public static int bell(int num) {
        int bellStart = 1; // началало новой строки в треугольнике Белла
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(bellStart); //  нулевая строка в треугольнике
        if(num < 2) { // исключение для чисел 0 и 1 - там число Белла равно 1
            return 1;
        }
        for(int i = 2; i <= num; i++) { // номер строки. номер строки = числу num
            list.add(bellStart); // начало новой строки - предыдущее число Белла
            for(int k = 1; k < i; k++) {
                list.add(list.get(list.size()-1)+list.get(list.size()-i)); // новое = предыдущее + кол-во всех предыдущих - номер строки, ибо в строке столько же чисел, какой ее номер
            }
            bellStart = list.get(list.size()-1);
        }
        return list.get(list.size()-1);
    }
    // поросячая латынь
    public static String translateWord(String word) {
        StringBuilder sb = new StringBuilder(word);
        if(sb.length() == 0) {
            return " ";
        } else {
            if("aouiyeAOUYIE".contains(String.valueOf(sb.charAt(0)))) {
                return sb.append("yay").toString();
            } else {
                while(true) {
                    if(!"aouiyeAOUYIE".contains(String.valueOf(sb.charAt(0)))) {
                        sb.append(sb.charAt(0));
                        sb.deleteCharAt(0);
                    } else {
                        sb.append("ay");
                        break;
                    }
                }
            }
        }
        return sb.toString();
    }
    public static String translateSentense(String line) {
        ArrayList<String> listLine = new ArrayList<String>(Arrays.asList(line.split("\\s")));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < listLine.size(); i++){
            String word = listLine.get(i);
            if("!,.?".contains(word.substring(word.length()-1))) {
                sb.append(translateWord(word.substring(0, word.length()-1)) + word.substring(word.length()-1) + " ");
            } else {
                sb.append(translateWord(word) + " ");
            }
        }
        return sb.toString();
    }
    // правильный формат цвета
    public static boolean validColor(String color) {
        Pattern p = Pattern.compile("\\d{1,3}");
        Pattern pRGBA = Pattern.compile("\\d\\.\\d++"); // паттерн для числа с точкой
        Matcher mm = pRGBA.matcher(color);
        Matcher m = p.matcher(color);
        int count = 0; // счетчик для RGB (A)
        if(color.matches("^rgb\\(\\d{1,3},\\d{1,3},\\d{1,3}\\)$")) {
            while(m.find()) {
                int num = Integer.parseInt(m.group());
                if(num > 255 || num < 0) {
                    return false;
                }
            }
        } else if(color.matches("^rgba\\(\\d{1,3},\\d{1,3},\\d{1,3},\\d\\.\\d++\\)$")) {
            if(mm.find()) {
                if (Double.parseDouble(mm.group()) > 1 || Double.parseDouble(mm.group()) < 0) { // сначала проверяем число с точкой
                    return false;
                } else {
                    while (m.find() && count < 3) { // проверяем три остальных числа
                        int num = Integer.parseInt(m.group());
                        if (num > 255 || num < 0) {
                            return false;
                        }
                        count++;
                    }
                }
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
        return true;
    }
    // url
    public static String stripUrlParams(String url, String ...paramsToStrip) {
        StringBuilder sb = new StringBuilder(url.substring(0, url.indexOf('?')+1));
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> exep = new ArrayList<String>(Arrays.asList(paramsToStrip));
        Pattern p = Pattern.compile("[a-z]=\\d++");
        Matcher m = p.matcher(url);
        if(url.contains("?")) {
            while(m.find()) {
                if(!list.contains(m.group().substring(0,1)) && paramsToStrip == null) {
                    list.add(m.group().substring(0,1));
                    sb.append(m.group()+"&");
                } else if(!list.contains(m.group().substring(0,1)) && !exep.contains(m.group().substring(0,1))) {
                    list.add(m.group().substring(0,1));
                    sb.append(m.group()+"&");
                }
            }
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }


    // три длиннейших
    public static List getHashTags(String line) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> lineList = new ArrayList<String>(Arrays.asList(line.split(" ")));
        for(String str : lineList) {
            StringBuilder sb = new StringBuilder(str.toLowerCase());
            // сначала чистим от ненужных знаков наши слова
            if((int) sb.charAt(sb.length()-1) < 97 || (int) sb.charAt(sb.length()-1) > 122) { // по ascii коду выясняем, на конце буква или любой другой знак
                sb.deleteCharAt(sb.length()-1); // если там лишний символ (не буква) - удаляем его
            }
            lineList.set(lineList.indexOf(str), sb.toString()); // возвращаем слово на его место
        }
        if(lineList.size() < 3) {
            switch (lineList.size()){
                case 1:
                    list.add("#" + line.toString());
                    return list;
                case 2:
                    if(lineList.get(0).length() > lineList.get(1).length()) {
                        list.add("#" + lineList.get(0));
                        list.add("#" + lineList.get(1));
                    } else {
                        list.add("#" + lineList.get(1));
                        list.add("#" + lineList.get(0));
                    }
                default:
                    break;
            }
        } else {
            int numberOfStr = 0; // счетчик чисел для вывода
            while(true) {
                int count = 0;
                for(String str : lineList) {
                    if(str.length() > count) {
                        count = str.length();
                        if(list.size() == numberOfStr+1) { // если до этого нашлось слово больше, а мы больше этого слова - удаляем старое и ставим новое
                            list.remove(numberOfStr);
                            list.add(str);
                        } else {
                            list.add(str); // если слова, больше нашего, не было еще - добавляем его
                        }
                    }
                }
                numberOfStr ++;
                if(numberOfStr == 3) {
                    break;
                } else {
                    lineList.remove(list.get(numberOfStr-1));
                }
            }
            list.set(0, "#" + list.get(0));
            list.set(1, "#" + list.get(1));
            list.set(2, "#" + list.get(2));
        }
        return list;
    }
    // число Улама
    public static int ulam(int number) {
        ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1,2));
        Set<Integer> s = new HashSet<Integer>();
        s.add(1);
        s.add(2);
        for(int i = 3; i < 10000; i++) {
            int count = 0;
            for(int j = 0; j < list.size(); j++) {
                if(s.contains(i-list.get(j)) && list.get(j) != (i - list.get(j))) { // проверка, может ли очередное i быть представлено как сумма двух имеющихся в массиве чисел
                    count++;
                }
                if(count > 2) {
                    break;
                }
            }
            if(count == 2) {
                list.add(i);
                s.add(i);
            }
        }
        return list.get(number-1);
    }

    // самая длинная подстрока из уникальных символов
    public static String longestNonrepeatingSubstring(String line) {
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for(char ch : line.toCharArray()) {
            if (list.isEmpty()) { // если список с символами пустой - просто добавляем очередной
                list.add(String.valueOf(ch));
            } else if (list.contains(String.valueOf(ch))) { // если элемент для данной строки не уникален
                if (sb.length() < list.size()) {
                    if (sb.length() == 0) { // если до этого наша строка была вовсе пустой - просто ее обновляем
                        for (String str : list) {
                            sb.append(str);
                        }
                        list.clear(); // чистим лист
                        list.add(String.valueOf(ch)); // добавляем наш символ
                    } else { // строка не была пустой
                        sb.delete(0, sb.length()); // чистим строку и далее, как в первом варианте
                        for (String str : list) {
                            sb.append(str);
                        }
                        list.clear();
                        list.add(String.valueOf(ch));
                    }
                } else if (sb.length() > list.size()) { // если старая строка длинее текущей - очищаем список, идем дальше
                    list.clear();
                    list.add(String.valueOf(ch));
                }
            } else { // если элемент уникальный для этой строки - добавляем его
                list.add(String.valueOf(ch));
            }
        }
        if(sb.length() < list.size()) {
            sb.delete(0, sb.length()); // чистим строку и далее, как в первом варианте
            for (String str : list) {
                sb.append(str);
            }
            list.clear();
        }
        return sb.toString();
    }
    // римские числа
    public static String convertToRoman(int num) {
        assert num < 3999 && num > 0;
        StringBuilder result = new StringBuilder();

        HashMap<Integer, String> constants = new HashMap<>(){{ // основа римского счисления
            put(1, "I");
            put(2, "II");
            put(3, "III");
            put(4, "IV");
            put(5, "V");
            put(6, "VI");
            put(7, "VII");
            put(8, "VIII");
            put(9, "IX");
            put(10, "X");
            put(50, "L");
            put(90, "XC");
            put(100, "C");
            put(500, "D");
            put(900, "CM");
            put(1000, "M");
        }};
        Stack<Integer> constantsKeys = new Stack<>(){{
            push(1);
            push(2);
            push(3);
            push(4);
            push(5);
            push(6);
            push(7);
            push(8);
            push(9);
            push(10);
            push(50);
            push(90);
            push(100);
            push(500);
            push(900);
            push(1000);
        }};

        while (num > 0) { // пока не переберем все число до конца
            int digit = constantsKeys.pop(); // получаем самый большой разряд из стэка
            if (digit > num) { // проверяем, больше ли наш раряд, чем наше число, если да - перебираем разряд дальше
                continue;
            }
            if (num < 10) { // если наше число меньше 10 - просто вернем заготовку из мапы
                result.append(constants.get(digit));
                break;
            } else {
                result.append(constants.get(digit).repeat(num / digit)); // получим обозначение старшего разряда и его значение
                num %= digit; // получим остаток без нашего пройденного разряда
            }
        }
        return result.toString();
    }
    // формула
    public static boolean formula(String line) {
        Pattern number = Pattern.compile("\\d++");
        Pattern act = Pattern.compile("[+-/*]");
        Pattern p = Pattern.compile("^\\d++[+-/*]\\d++=\\d++$");
        Matcher m = p.matcher(line);
        Matcher mN = number.matcher(line);
        Matcher mA = act.matcher(line);
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(line.matches("^\\d++[+-/*]\\d++=\\d++=\\d++[+-/*]\\d++$")) {
            return formula(line.substring(0,line.lastIndexOf('='))) && formula(line.substring(line.lastIndexOf('=')+1) + line.substring(line.indexOf('='),line.lastIndexOf('=')));
        }
        if(m.find()) {
            while(mN.find()) {
                list.add(Integer.parseInt(mN.group()));
            }
        }
        if(mA.find()) {
            if("+".equals(mA.group())) {
                return (list.get(0) + list.get(1) == list.get(2));
            } else if("-".equals(mA.group())) {
                return (list.get(0) - list.get(1) == list.get(2));
            } else if("*".equals(mA.group())) {
                return (list.get(0) * list.get(1) == list.get(2));
            } else if("/".equals(mA.group())) {
                return (list.get(0) / list.get(1) == list.get(2));
            } else {
                return false;
            }
        }
        return false;
    }
    // палиндром или любой из его потомков до 2х знаков
    public static boolean palindromedescendant(int number) {
        StringBuilder sb = new StringBuilder(String.valueOf(number));
        StringBuilder sbRever = new StringBuilder(String.valueOf(number));
        StringBuilder numberSB = new StringBuilder();
        ArrayList<String> list = new ArrayList<String>();
        while(sb.length() >= 2) {
            if(sb.equals(sbRever.reverse())) {
                break;
            } else {
                if(!numberSB.isEmpty()) {
                    numberSB.delete(0,numberSB.length());
                }
                for(char ch : sb.toString().toCharArray()) {
                    list.add(String.valueOf(ch));
                }
                for(int i = 0; i < list.size(); i++){
                    numberSB.append(String.valueOf(Integer.parseInt(list.get(i)) + Integer.parseInt(list.get(i+1))));
                    i++;
                }
                list.clear();
                sb = numberSB;
                sbRever = numberSB;
            }
        }
        return sb.equals(sbRever.reverse());
    }
}