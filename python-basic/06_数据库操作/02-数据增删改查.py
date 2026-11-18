import pymysql

# ------------------ 1、建立数据库连接 ------------------
database = pymysql.connect(
    host='localhost',
    port=3306,
    user='root',
    password='1234',
    db='test',
    charset='utf8'
)


# ------------------ 2、准备 SQL 操作清单 ------------------
sql_map = {
    "get_tables": "SHOW TABLES",
    "create_user_table": "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), age INT)",
    "insert_user": "INSERT INTO users (name, age) VALUES (%s, %s)",
    "select_users": "SELECT * FROM users",
    "update_user_age": "UPDATE users SET age = %s WHERE name = %s",
    "delete_user": "DELETE FROM users WHERE name = %s"
}


# ------------------ 3、定义数据库操作函数 ------------------

# 创建表
def create_table():
    cursor = database.cursor()
    cursor.execute(sql_map['create_user_table'])
    database.commit()  # 创建表也需要提交
    print("表创建成功或已存在")
    cursor.close()

# 写入数据
def insert_data(name, age):
    cursor = database.cursor()
    cursor.execute(sql_map['insert_user'], (name, age))
    database.commit()  # 增删改操作必须 commit 提交才会生效
    print(f"成功插入数据: {name}")
    cursor.close()

# 查询数据
def select_data():
    cursor = database.cursor()
    cursor.execute(sql_map['select_users'])
    rows = cursor.fetchall()
    for row in rows:
        print(row)
    cursor.close()

# 修改数据
def update_data(age, name):
    cursor = database.cursor()
    cursor.execute(sql_map['update_user_age'], (age, name))
    database.commit()
    print(f"成功更新 {name} 的年龄为 {age}")
    cursor.close()

# 删除数据
def delete_data(name):
    cursor = database.cursor()
    cursor.execute(sql_map['delete_user'], (name,))  # 注意：单参数元组要加逗号 (name,)
    database.commit()
    print(f"成功删除用户: {name}")
    cursor.close()


# ------------------ 4、测试执行（按需调用） ------------------
try:
    create_table()
    insert_data('张三', 25)
    insert_data('李四', 30)
    print("--- 当前所有数据 ---")
    select_data()
    update_data(26, '张三')
    delete_data('李四')
    print("--- 操作后数据 ---")
    select_data()
except Exception as e:
    print(f"发生错误: {e}")
    database.rollback()  # 如果出错，回滚事务
finally:
    # ------------------ 5、用完了关闭连接（先关游标，再关连接） ------------------
    # 放在 finally 中确保无论是否报错都会执行
    if 'cursor' in locals() and not cursor.closed:
        cursor.close()
    database.close()
    print("数据库连接已安全关闭")