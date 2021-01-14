'''
Created on Mar 29, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-29   umartine    Initial creation
    2018-05-24   umartine    Add support for remote commands
    2018-08-21   umartine    Add new flag
    2018-08-22   umartine    Adding Paramiko remote command runner

'''

import subprocess
import paramiko

from  oci_cli_qa.lib.logger import LOG

SSH_COMMAD = "ssh -o 'StrictHostKeyChecking no' -i {0} opc@{1}"

def get_compute_ssh_cmd_list(ssh_key, ip_address):
    cmd = list(SSH_COMMAD)
    cmd[4] = ssh_key
    cmd[5] = cmd[5].format(ip_address)
    return cmd 

def run_command(command, need_to_wait = True):
    LOG.info("[START] Run command")
    cmd_output = {}
    LOG.info("Running command: '{0}'".format(command))
    p = subprocess.Popen(command,
                         stdin = subprocess.PIPE,
                         stdout = subprocess.PIPE,
                         stderr = subprocess.PIPE,
                         shell = False)
    if need_to_wait:
        p.wait()
        cmd_output["stdout"], cmd_output["stderr"] = p.communicate()
        cmd_output["rc"] = p.returncode
        LOG.info("Output: {0}".format(cmd_output))
    LOG.info("[END] Run command")
    return cmd_output


def run_command_remote(ip_address, ssh_key, command):
    LOG.info("[START] Run command remote")
    LOG.info("IP: '{0}'".format(ip_address))
    LOG.info("SSH Key: '{0}'".format(ssh_key))
    LOG.info("Command: '{0}'".format(command))
    cmd_remote = SSH_COMMAD.format(ssh_key, ip_address)
    cmd = "{0} {1}".format(cmd_remote, command)
    cmd_output = run_command(cmd)
    LOG.info("[END] Run command remote")
    return cmd_output


def run_command_paramiko(ip_address, ssh_key, command):
    LOG.info("[START] Run command remote with Paramiko")
    LOG.info("IP: '{0}'".format(ip_address))
    LOG.info("SSH Key: '{0}'".format(ssh_key))
    LOG.info("Running command: '{0}'".format(command))
    cmd_output = {}
    ssh_client = paramiko.SSHClient()
    ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh_client.connect(ip_address, username = 'opc', key_filename = ssh_key)
    _, stdout, stderr = ssh_client.exec_command(command)
    cmd_output["stdout"], cmd_output["stderr"] = stdout.read(), stderr.read()
    cmd_output["rc"] = stdout.channel.recv_exit_status()
    LOG.info("Output: {0}".format(cmd_output))
    LOG.info("[START] Run command remote with Paramiko")
    return cmd_output
