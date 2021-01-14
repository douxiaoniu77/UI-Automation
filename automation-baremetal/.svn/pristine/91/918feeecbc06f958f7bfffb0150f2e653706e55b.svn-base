#!/bin/sh

JOB_BUILD_URL=$1

mkdir -p ${WORKSPACE}/target/surefire-reports;

efile=${WORKSPACE}'/target/surefire-reports/emailable-report.html';
echo $efile;
if [ ! -f $efile ]; then
    cp ${WORKSPACE}/email/default_email.html ${WORKSPACE}/target/surefire-reports/emailable-report.html;
	mkdir -p ${WORKSPACE}/target/surefire-reports/email;
	cat ${WORKSPACE}/email/default_subject.txt > ${WORKSPACE}/target/surefire-reports/email/subject.txt;
	echo "The failed job at <a href='$JOB_BUILD_URL'>$JOB_BUILD_URL</a><br/>" > ${WORKSPACE}/target/surefire-reports/email/tmp.html
	cat ${WORKSPACE}/target/surefire-reports/emailable-report.html >> ${WORKSPACE}/target/surefire-reports/email/tmp.html
	cat ${WORKSPACE}/target/surefire-reports/email/tmp.html | formail -I "From:c9_ui_automation_grp@oracle.com" -I "MIME-Version:1.0" -I "Content-type:text/html" -I "Subject:Please ignore this build" |/usr/sbin/sendmail -oi "samuel.yang@oracle.com,xiao.pan@oracle.com"
fi