import os
from socket import *

# 自动创建存放拍照图片的目录
os.makedirs('./img', exist_ok=True)



# ------------------ 1、监听TCP活动的会话连接 ------------------
s = socket(AF_INET, SOCK_STREAM)
# 绑定地址和端口，'0.0.0.0'表示监听本机所有网卡
s.bind(('0.0.0.0', 8888))
# 开始监听，允许一个连接排队
s.listen(1)
print("等待被控端连接...")


# ------------------ 2、客户端连接接收执行操作 ------------------
# 接受客户端连接，s_new是新的连接套接字，addr是客户端地址
s_new, addr = s.accept()
print(f"已连接来自: {addr}")

# 显示控制菜单
print('1.关机 2.重启 3.拍照')
choice = input('请选择操作: ')
s_new.send(choice.encode())

# 2.1、由于拍照是文件传输，需要做文件流的接收处理
if choice == '3':
    # 2.2、接收被控端发来的文件大小信息
    f_size = int(s_new.recv(1024).decode())
    print(f"准备接收图片，文件大小: {f_size} 字节")
    
    
    # 2.3、发送确认信号，通知被控端可以开始传输文件数据
    s_new.send('请开始！'.encode())
    
    
    # 2.4、接收并保存图片数据
    w_size = 0
    with open('./img/blank.jpg', 'wb') as file:
        while w_size < f_size:
            # 循环接收数据，直到接收的总大小等于文件大小
            data = s_new.recv(1024)
            if not data:
                break
            file.write(data)
            w_size += len(data)


# ------------------ 3、关闭连接 ------------------
s_new.close()
s.close()