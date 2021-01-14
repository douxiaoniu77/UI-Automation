#!/bin/sh

mkdir -p ${WORKSPACE}/target/surefire-reports;

efile=${WORKSPACE}'/target/surefire-reports/emailable-report.html';
echo $efile;
if [ ! -f $efile ]; then
    cp ${WORKSPACE}/email/default_email.html ${WORKSPACE}/target/surefire-reports/emailable-report.html;
	mkdir -p ${WORKSPACE}/target/surefire-reports/email;
	cat ${WORKSPACE}/email/default_subject.txt > ${WORKSPACE}/target/surefire-reports/email/subject.txt;
fi