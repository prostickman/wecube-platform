# WeCube安装指引

WeCube运行环境包括3个组件：wecube-app、wecube-db(mysql)、minio(对象存储),这三个组件的安装包以docker镜像的方式提供，本安装指引通过docker-compose的方式启动WeCube服务，不需要单独安装mysql和minio对象存储；如果用户需要使用已存在的mysql和minio对象存储，修改部分配置文件即可。

## 安装前准备
1. 准备一台linux主机，资源配置建议为4核8GB或以上。
2. 操作系统版本建议为ubuntu16.04以上或centos7.3以上。
3. 建议网络可通外网(需从外网下载部分软件)。
4. 安装docker1.17.03.x以上版本及docker-compose命令。
     - docker安装请参考[docker安装文档](https://github.com/WeBankPartners/we-cmdb/blob/master/cmdb-wiki/docs/install/docker_install_guide.md)
     - docker-compose安装请参考[docker-compose安装文档](https://github.com/WeBankPartners/we-cmdb/blob/master/cmdb-wiki/docs/install/docker-compose_install_guide.md)
5. 确认cmdb已经部署并能正常访问
	
	需要知道cmdb的访问ip以及端口。

	确认cmdb的api访问ip白名单列表中已包含wecube部署主机的ip。
	
	可查看wecmdb的安装文档中cmdb.cfg配置文件中的配置项：

	```
	cmdb_ip_whitelists={$cmdb_ip_whitelists}
	```
	
	如果wecmdb已经运行， 需要修改此配置， 则需要进入wecmdb的应用容器中， 修改start.sh脚本文件中的配置参数：
	
	```
	--cas-server.whitelist-ipaddress=127.0.0.1
	```

	127.0.0.1替换成wecube所在主机的ip，重启wecmdb服务。


## 加载镜像
    
   通过文件方式加载镜像，执行以下命令：

   ```
   docker load --input wecube-platform.tar
   docker load --input wecube-db.tar 
   ```

   执行docker images 命令，能看到镜像已经导入：

   ![wecube-platform_images](images/wecube-platform_images.png)

   记下镜像列表中的镜像名称以及TAG， 在下面的配置中需要用到。

## 配置
1. 建立执行目录和相关文件
   
	在部署机器上建立安装目录，新建以下三个文件:

	[wecube.cfg](../../../build/wecube.cfg)

	[install.sh](../../../build/install.sh)

	[uninstall.sh](../../../build/uninstall.sh)

	[docker-compose.tpl](../../../build/docker-compose.tpl)


2. 编辑wecube.cfg配置文件，该文件包含如下配置项，用户根据各自的部署环境替换掉相关值。

	```
	#wecube-core
	wecube_server_port=9090
	wecube_image_name={$wecube_image_name}
	wecube_plugin_hosts=127.0.0.1,127.0.0.2
	wecube_plugin_host_port=22
	wecube_plugin_host_user={$plugin_host_user_name}
	wecube_plugin_host_pwd={$plugin_host_password}
	
	#cmdb
	cmdb_url=http://{$cmdb_server_ip}:{$cmdb_server_port}/cmdb
	
	#database
	database_image_name={$wecube_database_image_name}
	database_init_password={$wecube_database_init_password}
	
	#s3
	s3_url=http://{$minio_server_ip}:9000
	s3_access_key=access_key
	s3_secret_key=secret_key
	```

	配置项                      |说明
	---------------------------|--------------------
	wecube_server_port         |wecube的服务端口
	wecube_image_name          |wecube的docker镜像名称及TAG，请填入在“加载镜像”章节中看到的镜像名称以及TAG，需要保持一致， 例如：wecube-platform:bd4fbec
	wecube_plugin_hosts        |部署wecube插件的容器母机ip，每一台母机上都需要预装docker
	wecube_plugin_host_port    |wecube部署插件主机的ssh端口
	wecube_plugin_host_user    |wecube部署插件主机的ssh用户，最好是root用户，若是其他用户，请保证该用户有执行docker命令的权限和/opt目录的读写权限
	wecube_plugin_host_pwd     |wecube部署插件主机的ssh密码
	cmdb_url                   |wecube依赖的cmdb服务url
	database_image_name        |wecube数据库镜像名称及TAG，请填入在“加载镜像”章节中看到的镜像名称以及TAG，需要保持一致， 例如：wecube-db:dev
	database_init_password     |wecube数据库初始化密码
	s3_url                     |wecube依赖的对象存储服务器地址，docker-compose.tpl中已经包含minio的S3服务，此处填部署主机ip
	s3_access_key              |minio对象存储访问access_key
	s3_secret_key              |minio对象存储访问secret_key

3. 编辑install.sh文件， 代码内容如下：

	```
	#!/bin/bash
	set -ex
	if ! docker --version &> /dev/null
	then
	    echo "must have docker installed"
	    exit 1
	fi
	
	if ! docker-compose --version &> /dev/null
	then
	    echo  "must have docker-compose installed"
	    exit 1
	fi
	
	source wecube.cfg
	
	sed  "s~{{WECUBE_DATABASE_IMAGE_NAME}}~$database_image_name~" docker-compose.tpl >  docker-compose.yml  
	sed -i "s~{{WECUBE_IMAGE_NAME}}~$wecube_image_name~" docker-compose.yml  
	sed -i "s~{{WECUBE_SERVER_PORT}}~$wecube_server_port~" docker-compose.yml 
	sed -i "s~{{MYSQL_ROOT_PASSWORD}}~$database_user_password~" docker-compose.yml 
	sed -i "s~{{CMDB_SERVER_URL}}~$cmdb_url~" docker-compose.yml 
	sed -i "s~{{WECUBE_PLUGIN_HOSTS}}~$wecube_plugin_hosts~" docker-compose.yml
	sed -i "s~{{WECUBE_PLUGIN_HOST_PORT}}~$wecube_plugin_host_port~" docker-compose.yml
	sed -i "s~{{WECUBE_PLUGIN_HOST_USER}}~$wecube_plugin_host_user~" docker-compose.yml
	sed -i "s~{{WECUBE_PLUGIN_HOST_PWD}}~$wecube_plugin_host_pwd~" docker-compose.yml
	sed -i "s~{{S3_URL}}~$s3_url~" docker-compose.yml
	sed -i "s~{{S3_ACCESS_KEY}}~$s3_access_key~" docker-compose.yml
	sed -i "s~{{S3_SECRET_KEY}}~$s3_secret_key~" docker-compose.yml
	
	docker-compose  -f docker-compose.yml  up -d
	
	```

4. uninstall.sh文件。

	```
	#!/bin/bash
	docker-compose -f docker-compose.yml down -v
	```

5. docker-compose.tpl文件
	
	此文件中配置了要安装的服务:wecube、mysql和minio。
	
	如果已有minio和mysql，在文件中将这两段注释掉,在wecube的environment配置中,手动修改s3和数据库配置即可。
	
	详细代码如下:

	```
	version: '2'
	services:
	  minio:
	    image: minio/minio
	    restart: always
	    command: [
	        'server',
	        'data'
	    ]
	    ports:
	      - 9000:9000
	    volumes:
	      - /data/minio-storage/data:/data    
	      - /data/minio-storage/config:/root
	      - /etc/localtime:/etc/localtime
	    environment:
	      - MINIO_ACCESS_KEY={{S3_ACCESS_KEY}}
	      - MINIO_SECRET_KEY={{S3_SECRET_KEY}}
	  mysql:
	    image: {{WECUBE_DATABASE_IMAGE_NAME}}
	    restart: always
	    command: [
	            '--character-set-server=utf8mb4',
	            '--collation-server=utf8mb4_unicode_ci',
	            '--default-time-zone=+8:00'
	    ]
	    environment:
	      - MYSQL_ROOT_PASSWORD={{MYSQL_ROOT_PASSWORD}}
	    volumes:
	      - /data/wecube/db:/var/lib/mysql
	      - /etc/localtime:/etc/localtime
	  wecube:
	    image: {{WECUBE_IMAGE_NAME}}
	    restart: always
	    depends_on:
	      - mysql
	    volumes:
	      - /data/wecube/log:/log/ 
	      - /etc/localtime:/etc/localtime
	    networks:
	      - wecube-core
	    ports:
	      - {{WECUBE_SERVER_PORT}}:8080
	    environment:
	      - TZ=Asia/Shanghai
	      - MYSQL_SERVER_ADDR=wecube-mysql
	      - MYSQL_SERVER_PORT=3306
	      - MYSQL_SERVER_DATABASE_NAME=wecube
	      - MYSQL_USER_NAME=root
	      - MYSQL_USER_PASSWORD={{MYSQL_ROOT_PASSWORD}}
	      - CMDB_SERVER_URL={{CMDB_SERVER_URL}}
	      - WECUBE_PLUGIN_HOSTS={{WECUBE_PLUGIN_HOSTS}}
	      - WECUBE_PLUGIN_HOST_PORT={{WECUBE_PLUGIN_HOST_PORT}}
	      - WECUBE_PLUGIN_HOST_USER={{WECUBE_PLUGIN_HOST_USER}}
	      - WECUBE_PLUGIN_HOST_PWD={{WECUBE_PLUGIN_HOST_PWD}}
	      - S3_ENDPOINT={{S3_URL}}
	      - S3_ACCESS_KEY={{S3_ACCESS_KEY}}
	      - S3_SECRET_KEY={{S3_SECRET_KEY}}
	```

## 执行安装
1. 执行如下命令，通过docker-compose拉起WeCube服务。

	```
	/bin/bash ./install.sh
	```

2. 安装后检查
	访问WeCube的url http://wecube_server_ip:wecube_server_port 确认页面访问正常。


## 卸载
执行如下命令，通过docker-compose停止WeCube服务。

```
/bin/bash ./uninstall.sh
```

## 重启
执行如下命令，通过docker-compose停止WeCube服务。

```
/bin/bash ./uninstall.sh
```

根据需要修改wecube.cfg配置文件，重启服务

```
/bin/bash ./install.sh
```
