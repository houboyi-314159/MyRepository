\# Ledger \& User Info Manager



这是一个由 \*\*Python + Java\*\* 共同协作的小工具：



\- \*\*Python\*\*：负责初始化用户信息（`setting.json`），并提供主控菜单。

\- \*\*Java Swing GUI\*\*：负责记账数据（`data.db` SQLite）、用户信息录入与展示。



\## 功能



1\. \*\*基本信息管理\*\*  

&nbsp;  - 录入/修改用户名、年龄等信息  

&nbsp;  - 保存到 `settings\&data/setting.json`



2\. \*\*记账功能\*\*  

&nbsp;  - 添加收入/支出记录  

&nbsp;  - 查看记录列表  

&nbsp;  - 删除指定记录  

&nbsp;  - 数据存储于 `data.db`



3\. \*\*多语言协作\*\*  

&nbsp;  - Python 启动 Java 窗口  

&nbsp;  - 双方通过文件交换数据



\## 使用方法



1\. 安装 JDK 17+，确保 `java` 命令可用。  

2\. 在 `dataer` 目录下运行：

