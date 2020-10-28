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


## Docker常用命令

#### 帮助命令
* docker version
* docker info
* docker --help

#### 镜像命令
* docker images
	* 列出本地主机上的镜像
	* OPTIONS说明
		* -a  列出本地所有的镜像（含中间映像层）
		* -q  只显示镜像ID
		* --digests  显示镜像的摘要信息
		* --no-trunc  显示完整的镜像信息

* docker search
	* 某个xxx镜像名字  https://hub.docker.com
	* OPTIONS说明
		* --no-turnc  显示完整的镜像描述
		* --filter key=value  过滤条件
		* --automated  只列出automated build类型的镜像

* docker pull
	* 下载镜像
	* docker pull xxxx[:TAG]，不写默认latest

* docker rmi
	* 删除镜像
	* 删除一个 docker rmi -f 镜像ID
	* 删除多个 docker rmi -f 镜像1:TAG 镜像2:TAG
	* 删除全部 docker rmi -f $(docker images -qa)
___

#### 容器命令
* 新建并启动容器
	* docker run [OPTIONS] image [COMMAND] [arg...]
	* OPTIONS说明：
		* --name "容器新名字" 为容器指定一个名称
		* -d 后台运行容器，并返回容器ID（启动守护式容器）
		* -i 以交互模式运行容器，通常与-t同时使用
		* -t 为容器重新分配一个伪输入终端，通常与-i同时使用
		* -P 随机端口映射
		* -p 指定端口映射，有以下四种形式
			ip:hostPort:containerPort
			ip::containerPort
			hostPort:containerPort
			containerPort
	
* 列出所有正在运行的容器
	* docker ps [OPTIONS]
	* OPTIONS说明：
		* -a 列出所有正在运行的容器 + 历史上运行过的
		* -l 显示最近创建的容器
		* -n 显示最近n个创建的容器
		* -q 静默模式，只显示容器编号
		* --no-trunc 不截断输出

* 退出容器
	* exit ：容器停止退出
	* ctrl + P + Q 容器不停止退出

* 启动容器
	* docker start 容器Id或者容器名

* 停止容器
	* docker stop 容器id或容器名

* 强制停止容器
	* docker kill 容器id或容器名

* 删除已停止的容器
	* docker rm 容器id

* 删除多个容器
	* docker rm -f $(docker ps -aq)

* ***重点***
	* docker run -d centos
		* 容器启动后使用docker ps -a 进行查看，会发现容器已经退出。
		* 很重要的一点：Docker容器后台运行，就必须有一个前台进程，容器运行的命令如果不是那些一直挂起的命令（比如top，tail）就是会自动退出的。
		* 这个是docker机制的问题，这样的容器后台启动后，会立即自杀，因为他觉得没事可做了，所以，最佳的解决方案就是将要运行的程序以前台进程的形式运行。

	* 查看容器日志 docker logs -f -t --tail 20 容器ID
	* 查看容器内运行的进程 docker top 容器ID
	* 查看容器内部细节 docker inspect 容器ID
	* 进入正在运行的容器并以命令行交互 
		* docker attach 容器ID，直接进入容器内，不会启动进的进程。
		* docker exec -it 容器ID ls -l /tmp，在外层执行命令。
		* docker exec -it 容器ID bin/bash，进入到容器内，会启动新的进程。
	* 从容器内拷贝文件到主机上
		* docker cp 容器ID:容器内路径 目的主机路径

___

## Docker镜像
#### 是什么？
	镜像是一种轻量级、可执行的独立软件包，用来打包软件运行环境和基于运行环境开发的软件，它包含运行某个软件所需的所有内容，包括代码、运行时、库、环境变量和配置文件。
* UnionFS(联合文件系统)
	* UnionFS是一种分层、轻量级并且高性能的文件系统，它支持对文件系统的修改作为一次提交来一层层的叠加，同事可以将不同目录挂载到同一个虚拟文件系统下。Union文件系统是Docker镜像的基础。镜像可以通过分层来进行集成，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像。
	* 特性：一次同时加载多个文件系统，但从外面看起来，只能看到一个文件系统，联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录。

* Docker镜像加载原理
	* docker的镜像实际上由一层一层的文件系统组成
		* bootfs（boot file system）主要包含bootloader和kernel，bootloader主要是引导加载kernel，Linux刚启动时会加载bootfs文件系统，在Docker镜像的最底层是bootfs。这一层与我们经典的Linux/Unix系统是一样的，包含boot加载器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由bootfs转交给内核，此时系统也会卸载bootfs。
		* rootfs（root file system），在bootfs之上。包含的就是典型Linux系统中的/dev,/proc,/bin,/etc等标准目录和文件。rootfs就是各种不同的操作系统发行版，比如Ubnutu，Centos等等。
	* 平时的一个虚拟机CentOS都是好几个G，为什么docker这里才200M？？
		对于一个精简的OS，rootfs可以很小，只需要包括最基本的命令、工具和程序库就可以了，因为底层直接用Host的kernel，自己只需要提供rootfs即可。由此可见对于不同的linux发行版，bootfs基本是一致的，rootfs会有差别，因此不同的发行版可以公用bootfs。
	
* 分层的镜像
* 为什么Docker镜像要才用这种分层的结构呢
	* 最大的好处就是：共享资源
	* 比如：有多个镜像都从相同的base镜像构建而来，那么宿主机只需在磁盘上保存一份base镜像，同时内存中也只需要加载一份base镜像，就可以为所有容器服务了。而且镜像的每一层都可以被共享。

#### 特点
	Docker镜像都是只读的，当容器启动时，一个新的可写层被加载到镜像的顶部。这一层通常被称作容器层，容器层之下都叫镜像层。

#### Docker镜像commit操作补充
* docker commit 提交容器副本使之成为一个新的镜像
* docker commit -m="提交的描述信息" -a="作者" 容器ID [命名空间]/要创建的目标镜像名:[TAG]

___
## Docker容器数据卷
#### 是什么？
* Docker容器产生的数据，如果不通过docker commit 生成新的镜像，使得数据作为镜像的一部分保存下来，那么当容器删除后，数据自然也就没有了。
* 为了能保存数据在docker中，我们使用卷。
* 类似于redis的rdb/aof
#### 能干嘛？
* 卷就是目录或文件，存在于一个或多个容器中，由docker挂载到容器，但不属于联合文件系统，因此能够绕过UnionFS提供一些用于持续存储或共享数据的特性。
* 卷的设计目的就是数据的持久化，完全独立于容器的生存周期，因此Docker不会在容器删除时删除其挂在的数据卷。
#### 数据卷
#### 数据卷容器


































