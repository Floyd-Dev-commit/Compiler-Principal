import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.Scanner;

public class LL1 {
    public static void main(String[] args) {
    	System.out.println("Please input String to be processed:");
        Test test = new Test();
        test.createNvNt_set();
        test.transformGrammar();
        test.Init();
        test.createTable();
        test.ouput();
        test.analyzeLL();
    }
}

class Test {
    //单个符号first集
    public HashMap<Character, HashSet<Character>> firstSet = new HashMap<>();
    //符号串first集
    public HashMap<String, HashSet<Character>> firstSetX = new HashMap<>();
    //follow集
    public HashMap<Character, HashSet<Character>> followSet = new HashMap<>();
    //非终结符
    public HashSet<Character> VnSet = new HashSet<>();
    //终结符
    public HashSet<Character> VtSet = new HashSet<>();
    //非终结符-产生式集合
    public HashMap<Character, ArrayList<String>> experssionSet = new HashMap<>();
    //预测分析表
    public String[][] table;
    //输入文法
    public String[] inputExperssion = {"E->Te", "e->ATe", "e->~", "T->Ft", "t->MFt", "t->~", "F->(E)", "F->i", "A->+", "A->-", "M->*", "M->/"};
    //开始符
    public char S = inputExperssion[0].charAt(0);
    //分析栈
    public Stack<Character> analyzeStatck = new Stack<>();
    //输入串
    Scanner Scan =new Scanner(System.in);
    String nextStr_fromBuffer=Scan.nextLine();
    public String strInput=nextStr_fromBuffer+"$";//末尾加上$表示结束
    public String action = "";
    int index = 0;

    public void Init() {
        //构造非终结符的first集
        for (char c : VnSet)
            getFirst(c);
        //构造开始符的follow集
        getFollow(S);
        //构造非终结符的follow集
        for (char c : VnSet)
            getFollow(c);
    }

     //先求非终结符，再求终结符
    public void createNvNt_set() {
        for (String e : inputExperssion)
            VnSet.add(e.split("->")[0].charAt(0));
        for (String e : inputExperssion)
            for (char c : e.split("->")[1].toCharArray())
                if (!VnSet.contains(c))
                    VtSet.add(c);
    }

    //改造文法
    public void transformGrammar() {
        for (String e : inputExperssion) {
            String[] str = e.split("->");
            char c = str[0].charAt(0);
            ArrayList<String> list = experssionSet.containsKey(c) ? experssionSet.get(c) : new ArrayList<>();
            list.add(str[1]);
            experssionSet.put(c, list);
        }
        Object[] VnArray = VnSet.toArray();
        for (int i = 0; i < VnArray.length; i++) {
            for (int j = 0; j < i; j++) {
                for (String e : inputExperssion) {
                    String[] str = e.split("->");
                    char ch = str[0].charAt(0);
                    String s = str[1];
                    if (VnArray[i].equals(ch)) {
                        if (s.substring(0, 1).equals(VnArray[j].toString())) {
                            for (String e1 : inputExperssion) {
                                String[] str1 = e1.split("->");
                                char ch1 = str1[0].charAt(0);
                                String s1 = str1[1];
                                if (VnArray[j].equals(ch1)) {
                                    String str2 = s.substring(1);
                                    ArrayList<String> list = experssionSet.containsKey(ch) ? experssionSet.get(ch) : new ArrayList<>();
                                    list.add(s1 + str2);
                                    list.remove(s);
                                    experssionSet.put(ch, list);
                                }
                            }
                        }
                    }
                }
            }
        }

        HashMap<Character, ArrayList<String>> experssionSetTemp1 = new HashMap<>();
        experssionSetTemp1 = clone(experssionSet);
        HashSet<Character> VnSetTemp = new HashSet<>();
        VnSetTemp = clone(VnSet);

        for (int i = 0; i < VnArray.length; i++) {
            int flag = 0;
            for (char ch : VnSet){
                for (String s : experssionSet.get(ch)){
                    if (VnArray[i].equals(ch)){
                        if (ch == s.charAt(0)){
                            flag++;
                        }
                    }
                }
            }
            if (flag == 0) continue;
            for (char ch : VnSet){
                for (String s : experssionSet.get(ch)){
                    if (VnArray[i].equals(ch)){
                        if (ch == s.charAt(0)){
                            String str = s.substring(1);
                            ArrayList<String> listT = experssionSetTemp1.containsKey(ch) ? experssionSetTemp1.get(ch) : new ArrayList<>();
                            listT.remove(s);
                            experssionSetTemp1.put(ch, listT);
                            ArrayList<String> list = experssionSetTemp1.containsKey(Character.toLowerCase(ch)) ? experssionSetTemp1.get(Character.toLowerCase(ch)) : new ArrayList<>();
                            list.add(str + Character.toLowerCase(ch));
                            experssionSetTemp1.put(Character.toLowerCase(ch), list);
                            ArrayList<String> list1 = experssionSetTemp1.containsKey(Character.toLowerCase(ch)) ? experssionSetTemp1.get(Character.toLowerCase(ch)) : new ArrayList<>();
                            list1.add("~");
                            experssionSetTemp1.put(Character.toLowerCase(ch), list);
                            VnSetTemp.add(Character.toLowerCase(ch));
                        }else{
                            ArrayList<String> list = experssionSetTemp1.containsKey(ch) ? experssionSetTemp1.get(ch) : new ArrayList<>();
                            list.remove(s);
                            list.add(s + Character.toLowerCase(ch));
                            experssionSetTemp1.put(ch, list);
                        }
                    }
                }
            }
        }

        System.out.println("\nTransform the Grammar to:");
        for (char c : VnSetTemp)
            for (String s : experssionSetTemp1.get(c))
                System.out.println(c + "->" + s);
        VnSet = clone(VnSetTemp);
        experssionSet = clone(experssionSetTemp1);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clonedObj;
    }

    public void getFirst(char c) {
        if (firstSet.containsKey(c))
            return;
        HashSet<Character> set = new HashSet<>();
        // 若c为终结符，直接添加
        if (VtSet.contains(c)) {
            set.add(c);
            firstSet.put(c, set);
            return;
        }
        // c为非终结符号，处理其每条产生式
        for (String s : experssionSet.get(c)) {
            if ("~".equals(c)) {
                set.add('~');
            } else {
                for (char cur : s.toCharArray()) {
                    if (!firstSet.containsKey(cur))
                        getFirst(cur);
                    HashSet<Character> curFirst = firstSet.get(cur);
                    set.addAll(curFirst);
                    if (!curFirst.contains('~'))
                        break;
                }
            }
        }
        firstSet.put(c, set);
    }

    public void getFirst(String s) {
        if (firstSetX.containsKey(s))
            return;
        HashSet<Character> set = new HashSet<>();
        // 从左往右扫描该式
        int i = 0;
        while (i < s.length()) {
            char cur = s.charAt(i);
            if (!firstSet.containsKey(cur))
                getFirst(cur);
            HashSet<Character> rightSet = firstSet.get(cur);
            // 将其非空first集加入左部
            set.addAll(rightSet);
            // 若包含空串 处理下一个符号
            if (rightSet.contains('~'))
                i++;
            else
                break;
            // 若到了尾部 即所有符号的first集都包含空串 把空串加入fisrt集
            if (i == s.length()) {
                set.add('~');
            }
        }
        firstSetX.put(s, set);
    }


    //求follow集
    public void getFollow(char c) {
        ArrayList<String> list = experssionSet.get(c);
        HashSet<Character> leftFollowSet = followSet.containsKey(c) ? followSet.get(c) : new HashSet<>();
        //如果是开始符 添加 $
        if (c == S)
            leftFollowSet.add('$');
        //查找输入的所有产生式，添加c的后跟终结符
        for (char ch : VnSet)
            for (String s : experssionSet.get(ch))
                for (int i = 0; i < s.length(); i++)
                    if (c == s.charAt(i) && i + 1 < s.length() && VtSet.contains(s.charAt(i + 1)))
                        leftFollowSet.add(s.charAt(i + 1));
        followSet.put(c, leftFollowSet);
        //反向扫描处理c的每一条产生式
        for (String s : list) {
            int i = s.length() - 1;
            while (i >= 0) {
                char cur = s.charAt(i);
                //只处理非终结符
                if (VnSet.contains(cur)) {
                    //1.若β不存在   followA 加入 followB
                    //2.若β存在，把β的first集非空符号加入followB
                    //3.若β存在  且first(β)包含空串  followA 加入 followB
                    String right = s.substring(i + 1);
                    HashSet<Character> rightFirstSet;
                    if(!followSet.containsKey(cur))
                        getFollow(cur);
                    HashSet<Character> curFollowSet = followSet.get(cur);
                    //followA加入followB
                    if (right.length() == 0) {
                        curFollowSet.addAll(leftFollowSet);
                    } else {
                        if (right.length() == 1) {
                            if (!firstSet.containsKey(right.charAt(0)))
                                getFirst(right.charAt(0));
                            rightFirstSet = firstSet.get(right.charAt(0));
                        } else {
                            if (!firstSetX.containsKey(right))
                                getFirst(right);
                            rightFirstSet = firstSetX.get(right);
                        }
                        for (char var : rightFirstSet)
                            if (var != '~')
                                curFollowSet.add(var);
                        // 若first(β)包含空串,将followA加入followB
                        if (rightFirstSet.contains('~'))
                            curFollowSet.addAll(leftFollowSet);
                    }
                    followSet.put(cur, curFollowSet);
                }
                i--;
            }
        }
    }

    //建立预测分析表
    public void createTable() {
        Object[] VtArray = VtSet.toArray();
        Object[] VnArray = VnSet.toArray();
        table = new String[VnArray.length + 1][VtArray.length + 1];
        table[0][0] = " ";
        for (int i = 0; i < VtArray.length; i++)
            table[0][i + 1] = (VtArray[i].toString().charAt(0) == '~') ? "$" : VtArray[i].toString();
        for (int i = 0; i < VnArray.length; i++)
            table[i + 1][0] = VnArray[i] + "";
        for (int i = 0; i < VnArray.length; i++)
            for (int j = 0; j < VtArray.length; j++)
                table[i + 1][j + 1] = " ";
        for (char A : VnSet) {
            for (String s : experssionSet.get(A)) {
                if (!firstSetX.containsKey(s))
                    getFirst(s);
                HashSet<Character> set = firstSetX.get(s);
                for (char a : set)
                    insert(A, a, s);
                if (set.contains('~')) {
                    HashSet<Character> setFollow = followSet.get(A);
                    if (setFollow.contains('$'))
                        insert(A, '$', s);
                    for (char b : setFollow)
                        insert(A, b, s);
                }
            }
        }
    }

    //分析过程
    public void analyzeLL() {
        System.out.println();
        System.out.println("Analysis Process as follow:");
        System.out.println("               Stack         Input              Output");
        analyzeStatck.push('$');
        analyzeStatck.push(S);
        displayLL();
        char X = analyzeStatck.peek();
        while (X != '$') {
            char a = strInput.charAt(index);
            if (X == a) {
                action = "match " + analyzeStatck.peek();
                analyzeStatck.pop();
                index++;
            } else if (VtSet.contains(X)) {
                System.out.println("Error:Unexpected LL(1) String!");
                return;
            }
            else if (find(X, a).equals(" ")) {
                System.out.println("Error:Unexpected LL(1) String!");
                return;
            }
            else if (find(X, a).equals("~")) {
                analyzeStatck.pop();
                action = X + "->~";
            } else {
                String str = find(X, a);
                if (str != "") {
                    action = X + "->" + str;
                    analyzeStatck.pop();
                    int len = str.length();
                    for (int i = len - 1; i >= 0; i--)
                        analyzeStatck.push(str.charAt(i));
                } else {
                    System.out.println("Error:Unexpected LL(1) String!");
                    return;
                }
            }
            X = analyzeStatck.peek();
            displayLL();
        }
        System.out.println("\nSuccess!");
    }


    public String find(char X, char a) {
        for (int i = 0; i < VnSet.size() + 1; i++) {
            if (table[i][0].charAt(0) == X)
                for (int j = 0; j < VtSet.size() + 1; j++) {
                    if (table[0][j].charAt(0) == a)
                        return table[i][j];
                }
        }
        return "";
    }

    public void insert(char X, char a, String s) {
        if (a == '~') a = '$';
        for (int i = 0; i < VnSet.size() + 1; i++) {
            if (table[i][0].charAt(0) == X) {
                for (int j = 0; j < VtSet.size() + 1; j++) {
                    if (table[0][j].charAt(0) == a) {
                        if (!(table[i][j].equals(" ") || table[i][j].equals(s))){
                            System.out.println("Not LL1 Grammar!");
                            return;
                        }
                        table[i][j] = s;
                        return;
                    }
                }
            }
        }
    }

    // 输出分析过程
    public void displayLL() {
        Stack<Character> s = analyzeStatck;
        System.out.printf("%20s", s);
        System.out.printf("%15s", strInput.substring(index));
        System.out.printf("%17s", action);
        System.out.println();
    }

    public void ouput() {
        System.out.println("\nFirst-Set as follow:");
    	//first集
        for (Character c : VnSet) {
            HashSet<Character> set = firstSet.get(c);
            System.out.printf("first(" + c + "):");
            for (Character var : set)
                System.out.print(var + " ");
            System.out.println();
        }

        System.out.println("\nFollow-Set as follow:");
        //follow集
        for (Character c : VnSet) {
            HashSet<Character> set = followSet.get(c);
            System.out.print("follow(" + c + "):");
            for (Character var : set)
                System.out.print(var + " ");
            System.out.println();
        }
        System.out.println();

        //预测分析表
        System.out.println("LL(1) Analysis Table as follow:");
        for (int i = 0; i < VnSet.size() + 1; i++) {
            for (int j = 0; j < VtSet.size() + 1; j++) {
                System.out.printf("%6s", table[i][j] + " ");
            }
            System.out.println();
        }
    }

}