'''
Created on May 4, 2018
@author: umartine


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-04   umartine    Initial creation
    2018-05-24   umartine    Create FS details
    2018-11-26   umartine    Change to use Paramiko as remote runner
    2018-12-05   umartine    Support for compartment selection

'''

import oci_cli_qa.lib.operations.compute as compute
import oci_cli_qa.lib.operations.vcn as vcn
import oci_cli_qa.lib.operations.file_system as file_system
import oci_cli_qa.lib.operations.identity as identity

from oci_cli_qa.lib.runner import run_command_paramiko
from oci_cli_qa.lib.logger import LOG

PATH = "/mnt/nfs/home"
CONSUMER_FILE = "consumer_file.txt"
INSTALL_NFS_SUBCOMMAND = "sudo yum -y install nfs-utils"
CREATE_MDIR_SUBCOMMAND = "sudo mkdir -p {0}"
MOUNT_FILES_SUBCOMMAND = "sudo mount {1}:/ {0}"
CREATE_FILE_SUBCOMMAND = "sudo dd if=/dev/zero of={0}/{1} bs=1048576 count={2}"
GETSZE_FILE_SUBCOMMAND = "sudo du {0}/{1}"
LISTOF_FILE_SUBCOMMAND = "ls {0}"


def create_file_system(config, fs_config):
    fs_name = fs_config["display_name"]
    mt_name = fs_config["mount_target_name"]
    compartment_id = fs_config["compartment"]
    ad = identity.get_availability_domain(config, fs_config["availability_domain"])
    vcn_name = fs_config["vcn"]
    file_system.create_fs_e2e(config, fs_name, mt_name, compartment_id,
                              ad.name, vcn_name)


def functional_file_systen(config, fs_config):
    mt_name = fs_config["mount_target_name"]
    compartment_id = fs_config["compartment"]
    instance_name = fs_config["instance_name"]
    ssh_key = fs_config["ssh_key"]
    ad = identity.get_availability_domain(config, fs_config["availability_domain"])
    vcn_client = vcn.get_vcn_client(config)
    ip_address = compute.get_instance_public_ip(config, compartment_id,
                                                instance_name)
    mount_target = file_system.get_mount_target(config, mt_name,
                                                compartment_id, ad)
    ip_id = mount_target.private_ip_ids
    mount_target_ip = vcn_client.get_private_ip(ip_id).data.ip_address
    LOG.info("Installing NFS Utils")
    cmd_output = run_command_paramiko(ip_address, ssh_key, INSTALL_NFS_SUBCOMMAND)
    assert cmd_output["rc"] == 0
    LOG.info("Creating Mount Directory")
    command = CREATE_MDIR_SUBCOMMAND.format(PATH)
    cmd_output = run_command_paramiko(ip_address, ssh_key, command)
    assert cmd_output["rc"] == 0
    LOG.info("Mounting FS")
    command = MOUNT_FILES_SUBCOMMAND.format(PATH, mount_target_ip)
    cmd_output = run_command_paramiko(ip_address, ssh_key, command)
    assert cmd_output["rc"] == 0
    pass


def upsize_file_systen(config, fs_config):
    compartment_id = fs_config["compartment"]
    amount = fs_config["ammount"]
    instance_name = fs_config["instance_name"]
    ssh_key = fs_config["ssh_key"]
    ip_address = compute.get_instance_public_ip(config, compartment_id,
                                                instance_name)
    LOG.info("Create consumer file")
    command = CREATE_FILE_SUBCOMMAND.format(PATH, CONSUMER_FILE, amount)
    cmd_output = run_command_paramiko(ip_address, ssh_key, command)
    assert cmd_output["rc"] == 0


def downsize_file_systen(config, fs_config):
    compartment_id = fs_config["compartment"]
    amount = fs_config["ammount"]
    instance_name = fs_config["instance_name"]
    ssh_key = fs_config["ssh_key"]
    ip_address = compute.get_instance_public_ip(config, compartment_id,
                                                instance_name)    
    LOG.info("Get old size of file")
    command = GETSZE_FILE_SUBCOMMAND.format(PATH, CONSUMER_FILE)
    cmd_output = run_command_paramiko(ip_address, ssh_key, command)
    assert cmd_output["rc"] == 0
    LOG.info("Calculate new ammount")
    current_ammount = int(cmd_output["stdout"].split()[0]) / 1024
    new_ammount = amount - current_ammount
    LOG.info("Create new consumer file")
    command = CREATE_FILE_SUBCOMMAND.format(PATH, CONSUMER_FILE, new_ammount)
    cmd_output = run_command_paramiko(ip_address, ssh_key, command)
    assert cmd_output["rc"] == 0


def terminate_file_system(config, fs_config):
    fs_name = fs_config["display_name"]
    mt_name = fs_config["mount_target_name"]
    compartment_id = fs_config["compartment"]
    ad = identity.get_availability_domain(config, fs_config["availability_domain"])
    file_system.delete_fs_e2e(config, fs_name, mt_name, compartment_id,
                              ad.name)


FILE_SYSTEM_OPS = {"CREATE":     create_file_system,
                   "FUNCTIONAL": functional_file_systen,
                   "UPSIZE":     upsize_file_systen,
                   "DOWNSIZE":   downsize_file_systen, 
                   "TERMINATE":  terminate_file_system
                   }
