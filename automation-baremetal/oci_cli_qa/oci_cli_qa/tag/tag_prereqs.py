'''
Created on Jan 10, 2019

@author: Qin
'''
import time

import oci_cli_qa.lib.operations.identity as identity
from oci_cli_qa.lib.logger import LOG

API_KEY_PUB_PATH = "config/oci_api_key_public.pem"
API_KEY_PUB = ""
with open(API_KEY_PUB_PATH, 'r') as fd:
    API_KEY_PUB = fd.read()

DELETE_WAIT_TIME = 10

# TC1-TC16
def set_prereqs_for_all(config,resource = "generic"):
    LOG.info("[START] Set prereqs for all testcase")
    LOG.info("The resource is '{0}' ".format(resource))
        
    #define users,group and policies
    user_name_1 = "user_for_tag_1"
    user_1 = identity.get_user(config, user_name_1, config["tenancy"])
    if user_1 == None:
        user_1 = identity.create_user(config, config["tenancy"], user_name_1, "user description")
        identity.upload_api_key(config, user_name_1, API_KEY_PUB)
        identity.add_user(config, "Administrators", user_name_1)
    user_1_id = user_1.id
    LOG.info("User 1 ID: '{0}'".format(user_1_id))

    user_name_2 = "user_for_tag_2"
    user_2 = identity.get_user(config, user_name_2, config["tenancy"])
    if user_2 == None:
        user_2 = identity.create_user(config, config["tenancy"], user_name_2, "user description")
        identity.upload_api_key(config, user_name_2, API_KEY_PUB)
    user_2_id = user_2.id
    LOG.info("User 2 ID: '{0}'".format(user_2_id))
    
    group_name_1 = "group_for_tag_1"
    group_1 = identity.get_group(config, group_name_1, config["tenancy"])
    if group_1 == None:
        identity.create_group(config, group_name_1, "group 1 description")
        identity.add_user(config, group_name_1, user_name_1)
        
    group_name_2 = "group_for_tag_2"
    group_2 = identity.get_group(config, group_name_2, config["tenancy"])
    if group_2 == None:
        identity.create_group(config, group_name_2, "group 1 description")
        identity.add_user(config, group_name_2, user_name_2)    
        
    policy_name_1 = "policy_for_tag_1"
    statements = ["Allow group {0} to use tag-namespaces in tenancy".format(group_name_1),
                  "Allow group {0} to manage instance-family in tenancy".format(group_name_1),
                  "Allow group {0} to manage cluster-family in tenancy".format(group_name_1),
                  "Allow group {0} to manage object-family in tenancy".format(group_name_1),
                  "Allow group {0} to manage database-family in tenancy".format(group_name_1),
                  "Allow group {0} to manage dns in tenancy".format(group_name_1),
                  "Allow group {0} to manage file-family in tenancy".format(group_name_1),
                  "Allow group {0} to manage virtual-network-family in tenancy".format(group_name_1),
                  "Allow group {0} to manage volume-family in tenancy".format(group_name_1)
                  ]
    policy_instance_1 = identity.get_policy(config, policy_name_1)
    if policy_instance_1 == None:
        identity.create_policy(config, config["tenancy"], policy_name_1, statements, "policy description")
    
    policy_name_2 = "policy_for_tag_2"
    statements = ["Allow group {0} to read tag-namespaces in tenancy".format(group_name_2),
                  "Allow group {0} to manage instance-family in tenancy".format(group_name_2),
                  "Allow group {0} to manage cluster-family in tenancy".format(group_name_2),
                  "Allow group {0} to manage object-family in tenancy".format(group_name_2),
                  "Allow group {0} to manage database-family in tenancy".format(group_name_2),
                  "Allow group {0} to manage dns in tenancy".format(group_name_2),
                  "Allow group {0} to manage file-family in tenancy".format(group_name_2),
                  "Allow group {0} to manage virtual-network-family in tenancy".format(group_name_2),
                  "Allow group {0} to manage volume-family in tenancy".format(group_name_2)
                  ]
    policy_instance_2 = identity.get_policy(config, policy_name_2)
    if policy_instance_2 == None:
        identity.create_policy(config, config["tenancy"], policy_name_2, statements, "policy description")
    
    #define tag namesapce and tag
    tag_namespace = "TNS"
    tag_ns = identity.get_tag_namespace_instance(config, config["tenancy"], tag_namespace)
    if tag_ns == None:
        tag_ns = identity.create_tag_namespace(config, config["tenancy"], tag_namespace, "tag namespaces description")
    tag_namespace_id = tag_ns.id    
    tag_name_1 = "tag_1"
    tag_1 = identity.get_tag_instance(config, config["tenancy"], tag_namespace, tag_name_1)
    if tag_1 == None:
        tag_1 = identity.create_tag(config, config["tenancy"], tag_namespace, tag_name_1, "tag description")
    tag_1_id = tag_1.id    
    tag_name_2 = "tag_1"
    tag_2 = identity.get_tag_instance(config, config["tenancy"], tag_namespace, tag_name_2)
    if tag_2 == None:
        tag_2 = identity.create_tag(config, config["tenancy"], tag_namespace, tag_name_2, "tag description")
    tag_2_id = tag_2.id      
     
    return user_1_id,user_2_id,tag_namespace_id,tag_1_id,tag_2_id
    
    