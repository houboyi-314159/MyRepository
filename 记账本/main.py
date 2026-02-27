import os
from datetime import date
import subprocess

# 1. 相对路径
base_dir = os.path.dirname(__file__)
prop_path = os.path.join(base_dir, "settings&data", "settings.properties")
jar_path = os.path.join(base_dir, "code", "edit.jar")

# 2. 读取 settings.properties
def load_props(path):
    props = {}
    if os.path.exists(path):
        with open(path, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith("#"):
                    if "=" in line:
                        key, val = line.split("=", 1)
                        props[key.strip()] = val.strip()
    return props

def save_props(path, props):
    with open(path, "w", encoding="utf-8") as f:
        for key, val in props.items():
            f.write(f"{key}={val}\n")

props = load_props(prop_path)

# 3. 判断是不是第一次用（is_active 不是 true 才算首次）
first_time = props.get("is_active", "").lower() != "true"

if first_time:
    # 首次使用：激活并设置日期，名字和年龄留空
    props["is_active"] = "true"
    props["name"] = "name"
    props["age"] = "0"
    save_props(prop_path, props)
    print("首次使用，已初始化。请在 Java 窗口中输入名字和年龄。")
    subprocess.run(["javaw", "-jar", jar_path])
    exit(0)
else:
    # 已经用过：显示主菜单
    print("请选择操作：")
    print("0 - 启动 Java 窗口")
    print("1 - 重置为首次使用状态")
    choice = input("请输入选项（0/1）：").strip()

    if choice == "0":
        print("正在启动 Java 窗口...")
        subprocess.run(["javaw", "-jar", jar_path])
        exit(0)
    elif choice == "1":
        # 重置为首次使用状态
        props["is_active"] = "false"
        props["name"] = "name"
        props["age"] = "0"
        save_props(prop_path, props)

        # 清空 ledger.csv
        ledger_path = os.path.join(base_dir, "settings&data", "ledger.csv")
        try:
            with open(ledger_path, "w", encoding="utf-8") as f:
                pass
            print("账本已清空。")
        except Exception as e:
            print("清空账本失败：", e)

        print("已重置。接下来将重新初始化。")
        exit(0)
    else:
        print("无效选项，未作任何修改。")