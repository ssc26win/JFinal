#!/bin/sh
src=apache-tomcat-7.0.52.tar.gz

install_path=/usr/local/tomcat7
full_name=apache-tomcat-7.0.52
short_name=tomcat

#rm -rf ${install_path}${full_name} ${install_path}${short_name}

#cp tomcat install file to install detsination
tar -xzvf ${src} -C ${install_path}
#ln -s ${install_path}${full_name} ${install_path}${short_name}
