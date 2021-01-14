'''
Created on Jan 3, 2019

@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2019-01-03   umartine    Initial Creation
    2019-01-04   umartine    Improve logging and return desired values
    2019-01-07   umartine    Add delete-compartment flows
    2019-01-08   umartine    Add get compartment function

'''

import time

import oci_cli_qa.lib.operations.identity as identity
from oci_cli_qa.lib.logger import LOG

API_KEY_PUB_PATH = "config/oci_api_key_public.pem"
API_KEY_PUB = ""
with open(API_KEY_PUB_PATH, 'r') as fd:
    API_KEY_PUB = fd.read()

DELETE_WAIT_TIME = 10

'''
-------------------------------------------------------------------------------
                            Set Configurations
-------------------------------------------------------------------------------
'''

# TC-1, TC-2, TC-3, TC-7, TC-8, TC-9
def set_single_compartment_prereqs(config, resource = "generic"):
    LOG.info("[START] Create single compartment")
    compartment_name = "new_compartment_{0}".format(resource)
    compartment_desc = "compartment description for resource '{0}'".format(resource)
    compartment = identity.get_compartment(config, compartment_name)
    if compartment == None:
        compartment = identity.create_compartment(config, compartment_name, compartment_desc, config["tenancy"])
    LOG.info("Compartment ID: '{0}'".format(compartment.id))
    LOG.info("[END] Create single compartment")
    return compartment.id


# TC-4
def set_restricted_compartment_prereqs(config, resource = "generic"):
    LOG.info("[START] Create restricted compartment")
    compartment_name = "restricted_compartment_{0}".format(resource)
    compartment_desc = "compartment description for resource '{0}'".format(resource)
    compartment = identity.get_compartment(config, compartment_name) 
    if compartment == None:
        compartment = identity.create_compartment(config, compartment_name, compartment_desc, config["tenancy"])
    compartment_id = compartment.id
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
        
    user_name_1 = "user_1"
    user_1 = identity.get_user(config, user_name_1, config["tenancy"])
    if user_1 == None:
        user_1 = identity.create_user(config, config["tenancy"], user_name_1, "user description")
        identity.upload_api_key(config, user_name_1, API_KEY_PUB)
        identity.add_user(config, "Administrators", user_name_1)
    user_1_id = user_1.id
    LOG.info("User 1 ID: '{0}'".format(user_1_id))

    user_name_2 = "user_2"
    user_2 = identity.get_user(config, user_name_2, config["tenancy"])
    if user_2 == None:
        user_2 = identity.create_user(config, config["tenancy"], user_name_2, "user description")
        identity.upload_api_key(config, user_name_2, API_KEY_PUB)
    user_2_id = user_2.id
    LOG.info("User 2 ID: '{0}'".format(user_2_id))

    group_name = "group_1"
    group = identity.get_group(config, group_name, config["tenancy"])
    if group == None:
        identity.create_group(config, group_name, "group 1 description")
        identity.add_user(config, group_name, user_name_1)

    policy_name = "policy_1"
    statements = ["Allow group {0} to manage all-resources in compartment {1}".format(group_name, compartment_name)]
    policy_instance = identity.get_policy(config, policy_name)
    if policy_instance == None:
        identity.create_policy(config, config["tenancy"], policy_name, statements, "policy description")
    LOG.info("[END] Create restricted compartment")

    return compartment_id, user_1_id, user_2_id


# TC-5
def set_double_compartment_prereqs(config, resource = "generic"):
    LOG.info("[START] Create double compartment")

    compartment_name_1 = "new_compartment_1_{0}".format(resource)
    compartment_desc = "compartment description for resource '{0}'".format(resource)
    compartment_1 = identity.get_compartment(config, compartment_name_1)
    if compartment_1 == None:
        compartment_1 = identity.create_compartment(config, compartment_name_1, compartment_desc, config["tenancy"])
    LOG.info("Compartment 1 ID: '{0}'".format(compartment_1.id))

    compartment_name_2 = "new_compartment_2_{0}".format(resource)
    compartment_2 = identity.get_compartment(config, compartment_name_2)
    if compartment_2 == None:
        compartment_2 = identity.create_compartment(config, compartment_name_2, compartment_desc, config["tenancy"])
    LOG.info("Compartment 2 ID: '{0}'".format(compartment_2.id))

    LOG.info("[END] Create double compartment")
    return compartment_1.id, compartment_2.id


# TC-6
def set_children_compartment_prereqs(config, resource = "generic"):
    LOG.info("[START] Create children compartment")

    compartment_name_parent = "new_compartment_parent_{0}".format(resource)
    compartment_desc = "compartment description for resource '{0}'".format(resource)
    compartment_parent = identity.get_compartment(config, compartment_name_parent) 
    if compartment_parent == None:
        compartment_parent = identity.create_compartment(config, compartment_name_parent, compartment_desc, config["tenancy"])
    LOG.info("Parent Compartment ID: '{0}'".format(compartment_parent.id))

    compartment_name_child = "new_compartment_child_{0}".format(resource)
    compartment_child = identity.get_compartment(config, compartment_name_child, compartment_id = compartment_parent.id) 
    if compartment_child == None:
        compartment_child = identity.create_compartment(config, compartment_name_child, compartment_desc, compartment_parent.id)
    LOG.info("Child Compartment ID: '{0}'".format(compartment_child.id))

    LOG.info("[END] Create children compartment")
    return compartment_parent.id, compartment_child.id


# TC-7, TC-8, TC-9
def delete_compartment(config, compartment_name = "new_compartment", resource = "generic", empty = True):
    LOG.info("[START] Delete compartment")
    compartment_name = "{0}_{1}".format(compartment_name, resource)
    LOG.info("Compartment name '{0}'".format(compartment_name))
    LOG.info("Is empty compartment: '{0}'".format(empty))
    identity.delete_compartment(config, compartment_name)
    compartment = identity.get_compartment(config, compartment_name)
    i = 0
    while i < DELETE_WAIT_TIME:
        if compartment == None:
            LOG.info("Compartment deleted")
            assert empty == True
            break
        elif compartment.lifecycle_state == "ACTIVE":
            LOG.info("Compartment not deleted")
            assert empty == False
            break
        LOG.info("Deleting in progress")
        time.sleep(60)
        compartment = identity.get_compartment(config, compartment_name)
        i += 1
    if i == DELETE_WAIT_TIME:
        LOG.info("Compartment deleting process not completed after {0} minutes".format(DELETE_WAIT_TIME))
        LOG.info("Failing the test")
        assert False
    LOG.info("[END] Delete compartment")


def get_compartment(config, compartment_name = "new_compartment", resource = "generic"):
    LOG.info("[START] Get compartment")
    compartment_name = "{0}_{1}".format(compartment_name, resource)
    compartment = identity.get_compartment(config, compartment_name)
    return compartment.id
    LOG.info("[END] Get compartment")
