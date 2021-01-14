'''
Created on Jan 3, 2019
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2019-01-03   umartine    Initial creation
    2019-01-04   umartine    Adding test cases 1, 2, 4 and 6
    2019-01-07   umartine    Adding test cases 3, 5 and 7
    2019-01-08   umartine    Fixing test case 4 and adding 8 and 9

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import json
import pytest

import oci_cli_qa.lib.configuration as configuration

from oci_cli_qa.lib.runner import run_command
from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.test.compute import COMPUTE_OPS
from oci_cli_qa.test.object_storage import OBJECT_STORAGE_OPS
from oci_cli_qa.test.auxiliar import AUXILIAR_OPS, create_vcn
from oci_cli_qa.lib.scenarios import TEST_TYPE_AUTHORIZATION

from oci_cli_qa.platform.compartment_prereqs import set_single_compartment_prereqs
from oci_cli_qa.platform.compartment_prereqs import set_restricted_compartment_prereqs
from oci_cli_qa.platform.compartment_prereqs import set_children_compartment_prereqs
from oci_cli_qa.platform.compartment_prereqs import set_double_compartment_prereqs
from oci_cli_qa.platform.compartment_prereqs import delete_compartment
from oci_cli_qa.platform.compartment_prereqs import get_compartment


'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''
# Configuration steps
# ------------------------------------------------------------------------------
LOG.info("===================================================================")
LOG.info("                     Starting new session")
LOG.info("===================================================================")

#run_command("mkdir test_results")
run_command("mkdir test_results".split())
CONFIG = configuration.load_configuration()
create_vcn(CONFIG)

# Load Configurations
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["AUXILIAR", "COMPUTE", "OBJECT_STORAGE"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
AUX_RN, COMPUTE_RN, OS_RN = RESOURCE_LIST
# Operation types
CONSUME = ["CREATE", "UPSIZE", "FUNCTIONAL", "DELAY"]
RELEASE = ["DOWNSIZE", "TERMINATE"]

TEST_CONFIGURATION_JSON = "test/config/test_config.json"
TEST_CONFIG = json.load(open(TEST_CONFIGURATION_JSON))
RESOURCES["AUXILIAR"]["CFG"] = TEST_CONFIG
RESOURCES["AUXILIAR"]["OPERATIONS"] = AUXILIAR_OPS

# Load Compute Configuration
INSTANCE_CONFIGURATION_JSON = "config/job/compute_instance/config.json"
RESOURCES["COMPUTE"]["CFG"] = json.load(open(INSTANCE_CONFIGURATION_JSON))
RESOURCES["COMPUTE"]["CFG"]["vcn_compartment"] = CONFIG["tenancy"]
RESOURCES["COMPUTE"]["OPERATIONS"] = COMPUTE_OPS

# Load Object Storage Configuration
BUCKET_CONFIGURATION_JSON = "config/job/os_bucket/config.json"
RESOURCES["OBJECT_STORAGE"]["CFG"] = json.load(open(BUCKET_CONFIGURATION_JSON))
RESOURCES["OBJECT_STORAGE"]["OPERATIONS"] = OBJECT_STORAGE_OPS


'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''

RESOURCE_AD_MAPPING = {
    "VM.Standard2.2":   "AD-1",
    "VM.Standard2.4":   "AD-2",
    "BM.GPU2.2":        "AD-3",
    "BM.Standard2.52":  "AD-2",
    "BM.GPU3.8":        "AD-2",
    "BM.DenseIO2.52":   "AD-1",
    "BM.HPC2.36":       "AD-1",
    "BM.Standard.E2.64":"AD-3"
}


# Compute
RESOURCE_TO_TEST = ("VM.Standard2.4", COMPUTE_RN)

# Object Storage
#RESOURCE_TO_TEST = ("Standard", OS_RN)


'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''


# TC-1 Create a resource in a compartment
def test_case_01():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 01 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")
    # Setup Compartment configuration
    compartment_id = set_single_compartment_prereqs(CONFIG, resource = resource)

    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_01".format(shape)
    instance_cfg["compartment"] = compartment_id

    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]

    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["CREATE"]
    operation_runner(CONFIG, instance_cfg)

    # Report Result
    open("test_results/{0}.suc".format(shape), 'a').close()
    LOG.info("[TEST] Finishing test 01 for '{0}:{1}'".format(resource, shape))


# TC-3 List existing resources in the compartment
def test_case_03():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 03 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")
        # Setup Compartment configuration
    compartment_id = set_single_compartment_prereqs(CONFIG, resource = resource)

    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_01".format(shape)
    instance_cfg["compartment"] = compartment_id

    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]

    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["LIST"]
    operation_runner(CONFIG, instance_cfg)

    # Report Result
    open("test_results/{0}.suc".format(shape), 'a').close()
    LOG.info("[TEST] Finishing test 03 for '{0}:{1}'".format(resource, shape))


# TC-2 Deleting a resource removes it from the compartment
def test_case_02():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 02 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")
    # Setup Compartment configuration
    compartment_id = set_single_compartment_prereqs(CONFIG, resource = resource)

    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_01".format(shape)
    instance_cfg["compartment"] = compartment_id

    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]

    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["TERMINATE"]
    operation_runner(CONFIG, instance_cfg)

    # Report Result
    open("test_results/{0}.suc".format(shape), 'a').close()
    LOG.info("[TEST] Finishing test 02 for '{0}:{1}'".format(resource, shape))


# TC-4 Create/delete/retrieve resource from the compartment that user doesn't have permission to
def test_case_04():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 04 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")
    # Setup Compartment configuration
    compartment_id, user_1_id, user_2_id = set_restricted_compartment_prereqs(CONFIG,
                                                                    resource = resource)
    
    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_04_User_1".format(shape)
    instance_cfg["compartment"] = compartment_id

    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]

    # Run operation user 1
    user_1_config = dict(CONFIG)
    user_1_config["user"] = user_1_id
    instance_cfg["admin_cfg"] = CONFIG
    operation_runner = RESOURCES[resource]["OPERATIONS"]["CREATE"]
    operation_runner(user_1_config, instance_cfg)

    # Run operation user 2
    user_2_config = dict(CONFIG)
    user_2_config["user"] = user_2_id

    # List instance
    operation_runner = RESOURCES[resource]["OPERATIONS"]["LIST"]
    operation_runner(user_2_config, instance_cfg, test_type = TEST_TYPE_AUTHORIZATION)

    # Terminate instance
    operation_runner = RESOURCES[resource]["OPERATIONS"]["TERMINATE"]
    operation_runner(user_2_config, instance_cfg, test_type = TEST_TYPE_AUTHORIZATION)

    # Create instance
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_04_User_2".format(shape)
    operation_runner = RESOURCES[resource]["OPERATIONS"]["CREATE"]
    operation_runner(user_2_config, instance_cfg, test_type = TEST_TYPE_AUTHORIZATION)

    # Report Result
    open("test_results/{0}.suc".format(shape), 'a').close()
    LOG.info("[TEST] Finishing test 04 for '{0}:{1}'".format(resource, shape))


# TC-5 Attach a resource to another resource in different compartment
def test_case_05():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 05 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")

    compartment_1_id, compartment_2_id = set_double_compartment_prereqs(CONFIG, resource = resource)

    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_05".format(shape)
    instance_cfg["compartment"] = compartment_1_id
    instance_cfg["vcn_compartment"] = compartment_2_id

    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]

    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["ATTACH"]
    operation_runner(CONFIG, instance_cfg)

    # Report Result
    open("test_results/{0}.suc".format(shape), 'a').close()
    LOG.info("[TEST] Finishing test 05 for '{0}:{1}'".format(resource, shape))


# TC-6 Create a resource in a parent/child compartment
def test_case_06():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 06 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")
    # Setup Compartment configuration
    compartment_parent_id, compartment_child_id = set_children_compartment_prereqs(CONFIG, resource = resource)

    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_06_parent".format(shape)

    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]

    #Run operation parent compartment
    instance_cfg["compartment"] = compartment_parent_id
    operation_runner = RESOURCES[resource]["OPERATIONS"]["CREATE"]
    operation_runner(CONFIG, instance_cfg)

    #Run operation child compartment
    instance_cfg["compartment"] = compartment_child_id
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_06_child".format(shape)
    operation_runner(CONFIG, instance_cfg)

    # Report Result
    open("test_results/{0}.suc".format(shape), 'a').close()
    LOG.info("[TEST] Finishing test 06 for '{0}:{1}'".format(resource, shape))


# TC-7 Delete a compartment that has resource in it
def test_case_07():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 07 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")

    # Delete Non-Empty Compartment
    delete_compartment(CONFIG,
                       compartment_name = "restricted_compartment",
                       resource = resource,
                       empty = False)
    LOG.info("[TEST] Finishing test 07 for '{0}:{1}'".format(resource, shape))


# TC-8 Delete a compartment that is empty but used to have resource
def test_case_08():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 08 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")
    # Delete Empty Compartment
    delete_compartment(CONFIG,
                       compartment_name = "new_compartment",
                       resource = resource,
                       empty = True)
    LOG.info("[TEST] Finishing test 08 for '{0}:{1}'".format(resource, shape))


# TC-9 Delete a compartment that spans multiple regions
def test_case_09():
    shape, resource = RESOURCE_TO_TEST
    LOG.info("----------------------------------------------------------------")
    LOG.info("[TEST] Starting test 09 for '{0}:{1}'".format(resource, shape))
    LOG.info("----------------------------------------------------------------")

    compartment_name = "restricted_compartment"
    compartment_id = get_compartment(CONFIG,
                                     compartment_name = compartment_name,
                                     resource = resource)

    # Pre-config test
    phoenix_config = dict(CONFIG)
    phoenix_config["region"] = "us-phoenix-1"

    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_COMPARTMENT_tc_05".format(shape)
    instance_cfg["compartment"] = compartment_id

    # Create resources in Phoenix Region 
    create_vcn(phoenix_config)
    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]
    # Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["CREATE"]
    operation_runner(phoenix_config, instance_cfg)

    # Delete resources in Ashburn Region
    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["TERMINATE"]
    operation_runner(CONFIG, instance_cfg)

    # Delete non empty compartment
    delete_compartment(CONFIG,
                       compartment_name = compartment_name,
                       resource = resource,
                       empty = False)

    # Delete resources in Phoenix Region
    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"]["TERMINATE"]
    operation_runner(phoenix_config, instance_cfg)

    # Delete empty compartment
    delete_compartment(CONFIG,
                       compartment_name = compartment_name,
                       resource = resource,
                       empty = True)
    LOG.info("[TEST] Finishing test 09 for '{0}:{1}'".format(resource, shape))
