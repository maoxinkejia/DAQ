# Kubernetes

## etcd
#### 主要功能
* 基本的key-value存储
* 监听机制
* key的过期及续约机制，用于监控和服务发现
* 原子CAS和CAD，用于分布式锁和leader选举

## API Server
#### 主要功能
*  提供集群管理的REST API接口，包括认证授权，数据校验以及集群状态变更等
* 提供其他模块之间的数据交互和通信的枢纽（其他木块通过API Server查询或修改数据，只有API Server才直接操作etcd）

## kube-scheduler
#### 主要功能
* 负责分配调度pod到集群内的节点上
* 监听kube-apiserver，查询还未分配Node的Pod，然后根据调度策略为这些Pod分配节点（更新Pod的NodeName字段）

## Controller Manager
Controller Manager由kube-controller-manager和cloud-controller-manager组成，是Kubernetes的大脑，
它通过API Server监控整个集群的状态，并确保集群处于预期的工作状态。

## Kubelet
每个Node节点上都运行一个Kubelet服务进程，默认监听10250端口，接收并执行master发来的指令，管理pod及pod中的容器。
每个Kubelet进程会在API Server上注册所在Node节点的信息，定期向master节点汇报该节点的资源使用情况，并通过cAdvisor监控节点和容器的资源。


## RC (Replication Controller)  && RS (ReplicaSet)
#### 什么是Replication Controller
* RC保证了在所有时间都有特定数量的Pod副本正在运行，如果太多了，RC就杀死几个，若果太少了，RC就会新建几个。
和直接创建Pod不同的是，RC会替换掉那些删除的或者被终止的Pod。
* RC就想是一个进程管理器，监管者不同node上的多个pod，而不是单单监控一个node上的pod，RC会委派本地容器来启动一些节点上的服务。
* RC只会对那些RestartPolicy = Always的pod生效（默认就是），RC不会去管理那些有不同启动策略的pod。
* RC永远不会自己关闭，服务可能会有多个pod组成，这些pod又被多个RC控制着，我们希望RC会在服务的生命周期被删除和创建，
对于服务和用户来说，RC是通过一种无形的方式来维持着服务的状态。

#### RC和RS的区别
* 在新版的kubernetes中建议使用ReplicaSet来取代Replication Controller。
* RS和RC没有本质的不同，只是名字不一样，并且RS支持集合式的selector（RC仅支持等式）。
* 虽然RS和RC可以独立使用，但建议使用Deployment来自动管理，这样就无需担心跟其他机制不兼容问题


### 常用命令
* kubectl get node 查询k8s所有的node节点
* kubectl describe node <node> 查看node的详细信息
* kubectl get pod --all-namespaces -o wide 查看所有pod所属namespace信息，展示多数据
* kubectl get pod -n <ns> 查看指定namespace下的pod
* kubectl describe pod <podname> -n <ns>  显示pod的详细信息，特别是查看pod无法创建时候的日志
* 