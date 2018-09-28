import numpy

a=[3.5, 2.0, 0,  4.5,5.0, 1.5, 2.5, 2.0]  //user a
b=[2.0, 3.5, 4.0,0,  2.0, 3.5, 0,   3.0]  //user b
suma=sum([i*i for i in a])
sqrta=numpy.sqrt(suma)        #user a的值

sumb=sum([i*i for i in b])
sqrtb=numpy.sqrt(sumb)

dotab=numpy.dot(a,b)  #  a.b
# dotab=[a[i]*b[i] for i in range(0,len(a) ]

cos1=dotab/(sqrta*sqrtb)  #cos相似度, =1最相似

import pandas as pd
df=pd.read_csv('Desktop/1.csv')
df['column1']
len(df)
df.iloc(4)
df.head(3)
df.tail(3)
df[df.column1>100]
df.sort_index(ascending=False)
df.to_csv('Desktop/2.csv')

df.merge
df.plot(kind='bar', x='', y=[], legend=False)



                                   
                                   
