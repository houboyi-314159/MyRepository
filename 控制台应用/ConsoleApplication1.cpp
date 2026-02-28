// ConsoleApplication1.cpp : 此文件包含 "main" 函数。程序执行将在此处开始并结束。
//

#include <iostream>

int main()
{
	int choice;
	char str[256];
    std::cout << "Welcome to go my Application!\n";
	std::cout << "This is a simple C++ console application.\n";
	std::cout << "What do you want to do?\n" << "0    :Get Unicode number\n" << "1    :pass\n" << "other:Exit\n";
	std::cout << "Please input your choice: ";
	std::cin >> choice;
	switch (choice)
	{
		case 0:
			std::cout << "You choose 0, input string (<= 256 B), then get its Unicode int.\n";
			std::cin >> str;
			std::cout << "The Unicode int is :";
			for (int i = 0; str[i] != '\0'; i++)
			{
				std::cout << str[i] << (int)str[i] << " ";
			}
			break;
		case 1:
			std::cout << "You choose 1, pass.\n";
			//pass 待开发
			break;
		default:
			std::cout << "You choose other, exit.\n";
			break;
	}
	std::cout << "Press Enter to continue...";
	std::cin.ignore();  // 忽略之前可能残留的换行符
	std::cin.get();     // 等待用户按回车
	return 0;
}

// 运行程序: Ctrl + F5 或调试 >“开始执行(不调试)”菜单
// 调试程序: F5 或调试 >“开始调试”菜单

// 入门使用技巧: 
//   1. 使用解决方案资源管理器窗口添加/管理文件
//   2. 使用团队资源管理器窗口连接到源代码管理
//   3. 使用输出窗口查看生成输出和其他消息
//   4. 使用错误列表窗口查看错误
//   5. 转到“项目”>“添加新项”以创建新的代码文件，或转到“项目”>“添加现有项”以将现有代码文件添加到项目
//   6. 将来，若要再次打开此项目，请转到“文件”>“打开”>“项目”并选择 .sln 文件
