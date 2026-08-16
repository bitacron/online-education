# Getting Started
## 1.简介
### 尚硅谷微服务项目：谷粒学院（后端）
### 添加了文章系统，并基于支持elasticsearch的检索功能

## 2.依赖中间件
### 启动项目需要：
### 开启nacos、redis、elasticsearch、kibana
### 本项目中redis、elasticsearch、kibana部署在Linux中，位于 `/opt/module`目录下

## 3.中间件启动流程：
### （1）start the redis on linux:
```shell
cd /opt/module/redis-5.0.8/
redis-server redis.conf
```


### start the kibana on linux:
```shell
cd /opt/module/kibana-7.6.1/bin
./kibana
```

### start the elasticsearch on linux:
```shell
cd /opt/module/elasticsearch-7.6.1/bin/
./elasticsearch
```

### start nacos on windows
