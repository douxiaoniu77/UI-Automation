'''
Created on June 11, 2018
@author: Qin

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-11   qxionng     Initial creation
'''

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
    LOG.info("[Start] Launch Exadata Instance")
    LOG.info(instance_cfg)    
    dbsystem_client=get_dbsystem_client(config)
    
    LOG.info("Get subnet details")
    vcn_instance = vcn.get_vcn(config,
                               config["tenancy"],
                               instance_cfg["vcn"])

    subnet = vcn.get_subnet(config,
                            config["tenancy"],
                            vcn_instance.id,
                            subnet_name_pattern = "",
                            ad = instance_cfg["availability_domain"])
    #get backup subnet
    subnets = vcn.list_subnets(config, config["tenancy"], vcn_instance.id)
    LOG.info("Get backup subnet")
    for subnet_aux in subnets:
        back_subnet=None
        if (subnet_aux.id <> subnet.id) and (instance_cfg["availability_domain"] in subnet_aux.availability_domain):
            back_subnet = subnet_aux
            LOG.info(back_subnet) 
            break
       
            
    ad = identity.get_availability_domain(config, instance_cfg["availability_domain"])
    
    LOG.info("Set SSH key in metadata")
    ssh_key_file = "{0}.pub".format(instance_cfg["ssh_key"])
    ssh_key = open(ssh_key_file, "r").read()
    LOG.info(ssh_key)
    ssh_public_keys = [ssh_key]
    LOG.info(ssh_public_keys)
    
    database_details=oci.database.models.CreateDatabaseDetails(
        admin_password = instance_cfg["admin_password"],
        db_name = instance_cfg["db_name"]
        )        
    db_home=oci.database.models.CreateDbHomeDetails(
        database = database_details,
        db_version = instance_cfg["db_version"]        
        )
    LOG.info("Launch Exadata")
    instance_details=oci.database.models.LaunchDbSystemDetails(        
        db_home = db_home, 
        display_name = instance_cfg["display_name"],
        availability_domain = ad.name,
        compartment_id = config["tenancy"],
        shape = instance_cfg["shape"],
        database_edition = instance_cfg["database_edition"],
        cpu_core_count = instance_cfg["cpu_core_count"],
        subnet_id = subnet.id,
        backup_subnet_id = back_subnet.id,
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
    LOG.info("[END] Create Exadata Instance")
    return instance_response.data
    
def terminate_instance(config,instance_cfg):
    LOG.info("[Start] Terminate Exadata Instance")
    LOG.info(instance_cfg)    
    dbsystem_client=get_dbsystem_client(config) 
    
    LOG.info("Get Instance details")
    dbsystem_instance = get_instance(config,
                                    config["tenancy"],
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
    LOG.info("[END] Terminate Exadata Instance")
    return instance_response.data


#update db_system
def update_instance(config,instance_cfg):
    LOG.info("[START] update Exadata Instance")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))    
    dbsystem_client=get_dbsystem_client(config)
    
    dbsystem_instance = get_instance(config,
                                    config["tenancy"],
                                    instance_cfg["display_name"],
                                    status = 'AVAILABLE'
                                    )
    update_db_system_details=oci.database.models.UpdateDbSystemDetails(
        cpu_core_count = instance_cfg["new_cpu_core_count"]
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
    LOG.info("[END] Update Exadata Instance")
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
