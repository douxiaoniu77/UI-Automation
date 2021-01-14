'''
Created on Apr 27, 2018
@author: Qin

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-04-27   qxionng     Initial creation
    2018-06-12   umartine    Remove Status requirement 
    2018-06-12   umartine    Functional test for DB
    2018-06-20   umartine    Backup testing
    2018-06-22   umartine    Connection details aux functions
    2018-08-03   umartine    Change Date format for Backup testing
    2018-12-04   umartine    Support for compartment selection

'''

from datetime import datetime

import oci
import oci_cli_qa.lib.operations.vcn as vcn
import oci_cli_qa.lib.operations.identity as identity

from oci_cli_qa.lib.logger import LOG

def get_dbsystem_client(config):
    LOG.info("[START] Get DbSystem Client")
    return oci.database.DatabaseClient(config)


def get_instance(config,compartment_id, instance_name):
    LOG.info("[START] Get DB System Instance")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))    
    dbsystem_client = get_dbsystem_client(config)
    check = True
    instance = None
    instances = dbsystem_client.list_db_systems(compartment_id)
    while check:
        if not instances.has_next_page:
            check = False
        for instance_aux in instances.data:
            LOG.info(instance_aux)
            if instance_name == instance_aux.display_name:
                LOG.info("Found: {0}".format(instance_aux.display_name))
                if instance_aux.lifecycle_state in ["AVAILABLE", "PROVISIONING"]:
                    LOG.info("Instance found")
                    instance = instance_aux
                    check = False
        if check:
            instances = dbsystem_client.list_db_systems(compartment_id, page = instances.next_page)
    LOG.info(instance)
    LOG.info("[END] Get DB System Instance")
    return instance


def get_vnic_attachment_id(config, compartment_id, instance_name):
    LOG.info("[START] Get VNIC attachment id")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))    
    dbsystem_client = get_dbsystem_client(config)
    db_instance = get_instance(config, compartment_id, instance_name)
    db_nodes = dbsystem_client.list_db_nodes(compartment_id, db_instance.id).data
    vnic_id = db_nodes[0].vnic_id
    LOG.info(vnic_id)
    LOG.info("[END] Get VNIC attachment id")
    return vnic_id
    

def create_instance(config, instance_cfg):
    LOG.info("[Start] Launch DB System Instance")
    LOG.info(instance_cfg)    
    dbsystem_client=get_dbsystem_client(config)
    
    LOG.info("Get subnet details")
    vcn_instance = vcn.get_vcn(config,
                               instance_cfg["compartment"],
                               instance_cfg["vcn"])

    subnet = vcn.get_subnet(config,
                            instance_cfg["compartment"],
                            vcn_instance.id,
                            subnet_name_pattern = "",
                            ad = instance_cfg["availability_domain"])
    
    ad = identity.get_availability_domain(config, instance_cfg["availability_domain"])
    
    LOG.info("Set SSH key in metadata")
    ssh_key_file = "{0}.pub".format(instance_cfg["ssh_key"])
    ssh_key = open(ssh_key_file, "r").read()
    LOG.info(ssh_key)
    ssh_public_keys = [ssh_key]
    LOG.info(ssh_public_keys)
    #instance_metadata = {"ssh_authorized_keys": ssh_key}
    
    database_details=oci.database.models.CreateDatabaseDetails(
        admin_password = instance_cfg["admin_password"],
        db_name = instance_cfg["db_name"]
        )        
    db_home=oci.database.models.CreateDbHomeDetails(
        database = database_details,
        db_version = instance_cfg["db_version"]        
        )
    LOG.info("Launch DB System")
    instance_details=oci.database.models.LaunchDbSystemDetails(        
        db_home = db_home, 
        display_name = instance_cfg["display_name"],
        availability_domain = ad.name,
        compartment_id = instance_cfg["compartment"],
        shape = instance_cfg["shape"],
        database_edition = instance_cfg["database_edition"],
        cpu_core_count = instance_cfg["cpu_core_count"],
        initial_data_storage_size_in_gb = instance_cfg["initial_data_storage_size_in_gb"],
        node_count = instance_cfg["node_count"],
        subnet_id = subnet.id,
        hostname = instance_cfg["hostname"],
        license_model = instance_cfg["license_model"],
        ssh_public_keys = ssh_public_keys
        )
    
    LOG.info("Request Sent")
    instance_response=dbsystem_client.launch_db_system(instance_details)
    LOG.info(instance_response.data)
    
    LOG.info("Wait the operation to complete")
    instance_response = oci.wait_until(
        dbsystem_client,
        dbsystem_client.get_db_system(instance_response.data.id),
        "lifecycle_state",
        "PROVISIONING"
    )
    LOG.info("Operation completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Create DB System Instance")
    return instance_response.data
    
def terminate_instance(config,instance_cfg):
    LOG.info("[Start] Terminate DB System Instance")
    LOG.info(instance_cfg)    
    dbsystem_client=get_dbsystem_client(config) 
    
    LOG.info("Get Instance details")
    dbsystem_instance = get_instance(config,
                                    instance_cfg["compartment"],
                                    instance_cfg["display_name"]
                                    )
    
    LOG.info("Terminate instance")
    dbsystem_client.terminate_db_system(dbsystem_instance.id)
    
    instance_response = oci.wait_until(
        dbsystem_client,
        dbsystem_client.get_db_system(dbsystem_instance.id),
        "lifecycle_state",
        "TERMINATED",
        succeed_on_not_found=True
    )
    
    LOG.info("Operation completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Terminate DB System Instance")
    return instance_response.data


#update db_system
def update_instance(config,instance_cfg):
    LOG.info("[START] update DB System Instance")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))    
    dbsystem_client=get_dbsystem_client(config)
    
    dbsystem_instance = get_instance(config,
                                    instance_cfg["compartment"],
                                    instance_cfg["display_name"],
                                    status = 'AVAILABLE'
                                    )
    if dbsystem_instance.shape[0:2]=="BM":
        update_db_system_details=oci.database.models.UpdateDbSystemDetails(
        cpu_core_count = instance_cfg["new_cpu_core_count"]
        )
    else:
        update_db_system_details=oci.database.models.UpdateDbSystemDetails(
        data_storage_size_in_gbs = instance_cfg["new_data_storage_size_in_gb"]
        )
    instance_response = dbsystem_client.update_db_system(
        dbsystem_instance.id,
        update_db_system_details
        )
    LOG.info(instance_response.data)
    
    #wait for status changes to AVAILABLE again
    instance_response = oci.wait_until(
        dbsystem_client,
        dbsystem_client.get_db_system(dbsystem_instance.id),
        "lifecycle_state",
        "AVAILABLE",
        succeed_on_not_found=True
    )
    LOG.info("Update completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Update DB System Instance")
    return instance_response.data


def get_instance_public_ip(config, compartment_ocid, instance_name):
    LOG.info("[Start] Get Public IP")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    LOG.info("Compartment: '{0}'".format(compartment_ocid))
    vnic_id = get_vnic_attachment_id(config, compartment_ocid, instance_name)
    ip_address = vcn.get_vnic(config, vnic_id).public_ip
    LOG.info("Public IP address: '{0}'".format(ip_address))
    LOG.info("[END] Get Public IP")
    return ip_address


def get_backup(config, compartment_id, backup_display_name):
    LOG.info("[START] Get DB System Backup")
    LOG.info("Backup Name: '{0}'".format(backup_display_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))    
    dbsystem_client = get_dbsystem_client(config)
    check = True
    backup = None
    backups = dbsystem_client.list_backups(compartment_id = compartment_id)
    while check:
        if not backups.has_next_page:
            check = False
        for backup_aux in backups.data:
            LOG.info(backup_aux)
            if backup_display_name == backup_aux.display_name:
                LOG.info("Found: {0}".format(backup_aux.display_name))
                LOG.info("Backup  found")
                backup = backup_aux
                check = False
        if check:
            backups = dbsystem_client.list_db_systems(compartment_id, page = backups.next_page)
    LOG.info(backup)
    LOG.info("[END] Get DB System Backup")
    return backup


def get_database(config, compartment_id, dbsystem_instance_id):
    LOG.info("[START] Get DB")
    LOG.info("Database System ID: '{0}'".format(dbsystem_instance_id))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    dbsystem_client = get_dbsystem_client(config)
    LOG.info("Get DB Home")
    db_home =  dbsystem_client.list_db_homes(compartment_id, dbsystem_instance_id).data[0]
    LOG.info(db_home)
    LOG.info("Get Database")
    db = dbsystem_client.list_databases(compartment_id, db_home.id).data[0]
    LOG.info(db)    
    LOG.info("[END] Get DB")
    return db


def create_backup(config, instance_cfg):
    LOG.info("[START] Create Backup")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))    
    dbsystem_client = get_dbsystem_client(config)
    dbsystem_instance = get_instance(config, instance_cfg["compartment"],
                                     instance_cfg["display_name"]
                                     )
    db = get_database(config, instance_cfg["compartment"], dbsystem_instance.id)
    backup_display_name = "{0}_backup".format(instance_cfg["display_name"])
    backup_details = oci.database.models.CreateBackupDetails(
        database_id = db.id,
        display_name = backup_display_name
        )
    LOG.info("Creating Backup...")
    backup = dbsystem_client.create_backup(backup_details).data
    LOG.info(backup)
    LOG.info("[END] Create Backup")
    return backup


def delete_backup(config, instance_cfg):
    LOG.info("[START] Delete Backup")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))
    backup_display_name = "{0}_backup".format(instance_cfg["display_name"])    
    dbsystem_client = get_dbsystem_client(config)
    backup = get_backup(config, instance_cfg["compartment"], backup_display_name)
    LOG.info("Deleting Backup...")
    backup_deleted = dbsystem_client.delete_backup(backup.id).data
    LOG.info(backup_deleted)
    LOG.info("[END] Delete Backup")
    return backup_deleted


def restore_backup(config, instance_cfg):
    LOG.info("[START] Restore Backup")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))    
    dbsystem_client = get_dbsystem_client(config)

    LOG.info("Get Backup Timestamp")
    backup_display_name = "{0}_backup".format(instance_cfg["display_name"])
    backup = get_backup(config, instance_cfg["compartment"], backup_display_name)
    backup_ts = datetime(backup.time_ended.year,
                         backup.time_ended.month,
                         backup.time_ended.day,
                         backup.time_ended.hour,
                         backup.time_ended.minute,
                         backup.time_ended.second,
                         0)
    backup_ts = backup_ts.strftime("%Y-%m-%dT%H:%M:%S.000Z")
    LOG.info("Time stamp: '{0}'".format(backup_ts))


    LOG.info("Get DB instance")
    dbsystem_instance = get_instance(config, instance_cfg["compartment"],
                                     instance_cfg["display_name"]
                                     )
    db = get_database(config, instance_cfg["compartment"], dbsystem_instance.id)
    
    LOG.info("Restore Backup")
    restore_details = oci.database.models.RestoreDatabaseDetails(timestamp = backup_ts)
    #restore_details = oci.database.models.RestoreDatabaseDetails(latest = True)
    LOG.info("DB ID: '{0}'".format(db.id))
    LOG.info("Restore details: '{0}'".format(restore_details))
    dbsystem_client.restore_database(db.id, restore_details)
    LOG.info("[END] Restore Backup")
    pass

def get_database_domain(config, instance_cfg):
    LOG.info("[START] Get Database Domain")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))    
    dbsystem_instance = get_instance(config, instance_cfg["compartment"],
                                     instance_cfg["display_name"]
                                     )
    db_domain_name = dbsystem_instance.domain
    LOG.info("Domain name: '{0}'".format(db_domain_name))
    LOG.info("[END] Get Database Domain")
    return db_domain_name


def get_database_unique_name(config, instance_cfg):
    LOG.info("[START] Get Database Unique Name")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))    
    dbsystem_instance = get_instance(config, instance_cfg["compartment"],
                                     instance_cfg["display_name"]
                                     )
    db = get_database(config, instance_cfg["compartment"], dbsystem_instance.id)
    db_unique_name = db.db_unique_name
    LOG.info("DB unique name: '{0}'".format(db_unique_name))
    LOG.info("[END] Get Database Unique Name")
    return db_unique_name
