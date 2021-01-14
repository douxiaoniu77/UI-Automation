'''
created on Mar 21,2018
@author : Qin Xiong

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-21   qinxiong    Initial creation
    2018-12-04   umartine    Support for compartment selection

'''
import oci
import oci_cli_qa.lib.operations.identity as identity
from oci_cli_qa.lib.logger import LOG


def get_blockstorage_client(config):
    LOG.info("Get Block Volume Client")
    return oci.core.blockstorage_client.BlockstorageClient(config)

def get_instance(config, compartment_id, instance_name, status = None):
    LOG.info("[START] Get Block Volume Instance")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    blockstorage_client = get_blockstorage_client(config)
    instances = blockstorage_client.list_volumes(compartment_id).data
    instance = None
    for instance_aux in instances:
        if instance_aux.display_name == instance_name:
            if status == None or instance_aux.lifecycle_state == status:
                LOG.info("Instance found")
                instance = instance_aux
    LOG.info(instance)
    LOG.info("[END] Get Block Volume Instance")
    return instance
    
    
def create_instance(config,instance_cfg):
    LOG.info("[START] Create Block Volume Instance")
    LOG.info(instance_cfg)
    blockstorage_client = get_blockstorage_client(config)
    LOG.info("Create instance") 
    ad = identity.get_availability_domain(config, instance_cfg["availability_domain"])
    instance_details = oci.core.models.LaunchInstanceDetails(
        display_name = instance_cfg["display_name"],
        compartment_id = instance_cfg["compartment"],
        #availability_domain = instance_cfg["availability_domain"]
        availability_domain = ad.name
    )
    LOG.info("Request Sent")
    instance_response = blockstorage_client.create_volume(instance_details)
    LOG.info(instance_response.data)
    
    LOG.info("Wait the operation to complete")
    instance_response = oci.wait_until(
        blockstorage_client,
        blockstorage_client.get_volume(instance_response.data.id),
        "lifecycle_state",
        "AVAILABLE"
    )

    LOG.info("Operation completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Create Block Volume Instance")
    return instance_response.data

def delete_instance(config,bv_name):
    LOG.info("[START] Terminate Block Volume Instance")
    LOG.info(bv_name)
    blockstorage_client = get_blockstorage_client(config)

    LOG.info("Get Instance details")
    blockstorage_instance = get_instance(config,
                                    config["tenancy"],
                                    bv_name,
                                    status = "AVAILABLE"
                                    )

    LOG.info("Terminate instance")
    
    blockstorage_client.delete_volume(blockstorage_instance.id)

    instance_response = oci.wait_until(
        blockstorage_client,
        blockstorage_client.get_volume(blockstorage_instance.id),
        "lifecycle_state",
        "TERMINATED",
        succeed_on_not_found=True
    )

    LOG.info("Operation completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Terminate Block volume Instance")
    return instance_response.data