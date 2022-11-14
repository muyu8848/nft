环境要求：jdk1.8或以上，mysql5.7，redis随便一个版本
nft系统是maven模块化项目,用的是spring boot框架
项目用到了lombok插件，你的开发工具需要安装这个插件
1.数据库创建一个open_nft的库，用你的数据库管理工具,如navicat,执行open_nft.sql这个文件的内容,注意有视图要创建，数据库必须要有创建视图的权限
2.可在项目文件application.yml文件修改配置的mysql密码
3.nft-admin,nft-member,nft-biz分别是运营后台，app端接口，后端业务逻辑模块
3.找到application类名结未的java文件，直接启动即可
4.启动完成之后，运营后台的登录页是你的ip加端口加上/page/login，默认账号密码是admin/123
