#!/bin/sh

# edit /etc/profile
t=/etc/profile
echo " " >> $t
echo "# add $(date)" >> $t
echo "JAVA_HOME=/usr/local/java/jdk/jdk1.7.0_51" >> $t
echo "export JRE_HOME=/usr/local/java/jdk/jdk1.7.0_51/jre" >> $t
echo "export CLASSPATH=.:\$JAVA_HOME/lib:\$JRE_HOME/lib:\$CLASSPATH" >> $t
echo "export PATH=\$JAVA_HOME/bin:\$JRE_HOME/bin:\$PATH" >> $t


update-alternatives --install /usr/bin/java java /usr/local/java/jdk/jdk1.7.0_51/bin/java 300
update-alternatives --install /usr/bin/javac javac /usr/local/java/jdk/jdk1.7.0_51/bin/javac 300
update-alternatives --config java
update-alternatives --config javac
