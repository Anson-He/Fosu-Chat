#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pymysql
import pandas as pd
db = pymysql.connect(host="47.98.162.4", user="fosu", password="hyh010710", database="fosu",port=3306)
cursor = db.cursor()
data = pd.read_excel("MathBigData.xlsx")
data = data.drop(columns = ['序号'])
for i in range(len(data)):
    no = data.iloc[i]['学号']
    name = data.iloc[i]['姓名']
    class0 = data.iloc[i]['班级名称']
    print(no)
    print(name)
    print(class0)
    cursor.execute("insert into registered(id,name,class1) values("+"'"+str(no)+"'"+","+"'"+name+"'"+","+"'"+class0+"'"+");")
    print(i)
cursor.close()
db.commit()
db.close()

