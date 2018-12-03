#####1.配置用户名(提交时会引用)
###### git config --global user.name "你的用户名"
#####2.配置邮箱
###### git config --global user.email "你的邮箱"
#####3.编码配置
###### 避免git gui中的中文乱码
###### git config --global gui.encoding utf-8
###### 避免 git status显示的中文文件名乱码
###### git config --global core.quotepath off
#####4.其他
###### git config --global core.ignorecase false
#####git ssh key pair配置
#####1.在git bash命令行窗口中输入：
###### ssh-keygen -t rsa -C "你的邮箱"
#####2.然后一路回车，不要输入任何密码之类，生成ssh key pair
#####3.在用户目录下生成.ssh文件夹，找到公钥和私钥
###### id_rsa id_rsa.pub
#####4.将公钥的内容复制
#####5.进入github网站，将公钥添加进去

git 验证
#####执行git version ,出现版本信息,安装成功
git常用命令
##### git init 创建本地仓库
#####git add 添加到暂存区
#####git commit -m "描述"提交到本地仓库
#####git status 检查工作区文件状态
#####git log 查看提交committed
#####git reset --hard committed 版本回退
#####git branch 查看分支
#####git checkout -b dev 创建并转到dev分支
#####git checkout 分支名    切换分支
#####git pull 拉取
#####git push -u origin master 提交
#####git merge branchname 分支合并
#####git remote add origin 远程仓库地址
#####git push -u -f origin master 第一次向远程仓库推送
#####git push origin master 提交到远程
