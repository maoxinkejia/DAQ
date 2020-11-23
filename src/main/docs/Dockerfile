FROM centos:7

# jdk 安装
WORKDIR /opt

RUN mkdir jdk
RUN chmod 777 /opt/jdk

ADD jdk-8u271-linux-x64.tar.gz /opt/jdk

# 环境变量
ENV JAVA_HOME /opt/jdk/jdk1.8.0_271
ENV JRE_HOME ${JAVA_HOME}/jre
ENV CLASSPATH=$JAVA_HOME/bin:$JAVA_HOME/lib:$JAVA_HOME/jre/lib
ENV PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH


# 添加解压服务
RUN mkdir /app
WORKDIR /app

ADD dts-web.tar .

# 暴露端口
EXPOSE 9010


# 开始构建
CMD "sh /app/dts-web/bin/app.sh start dev"
