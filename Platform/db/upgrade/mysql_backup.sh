#!/bin/bash

#保存备份个数
number=3
#备份保存路径
backup_dir=/usr/local/mysql/mysqldb-backup
#日期
dd=`date +%Y%m%d`
#备份工具
tool=/usr/local/mysql/bin/mysqldump
#用户名
username=root
#密码
password=yc201708291a2s3dqwe
#将要备份的数据库
database_name=p_platform

#简单写法 mysqldump -u root -pyc201708291a2s3dqwe p_platform > /root/mysqlbackup/p_platform-$filename.dump

$tool -u $username -p$password $database_name > $backup_dir/$database_name-$dd.dump

#写创建备份日志
echo "create $backup_dir/$database_name-$dd.dump" >> $backup_dir/backup.log

#找出需要删除的备份
delfile=`ls -l -crt  $backup_dir/*.dump | awk '{print $9 }' | head -1`

#判断现在的备份数量是否大于$number
count=`ls -l -crt  $backup_dir/*.dump | awk '{print $9 }' | wc -l`

if [ $count -gt $number ]
then
#删除最早生成的备份，只保留number数量的备份
  rm $delfile
  #写删除文件日志
  echo "delete $delfile" >> $backup_dir/backup.log
fi

echo "synchronous db $backup_dir/$database_name-$dd.dump to yc_platform" >> $backup_dir/backup.log

mysql -u $username -p$password yc_platform < $backup_dir/$database_name-$dd.dump
