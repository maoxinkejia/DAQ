# RocketMQ

## 一、设计理念
* 同大部分消息中间件一样，采用发布订阅模式，主要组件有：消息发送者，消息服务器（消息存储），消息消费，路由发现。
* 消息有序：
	消息消费者按照消息到达消息存储服务器的顺序消费，RocketMQ可严格保证消息有序。
* 消息过滤：
	消息过滤支持在服务端与消费端的消息过滤机制，可供使用者自行选择。
* 消息存储：
	对于消息的存储一般有如下两个维度的考量：消息的堆积能力和消息存储性能。
	为了追求消息存储的高性能，引入内存映射机制，所有主题的消息顺序存储在同一个文件中。
	为了避免消息无限在消息存储服务器中累积，引入了消息文件过期机制与存储空间报警机制。
* 消息高可用性
* 消息消费低延时：
	在消息不发生堆积时，以长轮询实现准实时的消息推送模式。
* 消息消费时有重复消费的可能。
* 回溯消息
* 定时消息：
	不支持任意进度的定时消息，只支持特定延迟级别。
* 消息重试机制：
	若发生异常消息，支持重新投递。

## 二、NameServer 路由中心
### 1.设计
* Broker在启动时会向所有的NameServer注册，Producer在发送消息前会先从NameServer获取Broker服务器地址列表，根据负载算法从列表中选择一台Broker进行消息的发送。
* NameServer与每台Broker都保持长连接，每30s检测一次，若发现Broker宕机则将其从注册表中移除，但路由信息并不会立即通知消息生产者
* NameServer集群部署时，彼此间互不通信，也就是说NameServer服务器之间在某一时刻的数据可能并不完全相同，但并不影响消息的发送。
* RocketMQ的一个Topic有多个消息队列，一个Broker为一个Topic默认创建4个读队列，4个写队列。多个Broker组成一个集群，BrokerName由相同的多台Broker组成的Master-Slave架构，brokerId为0代表Master，大于0表示Salve。
### 2.Namesrv的启动
* Namesrv每隔10s扫描一次Broker，移除处于不激活状态的Broker
* Namesrv每隔10分钟打印一次KV配置

```java
Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(log, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                controller.shutdown();
                return null;
            }
}));
```
* 注册JVM钩子函数并启动服务器。这里的JVM钩子函数是一种优雅停机的方式，在JVM进程关闭之前，先将线程池关闭，及时释放资源。
### 3.路由注册与删除
* 路由注册
  路由注册是通过Broker与NameServer的心跳功能实现的。Broker启动时向所有的NameServer发送心跳，每隔30s向集群中所有NameServer发送心跳包，NameServer收到Broker心跳包时会更新brokerLiveTable中的时间戳信息，Namesrv会每隔10s定时扫描brokerLiveTable。
* 路由删除
	* Namesrv会通过定时任务自动扫描brokerLiveTable中的时间戳信息，若时间戳与当前时间大于120s，则移除该Broker；
	* Broker在正常关闭的情况时，会执行unregisterBroker方法。
	* 调用RouteInfoManager#onChannelDestory方法，根据brokerAddress从brokerLiveTable、filterServerTable移除。
	* 维护brokerAddrTable，根据BrokerData中的HashMap中的brokerAddrs找到具体的Broker，从BrokerData中移除，再检查HashMap中若不再包含其他Broker，直接移除brokerAddrTable中该brokerName对应的条目。
	* 根据brokerName，从clusterAddrTable中找到Broker并从集群中移除，如果移除后集群中不包含任何Broker，则将该集群从clusterAddrTable中移除。
	* 根据brokerName，遍历所有topicQueueTable，移除当前对应的Broker，如果当前topic只包含这个Broker，则将此topic移除。
### 4.








## xxxxx、主要名词及注意事项
##### 1 groupName 
组名，主要是consumer端使用。当有多个组且组名不一样但topic相同时，在不做特殊设置时每个组都是全量消费queue里的消息。当有多个组且组名一样时且topic相同时，按照负载均衡策略轮询消费queue里的消息。
##### 2 rebalance 
负载均衡，默认为平均负载法，有多种负载方式可以设置。当一个topic、同一groupName、只有一个consumer实例时，当前consumer为全量消费；当有多个consumer实例时会根据负载算法进行负载分配消费队列，一旦分配了queue则只能消费分配到queue的消息，其余的消费不到。但系统会自动周期性调用rebalance，根据consumer实例的个数实时的更新consumer对应的消费queue。
##### 3 instanceName 
实例名，每个producer、consumer对应实例的名称。consumer的instanceName在groupName相同时必须不一样！！如果一样时在负载均衡计算会出现两个一样的instanceName，根据rebalance算法计算后会得出两个实例消费一样队列的情况（c1消费q0,q1、c2消费q0,q1\\\而q2,q3无人消费的情况）
##### 4 topic 
主题，建立producer和consumer对应消费关系。
自动创建topic配置：
enablePropertyFilter=true
autoCreateTopicEnable=true
namesrvAddr=rocketmq-nameserver1:9876;rocketmq-nameserver2:9876

##### 5 nameSrv 
服务注册发现，类似于zookeeper，用来管理broker，producer，consumer

查询集群状态
../rocketmq/bin/mqadmin clusterList -n ip:port

##### 6 queue 
队列，配合topic存放消息的地方。默认queue队列为4个：q0,q1,q2,q3。可以配置，集群多机部署时可扩展并且提高MQ性能。若将queue设置为1个时，多个producer和consumer共同操作同一个queue时，会大大降低MQ性能。
##### 7 broker
用来存储消息和分发消息的地方，当producer发送消息后，将消息发送到broker的queue内，master可写可读，slave只能读不能写，但可以从master处同步数据。可以同步刷盘也可异步刷盘。

##### 8 producer 
生产者，给指定的topic发送消息。发送到queue中，按照负载方式存放到queue中。例如发送8条消息分别存放的是q0 offset=0, q1 offset=0, q2 offset=0, q3 offset=0, q0 offset=1, q1 offset=1, q2 offset=1, q3 offset=1。
##### 9 consumer
消费者，消费订阅的topic的消息。一个consumer可订阅多个topic，多个consumer之间groupName可一样，共同消费消费一个topic。每个consumer对应的实例必须不一样。
##### 10 offset 
队列已消费到的位置。每个queue都有对应的offset
##### 11 selector
可以给消息打上标签：tag，或者更灵活的bysql方式。用来筛选consumer感兴趣的消息，将不符合条件的消息在服务端直接过滤掉，并标记该消息对应的groupName下已消费。


