#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <vector>
#include <string>

using namespace std;

class Gram
{
    public:
    Gram(string a, string b)
    {
        left = a;
        right = b;
    }
    string left;
    string right;
};

vector<Gram> process;//保存最左推导过程

string str;
int index = 0;
int flag1 = 1, flag2 = 1;

void input();
void E();                //E->TE'
void E1();               //E'->ATE'|ε
void T();                //T->FT'
void T1();               //T'->MFT'|ε
void F();                //F->(E)|i
void A();                //A->+|-
void M();                //M->*|/
void Output(vector<Gram> process);//输出推导过程

int main() {
    input();
    cout << endl<<"递归分析过程如下" << endl;
    E();
    if (index != str.length()) cout <<"非法字符串，合法的句子"<<str.substr(0,index)<<"后符号串"<<str.substr(index) <<"无法继续推导"<<endl<<endl<<"合法部分的最左推导如下"<<endl;
    else cout<<endl<<"合法字符串"<<endl << endl << "最左推导过程如下" << endl;
    Output(process);
    return 0;
}

void input() {
    int i, len;
    string t1, t;
    while (getline(cin,t1)) {
        t+=t1;
    }
    len = t.length();
    for (i = 0; i < len; i++) {
        if (t[i] == '2' && t[i + 1] == ',') {
            str+="i";
        }
        if (t[i] == '4' && t[i + 1] == ',') {
            str += t[i + 2];
            if (t[i + 3] != '>') {
                str += t[i + 3];
            }
            else if (t[i + 4] == '>') {
                str += t[i + 3];
            }
        }
    }
    cout <<endl<<"您输入的待分析串形式化为"<<str <<"$"<< endl;
}

void E() {
    printf("E->TE'\n");
    process.push_back(Gram("E","Te"));
    
    T();
    E1();
}

void E1() {
    printf("E'->ATE'\n");
    process.push_back(Gram("e","ATe"));
    A();
    if (!flag1)
    {
        printf("待分析的下一个符号%c不属于First(A)={+,-},回溯\nE'->空串\n", str[index] == '\0' ? '$' : str[index]);
        process.pop_back();
        process.push_back(Gram("e",""));
        flag1 = 1;
        return;
    }
    T();
    E1();
}

void T() {
    printf("T->FT'\n");
    process.push_back(Gram("T", "Ft"));
    F();
    T1();
}

void T1() {
    printf("T'->MFT'\n");
    process.push_back(Gram("t", "MFt"));
    M();
    if (!flag2)
    {
        printf("待分析的下一个符号%c不属于First(M)={*,/},回溯\nT'->空串\n",str[index]=='\0'?'$':str[index]);
        process.pop_back();
        process.push_back(Gram("t", ""));
        flag2 = 1;
        return;
    }
    F();
    T1();
}

void F() {
    if (str[index] == 'i') {
        printf("F->i\n");
        process.push_back(Gram("F", "i"));
        index++;
    }
    else if (str[index] == '(') {
        printf("F->(E)\n");
        process.push_back(Gram("F", "(E)"));
        index++;
        E();
        if (str[index] == ')') {
            index++;
        }
        else {
            printf("非法字符串,终结符号%c与文法F->(E)中)不匹配\n", str[index] == '\0' ? '$' : str[index]);
            exit(0);
        }
    }
    else {
        printf("非法字符串,终结符号%c与文法F->(E)|i中(、i均不匹配\n", str[index] == '\0' ? '$' : str[index]);
        exit(0);
    }
}

void A() {
    if (str[index] == '+') {
        printf("A->+\n");
        process.push_back(Gram("A", "+"));
        index++;
    }
    else if (str[index] == '-') {
        printf("A->-\n");
        process.push_back(Gram("A", "-"));
        index++;
    }
    else {
        flag1 = 0;
    }
}

void M() {
    if (str[index] == '*') {
        printf("M->*\n");
        process.push_back(Gram("M", "*"));
        index++;
    }
    else if (str[index] == '/') {
        printf("M->/\n");
        process.push_back(Gram("M", "/"));
        index++;
    }
    else {
        flag2 = 0;
    }
}

string Recover(string now)//e,t还原为E',T'
{
    int nowLen = now.length();
    for (int i = 0; i < nowLen; i++)
    {
        if (now[i] == 'e' || now[i] == 't')
        {
            now[i] -= 32;
            now.insert(now.begin()+i+1,'\'');
            nowLen++;
        }
    }
    return now;
}

void Output(vector<Gram> process)//输出分析过程
{
    int Len = str.length();
    string now = "E";
    cout<<now;
    int steps = process.size();
    for (int i = 0; i < steps;i++)
    {
        cout << endl;
        int index = now.find(process[i].left);
        now = now.substr(0, index) + process[i].right + now.substr(index + process[i].left.length());
        cout << "=>" << Recover(now);
    }
}
