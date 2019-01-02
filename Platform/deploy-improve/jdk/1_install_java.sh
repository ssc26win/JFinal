#!/bin/sh
java_install_path=/usr/local/java/jdk
java_full_file=jdk-7u51-linux-x64.tar.gz
java_full=jdk1.7.0_51
java_short=java


rm -rf "$java_install_path/$java_full" "$java_install_path/$java_short"

#cp java install file to install detsination
tar -xzf $java_full_file -C $java_install_path
#ln -s $java_install_path$java_full $java_install_path$java_short

