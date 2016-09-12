* syn, lock
sync是language level,扩展性很差, block所有操作释放CPU, lock是library具备可扩展性scale to dual cores的潜能.
可重入(synchronized和lock都是reentrant)
中断锁
超时
公平锁与非公平锁,exclusive lock
trylock, tryrelease, tryacquire
读写锁
lock的scalable更好(dual cores)
spin lock(CAS) 自旋锁有个更贴切的名字：自旋-指数后退锁，也即复合锁。很显然，自旋在多处理器上才有意义。
执行几条空的汇编指令，目的是占着CPU不放，等待获取锁的机会.
lock free: cmpxchg CPU, volitale+while cas try to get lock多尝试几次以便尽快获得CPU运行机会.
区别:synchronized是阻塞互斥,simple; lock/cas:是尽量多试几次获取CPU的运行机会, more scalable but might result in high cpu usage.
[Synchronized](http://blog.csdn.net/bohu83/article/details/51141836)

* Future mode:
主线程期待某个操作异步执行，并在未来的某个时间获取结果。
如果是普通的thread, thread的结果没地方存，只能放在一个第3方的地方，而Future模式是把结果放在Future对象中

* CAS, lock free: 抢占式
各个线程不会互相阻塞，那么你的程序才能成为lock free的.

我们平常用的互斥锁，当有线程获得锁(synchronized)，其他线程就被阻塞掉了，这里的问题就是如果获得锁的线程挂掉了，而且锁也没有
释放，那么整个程序其实就被block在那了，而如果程序是lock free的那么即使有线程挂掉，也不影响整个程序继续向
下进行，也就是系统在整体上而言是一直前进的.

lock free, 一直尝试是否这个val是下一个expected value, 确保执行它的所有线程中至少有一个能够继续往下执行.

* happen before: thread start/run

* daemon thread: setDaemon(true), low priority (1), GC Daemon

* reflection: declared method: public

* pass by value



