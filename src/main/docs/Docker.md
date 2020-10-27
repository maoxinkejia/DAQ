# Docker

## Docker的安装
* 中文官网：https://www.docker-cn.com
* 中央仓库：https://hub.docker.com/

#### 前提说明
CentOS-6.5及以上的版本

#### Docker的基本组成
* 镜像
Docker镜像（Image）就是一个只读的模板。镜像可以用来创建Docker容器，一个镜像可以创建很多容器。


* 容器
	* Docker利用容器（Container）独立运行的一个或一组应用。容器是用镜像创建的运行实例。
	* 它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台。
	* 可以把容器看做是一个简易版的Linux环境（包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序。
	* 容器的定义和镜像几乎一模一样，也是一堆层的统一视角，唯一区别在于容器的最上面那一层是可读可写的。


* 仓库
	* 仓库（Repository）是集中存放镜像文件的场所。
	* 仓库（Repository）和仓库注册服务器（Registry）是有区别的。仓库注册服务器上往往存放着多个仓库，每个仓库中又包含了多个镜像，每个镜像有不同的标签（tag）。
	* 仓库分为公开仓库（Public）和私有仓库（Private）两种形式。
	* 最大的公开仓库是中央仓库，存放了数量庞大的精心昂供用户下载。国内的公开仓库包括阿里云、网易云等。


#### 安装步骤
CentOS 7.0以上版本参考官网：https://docs.docker.com/engine/install/centos/

#### 永远的HelloWord
* 阿里云镜像
	* 登陆阿里云容器Hub服务控制台：https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
	* 按相关文档操作即可。
* 网易云镜像
	* 操作步骤同阿里云相同，镜像地址配置：{"registry-mirrors": ["https://hub-mirror.c.163.com"]}

* docker run hello-world 


#### 底层原理
* Docker是如何工作的？
	* Docker是一个Client-Server结构的系统，DOcker守护进程运行在主机上， 然后通过Socket连接从客户端访问，守护进程从客户端接受命令并管理运行在主机上的容器。容器，是一个运行时环境，就是我们前面说到的集装箱。

* 为什么Docker比VM快？
	* docker有着比虚拟机更少的抽象层。由于docker不需要Hypervisor实现硬件资源虚拟化，运行在docker容器上的程序直接使用的都是实际物理机的硬件资源。因此在CPU、内存利用率上会有明显优势。
	* docker利用的是宿主机的内核，而不需要Guest OS。因此，当新建一个容器时，docker不需要和虚拟机一样重新加载一个操作系统内核。