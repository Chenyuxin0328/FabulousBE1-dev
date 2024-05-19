# Fabulous后端开发指南

哈喽，各位小伙伴，大家好！本文内容主要介绍如何快速搭建JAVA17-Springboot3后端框架。

## 开发环境准备

1. 首先按照Docker指南安装好Docker，同时在对应的目录下创建好cli-plugins文件夹，从  
   https://github.com/docker/compose/releases/tag/v2.26.1  
   中下载对应平台的docker compose创建，置于cli-plugins文件夹中。  
   window对应的文件是docker-compose-windows-x86_64.exe，下载后文件名修改为docker-compose.exe

2. 下载IDEA IDE到本地，并进行安装。这里推荐申请教育版一年免费使用，https://www.jetbrains.com.cn/community/education/#students 。教育邮箱请使用自己的学号在 https://mail.fzu.edu.cn 中申请。

## 项目开发
1. Fork本项目到自己的用户空间下，并克隆（clone）到本地，请勿直接克隆（clone）本项目；

2. 右上角的配置中打开编辑，选择下载AWS的corretto-17版本，下载后配置好点击应用并保存；

3. 点击右上角的运行，此时项目成功运行，并且开放了Swagger文档在 http://localhost:5083/swagger-ui/index.html 中。

## 项目部署
1. 用idea打开docker-compose.yml文件，点击文件services项的运行按钮，此时会弹出配置文件；

2. 在配置文件中的Server栏中，创建一个新的Docker配置，其中Daemon设置为TCP socket，URL设置为https://59.77.134.42:2376，证书文件夹设置为docker指南中TLSPath下的docker文件夹，点击OK保存并应用。

3. 点击项目右侧的Maven，选择Fabulous->Lifecycle->package进行项目打包，这个操作会在target中生成对应的jar包。

4. 右上角配置文件切换到Compose Deployment，点击执行即完成远程部署。

## 前后端对接

1. 前端采用了Swagger自动化客户端代码生成脚本技术，因此请确保相关的Swagger文档可以通过 https://github.com/minskiter/axios-swagger-helper 中的脚本进行生成。

## 提问的艺术

如果对本项目有任何疑问，可以在群里中进行提问，但提问之前你应该确保：
1. 使用Google搜索过，但是没有找到对应的解决方案；
2. 在Stackoverflow搜索过，但没有解决；
3. 使用ChatGPT咨询过，但没有解决；
4. 查阅官方文档，没有相应的解决方案。

提问应满足：问题所在截图 + 如何复现这个问题。

## 敏感信息处理⚠️

请使用.gitignore忽略任何和数据库密码等有关的信息，任何密钥都不允许上传到github远程仓库中。如果错误上传敏感信息，请参考博文https://mskter.com/2021/04/22/git-data-desensitization-zh/ 进行删除处理（代码删除需谨慎！）

## 关于项目

本项目由福州大学服务外包创新创业实验室及零一网络有限公司赞助。
