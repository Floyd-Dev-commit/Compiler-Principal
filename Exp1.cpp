#include <iostream>
#include <algorithm>
#include <string>
#include <vector>

using namespace std;

string key_word[11] = { "begin","end","if","then","else","for","while","do","and","or","not"};

bool Symbol(char c)
{
	if (c == '+' || c == '-' || c == '*' || c == '/' || c == '>' || c == '<'
		|| c == '=' || c == ':' || c == '+' || c == '-' || c == '(' || c == ')'
		|| c == ';' || c == '#') return true;
	else return false;
}

int isKeyWord(string str)//判断字符串是否为关键词
{
	for (int i = 0; i < 11; i++)
		if (key_word[i]==str) return i;
	return -1;
}

void Output(int number, string str, int mode = 0)//输出分析结果
{
	cout << "<" << number << "," << str << ">";
	if (!mode) cout << endl;
}

void Lex(string input)
{
	int len = input.length();
	for (int i = 0; i < len;)
	{
		if (isalpha(input[i])||input[i]=='_')//字母或下划线开头，可能是标识符或者关键词
		{
			int first = i;
			while (isalpha(input[i]) || isdigit(input[i])||input[i]=='_') i++; //遍历字符串直到遇到非字母非数字非下划线
			int end = i - 1;
			string temp = input.substr(first, end-first+1);
			int number = isKeyWord(temp);		//判断temp是否为关键词，并根据结果确定其为标识符还是关键字
			if (number == -1)
			{
				Output(40, temp,1);
				string temp2 = temp;
				transform(temp2.begin(), temp2.end(), temp2.begin(), ::tolower);//temp2小写，比对保留字，如果仅大小写不一致，输出警告。
				if (isKeyWord(temp2) != -1)
				{
					cout << " " << "Warning:" <<"标识符"<<temp << "与保留字" << temp2 << "仅大小写不相同" << endl;
				}
				else cout << endl;
			}
			else Output(number, temp);
		}
		else if (isdigit(input[i]))//数字开头，可能是数字串，也可能是不合法的标识符
		{
			int first = i;
			while (isdigit(input[i])) i++;//遍历完整个数字串，直到遇到非数字
			int end = i - 1;
			string temp = input.substr(first, end - first + 1);
			/*不满足数字串条件的非法标识符*/
			if (!(input[i]=='\0'||input[i] == ' ' || input[i] == '\n' || input[i] == '\t' || input[i] == ';' || input[i] == ')' || input[i] == ',' || input[i] == '+' || input[i] == '-' || input[i] == '*' || input[i] == '/'))
			{
				while (input[i] != ' ' && input[i] != '\n' && input[i] != '\t' && input[i] != ';' && input[i] != '$'&&!Symbol(input[i])) i++;
				end = i - 1;
				string temp = input.substr(first,end-first+1);
				cout << "Error:Expected unqualified-id, " << temp << endl;
				//i++;
			}
			else//满足数字串条件就可以输出
			{
				Output(30, temp);
				i++;
			}
		}
		/*剩下的情况就是各类连接、运算符*/
		else if (input[i] == '+')
		{
			Output(12, "+");
			i++;
		}
		else if (input[i] == '-')
		{
			Output(13, "-");
			i++;
		}
		else if (input[i] == '*')
		{
			Output(14, "*");
			i++;
		}
		else if (input[i] == '/')
		{
			Output(15, "/");
			i++;
		}
		else if (input[i] == '(')
		{
			Output(16, "(");
			i++;
		}
		else if (input[i] == ')')
		{
			Output(17, ")");
			i++;
		}
		else if (input[i] == '=')//可能两个符号共同组成一个运算符的继续判断下一位，下同
		{
			if (input[i + 1] == '=')
			{
				Output(24, "==");
				i += 2;
			}
			else
			{
				Output(18, "=");
				i++;
			}
		}
		else if (input[i] == '>')
		{
			if (input[i + 1] == '=')
			{
				Output(20, ">=");
				i += 2;
			}
			else
			{
				Output(19, ">");
				i++;
			}
		}
		else if (input[i] == '<')
		{
			if (input[i + 1] == '=')
			{
				Output(22, "<=");
				i += 2;
			}
			else if (input[i + 1] == '>')
			{
				Output(23, "<>");
				i += 2;
			}
			else
			{
				Output(21, "<");
				i++;
			}
		}
		else if (input[i] == ',')
		{
			Output(25, ",");
			i++;
		}
		else if (input[i] == '.')
		{
			Output(26, ".");
			i++;
		}
		else if (input[i] == ';')
		{
			Output(27, ";");
			i++;
		}
		else if (input[i] == ':')
		{
			if (input[i + 1] == '=')
			{
				Output(28, ":=");
				i += 2;
			}
		}
		else if (input[i] == '#')//注释的情况，跳过不予处理直到下一行开始
		{
			int first = i;
			while (input[i] != '\n' && input[i] != '0') i++;
		}
		else if (input[i] == ' ' || input[i] == '\n' || input[i] == '\t') i++;
		else/*如果所有情况都不符合，则判断遇到了不符合规范的符号*/
		{
			int first = i;
			while(input[i]!='\0'&&input[i] != ' ' && input[i] != '\n' && input[i] != '\t' && input[i] != ';'&&!isalpha(input[i])&&!isdigit(input[i])&&!Symbol(input[i])) i++;
			int end = i - 1;
			string temp = input.substr(first,end-first+1);
			cout << "Error:No such symbol," << temp << endl;
		}
	}
}

int main()
{
	string s;
	string temp;
	while (getline(cin, temp))
	{
		s += (temp+"\n");
	}
	cout<<endl<<endl;
	Lex(s);//分析函数
	return 0;
}