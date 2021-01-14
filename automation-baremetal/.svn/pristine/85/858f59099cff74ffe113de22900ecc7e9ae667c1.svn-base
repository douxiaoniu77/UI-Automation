'''
created on Dec 05,2018
@author : Qin Xiong

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-12-05   qinxiong    Initial creation

'''
import oci
import oci_cli_qa.lib.operations.identity as identity
from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.jobs.approved_sender_creation import instance_cfg
from scripts.regsetup import description

def get_identity_client(config):
    LOG.info("Get identity Client")
    return oci.identity.IdentityClient(config)

def get_tag_namespace_instance(config,compartment_id,tag_namespace_name):
    LOG.info("[START] Get Tag NameSpace")
    LOG.info("Tag NameSpace is: '{0}'".format(tag_namespace_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))     
    indentity_client = get_identity_client(config)
    
    instances = indentity_client.list_tag_namespaces(compartment_id)
    
    for instance in instances:
        if instance.name ==  tag_namespace_name:
            LOG.info("Instance found")
            
    LOG.info(instance)
    LOG.info("[END] Get Tag NameSpace")     
    return instance

def get_tag_instance(config,compartment_id,tag_namespace_name,tag_name):
    LOG.info("[START] Get tag ")
    LOG.info("Tag is: '{0}'".format(tag_name))
    LOG.info("Tag NameSpace is: '{0}'".format(tag_namespace_name))     
    indentity_client = get_identity_client(config)
    
    tag_namespace = get_instance(config,compartment_id,tag_namespace_name)
    instances = indentity_client.list_tags(tag_namespace.id)
    for instance in instances:
        if instance.name ==  tag_name:
            LOG.info("Tag found")
            
    LOG.info(instance)
    LOG.info("[END] Get Tag ")  
    
    return instance

def create_tag_namespace(config,instance_cfg):
    LOG.info("[START] Create Tag NameSpace")
    LOG.info(instance_cfg) 
    indentity_client = get_identity_client(config)
    
    tag_namespace_details = oci.identity.models.CreateTagNamespaceDetails(
        compartment_id = instance_cfg["compartment_id"],
        description = instance_cfg["tag_namespace_description"],
        name = instance_cfg["tag_namespace_name"]
        )
    instance_response = indentity_client.create_tag_namespace(tag_namespace_details)
    LOG.info("[END] Create Tag NameSpace")
    
    return instance_response.data


def create_tag(config,instance_cfg):
    LOG.info("[START] Create tag in '{0}' ".format(instance_cfg["tag_namespace_name"]))
    LOG.info(instance_cfg) 
    indentity_client=get_identity_client(config)
    
    tag_details = oci.identity.models.CreateTagDetails(
        description = instance_cfg["tag_description"],
        name = instance_cfg["tag_name"]        
        )
    tag_namespace = get_instance(config,
                                    instance_cfg["compartment_id"],
                                    instance_cfg["tag_namespace_name"]
                                    )
    if tag_namespace.id == Null:
       LOG.info("Can't find Specific Tag NameSpace") 
       return
    instance_response = indentity_client.create_tag(tag_namespace.id, create_tag_details)
    LOG.info("[END] Create tag in '{0}' ".format(instance_cfg["tag_namespace_name"]))
    
    return instance_response.data

