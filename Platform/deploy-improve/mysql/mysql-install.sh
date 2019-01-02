#!/bin/bash
 tar -xvf mysql-5.5.31.tar.gz
 cd mysql-5.5.31
 cmake . -DCMAKE_INSTALL_PREFIX=/usr/local/mysql -DDEFAULT_CHARSET=utf8 -DDEFAULT_COLLATION=utf8_general_ci -DWITH_EXTRA_CHARSETS=utf8,gb2312  -DENABLED_LOCAL_INFILE=1 -DWITH_INNOBASE_STORAGE_ENGINE=1  -DWITH_ARCHIVE_STORAGE_ENGINE=1 -DWITH_FEDERATED_STORAGE_ENGINE=1   -DWITH_PARTITION_STORAGE_ENGINE=1 -DMYSQL_TCP_PORT=8904 -DMYSQL_UNIX_ADDR=/tmp/mysqld.sock -DSYSCONFDIR=/etc -DMYSQL_DATADIR=/usr/local/mysql/db -DMYSQL_USER=mysql
 make
 make install
 cd /usr/local/mysql/
 groupadd mysql
 useradd -g mysql -M -s /sbin/nologin mysql
 chown -R mysql.mysql .
 scripts/mysql_install_db --user=mysql --datadir=/usr/local/mysql/db
 chown -R root .
 chown -R mysql.mysql /usr/local/mysql/db
 rm -fr /etc/my.cnf
 cd -
 cd ..
 cp my.cnf /etc/my.cnf
 cd /usr/local/mysql
 cp support-files/mysql.server /etc/init.d/mysqld
 chmod 755 /etc/init.d/mysqld
 chkconfig --add mysqld
 ln -s /usr/local/mysql/bin/mysql /usr/bin/
 rm -fr COPYING
 rm -fr INSTALL-BINARY
 rm -fr README
 rm -fr support-files
