#实惠集成测试

### 支持scalatest、specs2、junt4测试

### 安装：

1、安装idea scala插件   

2、配置sbt默认maven仓库

编辑 ～/.sbt/repositories 

    [repositories]
      local
      my-maven-proxy-releases: http://repo.hiwemeet.com/nexus/content/groups/public 
      
3、生成idea配置文件
    
    ./sbt gen-idea
    
4、运行测试

    ./sbt test