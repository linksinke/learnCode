import xlrd

#  xlrd库使用说明：从 xlrd 2.0.0 开始，官方移除了对 .xlsx 的支持，原因是 .xlsx 本质是 XML+ZIP 结构，和 .xls 的二进制格式完全不同

# ------------------ 1、打开 Excel 工作簿文件 ------------------
xls_file = xlrd.open_workbook('信息登记表.xls')


# ------------------ 2、获取指定的 sheet 工作表 ------------------
# 也可以通过索引获取：sheet = xls_file.sheet_by_index(0)
sheet1 = xls_file.sheet_by_name('sheet1')


# ------------------ 3、遍历所有行和列，逐格读取数据 ------------------
# 外层循环遍历行，内层循环遍历列
for row in range(sheet1.nrows):
    for col in range(sheet1.ncols):
        print(sheet1.cell_value(row, col), end='\t')
    print()  # 每行结束后换行
