---
title: 环境搭建
tags: [Java,zookeeper,微服务,分布式]
---
# 介绍
下面安装教程适用于Linux、Mac系统。
高可用集群节点数必须是奇数，最小节点为3个，这是实现高可用集群的最小规模。
 
- 为什么是奇数？
ZooKeeper 基于 ZAB 协议（类 Paxos 算法）的 多数派（Quorum）原则：写操作需超过半数节点确认（如 3 节点需 2 个确认）。奇数节点可避免脑裂（如 2 节点集群中，1 节点故障即无法达成多数）。
容错能力：
3节点集群允许1节点故障（剩余 2 > 3/2）。
5节点集群允许2节点故障（剩余 3 > 5/2），适合生产环境。
- 为什么不能是偶数？
偶数节点（如 4 台）的容错能力与奇数相同（4 节点仅允许 1 故障，与 3 节点一致），但增加了资源消耗和选举复杂度。

- 版本说明：zookeeper `v3.9.3`
> 注意：需要提前安装java环境
## 单机环境搭建
1、首先进入**conf**目录，从**zoo_sample.cfg**复制一个副本为**zoo.cfg**，**3.9.3**版本内部默认会使用**8080**和**2181**端口，安装前需要提前检查是否有进程占用该端口。
```shell
cp /conf/从zoo_sample.cfg zoo.cfg
```
2、进入/bin目录，执行下面命令启动zookeeper服务端
```shell
./zkServer.sh start
```
停止服务： ./zkServer.sh stop

3、执行以下命令可以启动一个基于终端的客户端
```shell
./zkCli.sh
```
（1）查看所有节点
```shell
ls /
```
（2）创建一个节点
```shell
create /user admin
```
（3）获取节点信息
```shell
get -s /user
```
（4）删除节点
```shell
delete /user
```
## 集群环境搭建
>本次搭建测试版本：3.9.3
> 
> 工作目录：groups
> 
> 操作系统要求：Linux或Mac
### 单服务器搭建集群（生产不推荐）
1、拷贝三份zookeeper服务
![img1.png](img1.png)

2、创建3个数据文件夹
```shell
sudo mkdir /var/lib/zookeeper/data/zk1
sudo mkdir /var/lib/zookeeper/data/zk2
sudo mkdir /var/lib/zookeeper/data/zk3

sudo chmod -R 777 /var/lib/zookeeper
```
创建myid文件：
```shell
echo "1" > /var/lib/zookeeper/data/zk1/myid
echo "2" > /var/lib/zookeeper/data/zk2/myid
echo "3" > /var/lib/zookeeper/data/zk3/myid
```
注意：写入的1、2、3需要和下面配置分钟的server.[id]一一对应。
3、编辑每个节点的配置文件
zoo-node1/conf/zoo.cfg
```shell
dataDir=/var/lib/zookeeper/data/zk1
tickTime=2000
initLimit=5
syncLimit=2
clientPort=2181
admin.serverPort=8081
server.1=localhost:2888:3888
server.2=localhost:2889:3889
server.3=localhost:2890:3890
```
zoo-node2/conf/zoo.cfg
```shell
tickTime=2000
dataDir=/var/lib/zookeeper/data/zk2
initLimit=5
syncLimit=2
clientPort=2182
admin.serverPort=8082
server.1=localhost:2888:3888
server.2=localhost:2889:3889
server.3=localhost:2890:3890
```
zoo-node3/conf/zoo.cfg
```shell

tickTime=2000
dataDir=/var/lib/zookeeper/data/zk3
initLimit=5
syncLimit=2
clientPort=2183
admin.serverPort=8083
server.1=localhost:2888:3888
server.2=localhost:2889:3889
server.3=localhost:2890:3890
```
4、编写一键启动脚本并启动
在group目录下写个最简单的集群启动脚本**stat-group.sh**
```shell
#!/bin/bash
./zoo-node1/bin/zkServer.sh start
./zoo-node2/bin/zkServer.sh start
./zoo-node3/bin/zkServer.sh start
echo "ZooKeeper 集群已启动 "
```
启动集群
```shell
./stat-group.sh
```
5、验证集群搭建结果
可以检查下面端口是否被占用验证集群是否启动成功
```shell
lsof -i:2181
lsof -i:2182
lsof -i:2183
lsof -i:8081
lsof -i:8082
lsof -i:8083
```
在目录groups下执行下面命令验证集群启动状态
```shell
./zoo-node1/bin/zkServer.sh status
./zoo-node2/bin/zkServer.sh status
./zoo-node2/bin/zkServer.sh status
```
连接集群节点
```shell
./zoo-node1/bin/zkCli.sh
./zoo-node2/bin/zkCli.sh
./zoo-node3/bin/zkCli.sh
```

## Java编程
引入[curator](https://curator.apache.org/docs/about)客户端

```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>5.8.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>5.8.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-client</artifactId>
    <version>5.8.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-x-discovery</artifactId>
    <version>5.8.0</version>
</dependency>
```
一个简单的集群案例
```java
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.
                builder().connectString("localhost:2181," +
                        "localhost:2182,localhost:2183").
                sessionTimeoutMs(4000).retryPolicy(new
                        ExponentialBackoffRetry(1000, 3)).
                namespace("").build();
        client.start();
        Stat stat = new Stat();
        //创建节点
        client.create().creatingParentsIfNeeded().forPath("/users", "admin".getBytes());
        //查询节点数据
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/users");
        System.out.println(new String(bytes));
        client.close();
    }
}
```
## 参考文档
- [zookeeper安装官方文档](https://zookeeper.apache.org/doc/r3.9.3/zookeeperStarted.html)
- [curator客户端官网](https://curator.apache.org/docs/about)
