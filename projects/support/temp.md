ml algorithm

1, logistic regression
Logistic regression  - for classification, 二分预测正反例
   http://52opencourse.com/125/coursera%E5%85%AC%E5%BC%80%E8%AF%BE%E7%AC%94%E8%AE%B0-%E6%96%AF%E5%9D%A6%E7%A6%8F%E5%A4%A7%E5%AD%A6%E6%9C%BA%E5%99%A8%E5%AD%A6%E4%B9%A0%E7%AC%AC%E5%85%AD%E8%AF%BE-%E9%80%BB%E8%BE%91%E5%9B%9E%E5%BD%92-logistic-regression
    multi-dimensional feature 判断相关性  yes or no (spam or non-spam)
    可以用MR对同样的dataset进行重复处理, so Spark benefits from caching the input in RAM across iterations.

    sigmoid function, S形函数: g(x)=1/(1+e的-x次方)    http://youess.github.io/blog/luo-ji-hui-gui.html
    g(x)>= 0.5时, 结果分类为1  (decision boundary)
    g(x)<0.5, 结果为分类0
    
    logistic regression核心就是对s形函数的参数θ进行随机的回归拟合regression fitting.

   有监督的学习,有training set训练集
  
val points = spark.textFile(...).map(parsePoint).cache()
var w = Vector.random(D) // current separating plane, 此向量不是List那么实现
for (i <- 1 to ITERATIONS) {
  val gradient = points.map(p =>
    (1 / (1 + exp(-p.y*(w dot p.x))) - 1) * p.y * p.x   //exp在scala里表示e的power幂 dot表示2个向量的点积A*B  https://zh.wikipedia.org/wiki/%E6%95%B0%E9%87%8F%E7%A7%AF
  ).reduce(_ + _)
  w -= gradient
}
println("Final separating plane: " + w)



Loss function J: θ把1个事物映射到1个实数上的map函数.  一般用来计算预测与实际的偏离差值(loss)
一般用来数据建模，以此来提高生产效率。
Loss function就是来判断θ与实际结果的效果.

gradient梯度:  一个立体空间, 垂直距离/水平面距离 vertical distance/horizontal distance
Gradient descent梯度下降:  每次取1个值,按照步长来迭代计算,如果某个结果值算出来与上1个结果值相差很小(收敛),那么可以认为找到了这个值.  http://www.zhizhihu.com/html/y2011/3632.html

Train: 通过训练集(input)和模型(算法)来优化最佳的parameter.   http://www.zhihu.com/question/29271217
假定房屋定价的模型为y=x  => h(x),那么如何让5个features分量(e.x. x1面积,x2朝向,x3楼层,x4位置,x5学区)能够匹配到这个直线上了?   x=θ0 + θ1*x1 + θ2*x2 + θ3*x3 + θ4*x4 + θ5*x5
我们train的工作就是调整θ参数

Regression回归: 分析自变量与应变量间的关系(方程)，一般通过数理统计的方法进行分析. 

 fitting拟合:  把参数,模型与data进行模拟匹配, 如果这个过程做得不够, 太多的data不符合你的模型(直线-线形回归，曲线-二次回归/二项式回归或其他方程)则是under-fitting, 如果为了使所有的data都在curve上则可能是牵强附会的over-fitting, 此时可能计算复杂,效率低下.  合适就好.


噪声:
