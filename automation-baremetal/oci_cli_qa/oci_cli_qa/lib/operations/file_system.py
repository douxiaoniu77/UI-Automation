'''
Created on May 4, 2018
@author: umartine


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-04   umartine    Initial creation
    2018-05-23   umartine    Create FS details
    2018-05-24   umartine    FS end to end scenarios
    2018-08-01   umartine    Update delete export flow

'''

import oci

import oci_cli_qa.lib.operations.vcn as vcn

from oci_cli_qa.lib.logger import LOG

def get_file_system_client(config):
    LOG.info("Get File System Client")
    return oci.file_storage.FileStorageClient(config)

'''
-------------------------------------------------------------------------------
                                File System Operations
-------------------------------------------------------------------------------
'''

def get_file_system(config, fs_name, compartment_id, ad):
    LOG.info("[START] Get File System")
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    file_system = None
    file_system_list = fs_client.list_file_systems(compartment_id, ad).data
    for file_system_aux in file_system_list:
        if file_system_aux.display_name == fs_name:
            file_system = file_system_aux
            break
    LOG.info(file_system)
    LOG.info("[END] Get File System")
    return file_system


def create_file_system(config, fs_name, compartment_id, ad):
    LOG.info("[START] Create File System")
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    fs_details = oci.file_storage.models.CreateFileSystemDetails(
            display_name = fs_name,
            compartment_id = compartment_id,
            availability_domain = ad
        )
    create_response = fs_client.create_file_system(fs_details,
                                                   retry_strategy = oci.retry.DEFAULT_RETRY_STRATEGY)
    file_system = fs_client.get_file_system(create_response.data.id)
    LOG.info("Wait the operation to complete")
    file_system = oci.wait_until(fs_client, file_system, 'lifecycle_state',
                                 'ACTIVE').data
    LOG.info(file_system)
    LOG.info("[END] Create File System")
    return file_system


def delete_file_system(config, fs_name, compartment_id, ad):
    LOG.info("[START] Delete File System")
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    file_system = get_file_system(config, fs_name, compartment_id, ad)
    fs_client.delete_file_system(file_system.id)
    file_system = fs_client.get_file_system(file_system.id)
    oci.wait_until(fs_client, file_system, 'lifecycle_state', 'DELETED',
                   succeed_on_not_found = True)
    LOG.info("[END] Delete File System")


'''
-------------------------------------------------------------------------------
                                Mount Targets Operations
-------------------------------------------------------------------------------
'''

def list_mount_targets(config, compartment_id, ad):
    LOG.info("[START] List Mount Targets")
    fs_client = get_file_system_client(config)
    LOG.info("Get Mount Targets")
    mount_targets = fs_client.list_mount_targets(compartment_id, ad).data
    LOG.info(mount_targets)
    LOG.info("[END] List Mount Targets")
    return mount_targets


def get_mount_target(config, mt_name, compartment_id, ad):
    LOG.info("[START] Get Mount Target")
    LOG.info("Mount Target Name: '{0}'".format(mt_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    mount_target = None
    mount_target_list = fs_client.list_mount_targets(compartment_id, ad).data
    for mount_target_aux in mount_target_list:
        if mount_target_aux.display_name == mt_name:
            mount_target = mount_target_aux
            break
    LOG.info(mount_target)
    LOG.info("[END] Get Mount Targets")
    return mount_target


def create_mount_target(config, mt_name, compartment_id, ad, vcn_name = ""):
    LOG.info("[START]Create Mount Target")
    LOG.info("Mount Target Name: '{0}'".format(mt_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("VCN name: '{0}'".format(vcn_name))
    LOG.info("Availability Domain: '{0}'".format(ad))
    vnc = vcn.get_vcn(config, compartment_id, vcn_name)
    subnet = vcn.get_subnet(config, compartment_id, vnc.id, ad = ad)
    fs_client = get_file_system_client(config)
    mt_details = oci.file_storage.models.CreateMountTargetDetails(
            availability_domain = ad,
            subnet_id = subnet.id,
            compartment_id = compartment_id,
            display_name = mt_name
        )
    mt_response = fs_client.create_mount_target(mt_details)
    mount_target = fs_client.get_mount_target(mt_response.data.id)
    LOG.info("Wait the operation to complete") 
    mount_target = oci.wait_until(fs_client, mount_target, 'lifecycle_state',
                                  'ACTIVE').data
    LOG.info(mount_target)
    LOG.info("[START]Create Mount Target")
    return mount_target


def delete_mount_target(config, mt_name, compartment_id, ad):
    LOG.info("[START] Delete Mount Target")
    LOG.info("Mount Target Name: '{0}'".format(mt_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    mount_target = get_mount_target(config, mt_name, compartment_id, ad)
    fs_client.delete_mount_target(mount_target.id)
    mount_target = fs_client.get_mount_target(mount_target.id)
    oci.wait_until(fs_client, mount_target, 'lifecycle_state', 'DELETED',
                   succeed_on_not_found = True)
    LOG.info("[END] Delete Mount Target")


'''
-------------------------------------------------------------------------------
                                Export and Export Sets Operations
-------------------------------------------------------------------------------
'''


def get_export_set(config, export_set_id):
    LOG.info("[START] Get Export Set '{0}'".format(export_set_id))
    fs_client = get_file_system_client(config)
    LOG.info("Get Mount Targets")
    export_set = fs_client.get_export_set(export_set_id).data
    LOG.info(export_set)
    LOG.info("[END] Get Export Set ID")
    return export_set


def create_export(config, fs_name, mt_name, compartment_id, ad):
    LOG.info("[START] Create Export")
    LOG.info("Mount Target Name: '{0}'".format(mt_name))
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    mount_target = get_mount_target(config, mt_name, compartment_id, ad)
    file_system = get_file_system(config, fs_name, compartment_id, ad)
    export_details = oci.file_storage.models.CreateExportDetails(
            export_set_id = mount_target.export_set_id,
            file_system_id = file_system.id,
            path='/'
        )
    export_inst = fs_client.create_export(export_details).data
    export_inst = fs_client.get_export(export_inst.id)
    export_inst = oci.wait_until(fs_client, export_inst, 'lifecycle_state',
                                 'ACTIVE').data
    LOG.info(export_inst)
    LOG.info("[END] Create Export")
    return export_inst


def delete_export(config, fs_name, compartment_id, ad):
    LOG.info("[START] Delete Export")
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    fs_client = get_file_system_client(config)
    file_system = get_file_system(config, fs_name, compartment_id, ad)
    export_list = fs_client.list_exports(compartment_id = compartment_id,
                                         file_system_id = file_system.id).data
    for export_inst in export_list:
        fs_client.delete_export(export_inst.id)
        export_inst = fs_client.get_export(export_inst.id)
        oci.wait_until(fs_client, export_inst, 'lifecycle_state', 'DELETED')
    LOG.info("[END] Delete Export")


'''
-------------------------------------------------------------------------------
                                End to End Scenarios
-------------------------------------------------------------------------------
'''

def create_fs_e2e(config, fs_name, mt_name, compartment_id, ad, vcn_name):
    LOG.info("[START] Create File System End to End")
    LOG.info("Mount Target Name: '{0}'".format(mt_name))
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    LOG.info("VCN name: '{0}'".format(vcn_name))
    create_file_system(config, fs_name, compartment_id, ad)
    create_mount_target(config, mt_name, compartment_id, ad, vcn_name)
    create_export(config, fs_name, mt_name, compartment_id, ad)
    LOG.info("[END] Create File System End to End")


def delete_fs_e2e(config, fs_name, mt_name, compartment_id, ad):
    LOG.info("[Delete] Create File System End to End")
    LOG.info("Mount Target Name: '{0}'".format(mt_name))
    LOG.info("File System Name: '{0}'".format(fs_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    LOG.info("Availability Domain: '{0}'".format(ad))
    delete_export(config, fs_name, compartment_id, ad)
    delete_mount_target(config, mt_name, compartment_id, ad)
    delete_file_system(config, fs_name, compartment_id, ad)
    LOG.info("[END] Delete File System End to End")
