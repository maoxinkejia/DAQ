# 面试-高级

## JUC多线程及高并发
#### volatile的理解
* volatile是java虚拟机提供的轻量级的同步机制
	* 保证可见性
	* 不保证原子性
	* 禁止指令重排

* JMM内存模型
	* JMM（java内存模型java memory model）本身是一种抽象的概念，并不真实存在，他描述的是一组规则或规范，
	通过这组规范定义了程序中各个变量（包括实例字段，静态字段和构成数组对象的元素）的访问方式。
	* JMM关于同步的规定
		1.线程解锁前，必须把共享变量的值刷新回主内存
		2.线程加锁前，必须读取主内存的最新值到自己的工作内存
		3.加锁解锁是同一把锁
	* 由于JVM运行程序的实际是线程，而每个线程创建时JVM都会为其创建一个工作内存，工作内存是每个线程的
	私有数据区域，而java内存模型中规定所有变量都存储在主内存，主内存是共享内存区域，所有线程都可以访问，
	但线程对变量的操作（读取赋值等）必须要工作内存中进行，首先要将变量存主内存拷贝到自己的工作内存空间，
	然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，各个线程中存储主内存中的
	变量副本拷贝，因此不同的线程间无法访问对方的工作内存，线程的通信（传值）必须通过主内存来完成。

	* JMM内存模型在多线程开发中需要遵循以下特性
		* 可见性
			```
			各个线程对内存中共享变量的操作都是各个线程各自拷贝到自己的工作内存进行操作后再写回到主内存中的。
			若线程A修改了共享变量X的值但还未写回主内存时，另外一个线程B又对主内存中的共享变量X进行操作，
			此时A线程工作内存中共享变量X对于B线程来说并不可见。这种工作内存与主内存同步延迟现象就造成了可见性问题。
			```
		* 原子性
		
		* 有序性
