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
import oci_cli_qa.lib.operations.tag as tag
from oci_cli_qa.jobs.approved_sender_creation import instance_cfg

def create_tag_namespace_instace(config,instance_cfg):
    tag.create_tag_namespace(config, instance_cfg)
    
    
def create_tag_instance(config,instance_cfg):
    tag.create_tag(config, instance_cfg)
    
    
TAG_OPS = {"CREATE_TAG_NAMESPACE" : create_tag_namespace_instace,
           "CREATE_TAG": create_tag_instance
           }    