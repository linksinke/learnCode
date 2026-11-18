import os
import time
from socket import *
import cv2

# 自动创建存放拍照图片的目录
os.makedirs('./img', exist_ok=True)



# ------------------ 1、与控制端建立TCP会话连接 ------------------
s = socket();
s.connect(('127.0.0.1', 8888))
rd = s.recv(1024).decode()


# ------------------ 2、接收控制端发来的指令 ------------------
if (int(rd) == 1):
    print('1关机')
    # os.system('shutdown -s -t 0') # 实际关机命令（慎用）
elif (int(rd) == 2):
    print('2重启')
    # os.system('shutdown -r -t 0') # 实际重启命令（慎用）
elif (int(rd) == 3):
    print('3拍照')
    
    # 2.1、调用摄像头
    # 0 代表默认摄像头，如果有多摄像头可改为 1, 2...
    cm = cv2.VideoCapture(0)
    ret, frame = cm.read();
    
    # 2.2、判断摄像头是否开启
    if ret:
        # 2.3、保存照片到本地
        file_path = f'./img/{time.time()}.jpg'
        cv2.imwrite(file_path, frame);
        cm.release()

        # 2.4、获取文件大小并发送给控制端（告知）
        f_size = os.path.getsize(file_path);
        s.send(str(f_size).encode())
        
        # 2.5、等待控制端的“开始传输”确认信号（由服务端确认才可以执行）
        print(s.recv(1024).decode())

        # 2.6、读取图片文件并分块发送
        print(f"正在发送文件: {file_path}")
        with open(file_path, 'rb') as file:
            while True:
                # 每次读取 1024 字节发送
                data = file.read(1024)
                if not data:
                    break
                s.send(data)
 
    else:
        print("摄像头读取失败！")
        cm.release()


# ------------------ 3、关闭连接 ------------------
s.close()