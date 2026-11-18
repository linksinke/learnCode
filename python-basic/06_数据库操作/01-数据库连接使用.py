import pymysql

# ------------------ 1、建立数据库连接 ------------------
database = pymysql.connect(host='localhost', port=3306, user='root', password='1234', db='test', charset='utf8')


# ------------------ 2、准备做一些事情 ------------------
# 例如获取数据库的所有表
cursor = database.cursor()
cursor.execute('SHOW TABLES')
tables = cursor.fetchall()
for table in tables:
    print('--tables--------')
    print(table)


# ------------------ 3、用完了关闭连接 ------------------
# !!! 先关游标，再关连接 !!!
cursor.close()
database.close()
