##### 1.配置用户名(提交时会引用)
###### git config --global user.name "你的用户名"
##### 2.配置邮箱
###### git config --global user.email "你的邮箱"
##### 3.编码配置
###### 避免git gui中的中文乱码
###### git config --global gui.encoding utf-8
###### 避免 git status显示的中文文件名乱码
###### git config --global core.quotepath off
##### 4.其他
###### git config --global core.ignorecase false
##### git ssh key pair配置
##### 1.在git bash命令行窗口中输入：
###### ssh-keygen -t rsa -C "你的邮箱"
##### 2.然后一路回车，不要输入任何密码之类，生成ssh key pair
##### 3.在用户目录下生成.ssh文件夹，找到公钥和私钥
###### id_rsa id_rsa.pub
##### 4.将公钥的内容复制
##### 5.进入github网站，将公钥添加进去

git 验证
##### 执行git version ,出现版本信息,安装成功
git常用命令
##### git init 创建本地仓库
##### git add 添加到暂存区
##### git commit -m "描述"提交到本地仓库
##### git status 检查工作区文件状态
##### git log 查看提交committed
##### git reset --hard committed 版本回退
##### git branch 查看分支
##### git checkout -b dev 创建并转到dev分支
##### git checkout 分支名    切换分支
##### git pull 拉取
##### git push -u origin master 提交
##### git merge branchname 分支合并
##### git remote add origin 远程仓库地址
##### git push -u -f origin master 第一次向远程仓库推送
##### git push origin master 提交到远程
#### -----------------

#### 项目架构-四层架构
```
    上层依赖下层
    
    视图层
    控制层(Controller)-->接收视图层传递的数据和调用业务逻辑
    业务逻辑层(Service)-->负责具体的业务逻辑
        接口和实现类
    Dao层 -->与数据库做交互
    
```
#### Mybatis--generator插件
```
可直接生成dao接口，实体类和mapper文件
```
#### 搭建ssm框架
##### 新建项目，设置java文件夹（放代码）、resources（放资源文件）文件夹类型
##### generatorConfig.xml放入resources文件夹中
##### 配置pom.xml，引入MySQL驱动包和mybatis-generator依赖  
```
 <!-- mysql驱动包 -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.47</version>
    </dependency>
  
    <!--mybatis-generator依赖-->
    <dependency>
      <groupId>org.mybatis.generator</groupId>
      <artifactId>mybatis-generator-core</artifactId>
      <version>1.3.5</version>
    </dependency>
```
##### 引入插件,org.mybatis.generator,在<pluginManagement>上面
```
      <plugins>
          <plugin>
              <groupId>org.mybatis.generator</groupId>
              <artifactId>mybatis-generator-maven-plugin</artifactId>
              <version>1.3.6</version>
              <configuration>
                  <verbose>true</verbose>
                  <overwrite>true</overwrite>
              </configuration>
          </plugin>
```

##### 在resources文件夹下创建db.properties文件，输入名字、密码、网址、驱动，为了防止插件默认加载系统的名字，最好加入前缀
```
       jdbc.username=root
       jdbc.password=345513
       jdbc.driver=com.mysql.jdbc.Driver
       jdbc.url=jdbc:mysql://localhost:3306/ilearnshopping 
```
##### 下一步，在generatorConfig.xml 中配置db.properties（位置在<generatorConfiguration>命令的下面第一行加入命令）
```
 <properties resource="db.properties"></properties>
```
##### 下一步，当前文件下配置MySQL依赖包，输入jar包具体路径在本机找路径
```
<classPathEntry location="C:\Users\dell\.m2\repository\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar"/>
```
##### 下一步，配置当前文件下的jdbc数据，配置数据库用${}
```
 <jdbcConnection userId="${jdbc.username}" password="${jdbc.password}" driverClass="${jdbc.driver}" connectionURL="${jdbc.url}"/>
```
##### 下一步，配置实体类，SQL文件，Dao接口
```
    ===============================================================================
游侠:
简写：
    com.neuedu.pojo         src/main/java
    com.neuedu.mapper       src/main/resources
    com.neuedu.dao          src/main/java
    ============================================================================
    明细：
     <!-- 实体类-->
            <javaModelGenerator targetPackage="com.neuedu.pojo" targetProject="src/main/java">
     <!--配置sql文件-->
            <sqlMapGenerator targetPackage="com.neuedu.mapper" targetProject="src/main/resources">
     <!--生成Dao接口-->
            <javaClientGenerator targetPackage="com.neuedu.dao" type="XMLMAPPER" targetProject="src/main/java">
     
     *重点注意：目录的书写方式
     =================================================================================
```
##### 配数据表（有几张表就有几个数据表，插件根据此表生成实体类，输入表名和实体类名字）
```
tableName="neuedu_user" domainObjectName="UserInfo"
tableName="neuedu_category" domainObjectName="Category"
tableName="neuedu_product" domainObjectName="Product" 
tableName="neuedu_cart" domainObjectName="Cart" 
tableName="neuedu_order" domainObjectName="Order"
tableName="neuedu_order_item" domainObjectName="OrderItem"
tableName="neuedu_payinfo" domainObjectName="PayInfo" 
tableName="neuedu_shipping" domainObjectName="Shipping"

```

##### 最后 右边栏的Maven Projects 里 pluging 里 mybatis-generator 里 mybatis-generator:generate   双击生成实体类、dao、mapper映射xml文件,完成。



### 搭建ssm框架步骤

##### 导入依赖
##### 统一版本号 <spring.version>4.2.0.RELEASE</spring.version>
##### 导入spring.xml springmvc.xml mybatis-config.xml文件
#### spring.xml
##### 开启注解扫包。核对更改数据源的名字
##### configLocation 全局配置文件 classpath 类路径
##### mapperLocations 映射文件  用/分割地址  *mapper.xml 任意mapper.xml文件
##### 配置mybatis Dao接口的代理实现类，动态生成代理实现类，很重要

#### mybatis-config.xml不用修改

#### springmvc.xml 
##### 开启注解，扫描包com.neuedu.controller ，也可以com.neuedu
##### 配置视图解析器、文件上传、拦截器（一期项目不用）


#### web.xml更换老师的
##### 加载spring配置文件  contextConfigLocation
##### 加载监听器 
##### 加载DispacherServlet    
##### /为缺省路径    访问 /login.do  有servlet处理login.do就交给对应的servlet处理。没有的话就交给/处理，就是dispacherservlet处理

#### 创建测试类   Testcontroller

##### @RestController 注解，往前端返回的数据是json格式
#####


#####@RequestMapping （value="/login.do"）  映射的网址，也就可以加在类上，多层级访问

#### 配置tomcat 启动输入网址http://localhost:8080/login.do ,出现json数据 完成测试


