# 导入所需的模块
import sys

# 存储用户信息的字典
users = {}


def add_user():
    """添加用户信息"""
    username = input("请输入用户名: ")
    if username in users:
        print("用户名已存在，请重新输入。")
        return
    age = input("请输入年龄: ")
    email = input("请输入邮箱: ")
    users[username] = {'age': age, 'email': email}
    print(f"用户 {username} 添加成功。")


def view_users():
    """查看所有用户信息"""
    if not users:
        print("没有用户信息。")
        return
    print("用户信息列表:")
    for username, info in users.items():
        print(f"用户名: {username}, 年龄: {info['age']}, 邮箱: {info['email']}")


def delete_user():
    """删除用户信息"""
    username = input("请输入要删除的用户名: ")
    if username in users:
        del users[username]
        print(f"用户 {username} 删除成功。")
    else:
        print("用户不存在。")


def main():
    """主函数，提供菜单选项"""
    while True:
        print("\n用户信息管理系统")
        print("1. 添加用户")
        print("2. 查看用户")
        print("3. 删除用户")
        print("4. 退出系统")
        choice = input("请选择操作 (1-4): ")

        if choice == '1':
            add_user()
        elif choice == '2':
            view_users()
        elif choice == '3':
            delete_user()
        elif choice == '4':
            print("感谢使用，再见！")
            sys.exit(0)
        else:
            print("无效的选择，请重新输入。")


if __name__ == "__main__":
    main()
