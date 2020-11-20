# gateway-security
### 这是一个基于Gateway实现的简易认证鉴权项目
未使用第三方开源框架，只是通过一个登陆过滤器加if/else拦截判断实现



## 项目结构
#### 系统采用前后端分离开发

#### 后台使用SpringCloud Alibaba微服务架构，启动之前需要先启动Nacos服务

#### 前台为Vue项目，启动需要先配置好Node环境



## 如何启动项目？
#### 1.下载解压nacos-server-1.3.1.zip文件，
#### 2.进入nacos-server-1.3.1\nacos\bin目录
#### 3.执行startup.cmd
#### 4.等待cmd命令框出现 ``` Completed initialization in xxx ms```
#### 5.然后启动gateway 和 auth 模块
#### 6.打开```gateway-security-frontend```项目
#### 7.在项目主目录下打开第二个cmd窗口
#### 8.执行以下命令
```
npm install 

npm run dev
```

## 可能需要你自行配置的地方
#### 1.gateway 和 auth 模块中yml文件的datasource
#### 2.后端使用了Redis作为用户信息和Token的缓存，也需要您自行下载安装和修改yml中的配置

## 相关博客连接
#### [根据登陆用户动态展示Vue菜单](https://juejin.cn/post/6896732256396640264)
#### [使用Gateway网关实现用户认证与鉴权](https://juejin.cn/post/6896679498651107341)
