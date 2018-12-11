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

 数据库中拿到的是经过注册后得密文密码，而我们要用自己注册的明文密码进行登录，e而这时登陆的明文密码和对应的数据库中的密文密码肯定不一样，所以要对登录时的明文密码进行加密
      购物车只有用户登录时候才能用，当要用购物车的时候就需要判断用户是否进行了登录，登录就可以直接用，没登录对他进行提醒，登录后才能使用
      登录后信息保存在session当中
      
      检查用户名是否有效
      在注册时，页面会立刻有个反馈做时时的提示防止有恶意的调用接口，用ajax 异步加载调用接口返回数据
#### 忘记密码之修改密码
```
   step1：校验username--->查询找回密码问题
   step2：前端，提交问题答案
   step3：校验答案-->修改密码 
   
   修改密码的时候要考虑到一个越权问题
     横向越权：权限都是一样的，a用户去修改其他用户  
     纵向越权：低级别用户修改高级别用户的权限
     解决：提交答案成功的时候，服务端会给客户端返回一个值（数据），这个数据在客户端服务端都分别保存，当用户去重置密码的时候，用户端必须带上这个数据，只有拿到数据服务端校验合法了才能修改
          所以服务端要给客户端传一个token,服务端客户端都分别保存，然后两个进行校验
          
   @JsonSerialize:json是个键值对往页面传对象的时候是通过，扫描类下的get方法来取值的
          
   UUID生成的是一个唯一的随机生成一个字符串，每次生成都是唯一的
   ```
 #### 根据用户名查询密保问题
     step1：参数非空校验
     step2：校验username是否存在
     step3：根据username查询密保问题
     step4：返回结果
 
 ##### 提交问题答案
     step1：参数非空校验
     step2：校验答案：根据username,question,answer查询，看看有没有这条记录
     step3：服务端生成一个token保存并将token返回给客户端
        String user_Token=UUID.randomUUID().toString();
        UUID每次生成的字符串是唯一的，不会重复的
        guava cache
        TokenCache.put(username,user_Token);
        缓存里用key或取，key要保证他的唯一性，key就是用户，key直接用value就可以了
        这样就把token放到服务端的缓存里面了，同时要将token返回到客户端
     step4：返回结果
 ##### 修改密码
     step1：参数的非空校验
     step2：校验token
     step3：更新密码
     step4：返回结果
 ### 登录状态下修改密码
     step1：参数的非空校验
     step2：校验旧密码是否正确,根据用户名和旧密码查询这个用户
     step3：修改密码
     step4：返回结果
     在控制层中要先判断是否登录
     
 ### 类别模块    
 #### 1：功能介绍
      获取节点
      增加节点
      修改名称
      获取分类
      递归子节点
  #### 2：学习目标
       如何设计界封装无限层级的树状数据结构
       递归算法的设计思想
         递归一定要有一个结束条件，否则就成了一个死循环
       如何处理复杂对象重排
       重写hashcode和equals的注意事项  
  #### 获取品类子节点（平级）
       step1：非空校验
       step2：根据categoryId查询类别
       step3：查询子类别
       step4：返回结果
  #### 增加节点
       step1：非空校验
       step2：添加节点
       step3：返回结果
  #### 修改节点
       step1：非空校验
       step2：根据categoryId查询类别
       step3：修改类别
       step4：返回结果
  #### 获取当前分类id及递归子节点categoryId
      先定义一个递归的方法
        先查找本节点
            set里面的集合不可重复，通过类别id判断是不是重复，要重写类别对象的equals方法，在重写equals方法前先重写hashcode方法
           
