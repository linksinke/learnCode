import xlwt

# xlwt库使用说明：仅支持 .xls 格式（单表最多 65536 行、256 列），不支持 .xlsx

# ------------------ 1、创建 Excel 工作簿 ------------------
new_workbook = xlwt.Workbook(encoding='utf-8')


# ------------------ 2、创建 sheet 工作表 ------------------
worksheet1 = new_workbook.add_sheet('sheet1', cell_overwrite_ok=True)

# 开始往sheet里写入数据
# 2.1、写入标题行
# 标题内容：序号|姓名|性别|年龄，写在第1行
worksheet1.write(0, 0, '序号')
worksheet1.write(0, 1, '姓名')
worksheet1.write(0, 2, '性别')
worksheet1.write(0, 3, '年龄')

# 2.2、写入数据行
# “张三” 的信息写在第2行
worksheet1.write(1, 0, '1')
worksheet1.write(1, 1, '张三')
worksheet1.write(1, 2, '男')
worksheet1.write(1, 3, '20')

# “李四” 的信息写在第3行
worksheet1.write(2, 0, '2')
worksheet1.write(2, 1, '李四')
worksheet1.write(2, 2, '男')
worksheet1.write(2, 3, '21')


# ------------------ 3、把写好的数据存储 ------------------
new_workbook.save('.\\信息登记表.xls')
