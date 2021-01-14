
'''
Created on May 14, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-05-14   umartine    Initial creation
    2018-05-14   umartine    Base test suite for Ashburn
    2018-05-15   umartine    Refactor for single entry point
    2018-05-15   umartine    Allow isolated resource actions
    2018-05-16   umartine    Adding SUC and DIF files after test completed
    2018-05-17   umartine    Refactor to improve waiting times and move common
                             code to test modules
    2018-05-18   umartine    Move results to a different folder
    2018-05-18   umartine    Add file system test cases
    2018-06-26   umartine    Add DNS Query and Email
    2018-08-22   umartine    Add Email functional test
    2018-08-23   umartine    Fixing name of Email configuration file

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import json
import pytest

import oci_cli_qa.lib.configuration as configuration

from oci_cli_qa.lib.runner import    run_command
from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.test.compute import COMPUTE_OPS
from oci_cli_qa.test.load_balancer import LOAD_BALANCER_OPS
from oci_cli_qa.test.object_storage import OBJECT_STORAGE_OPS
from oci_cli_qa.test.block_storage import BLOCK_STORAGE_OPS
from oci_cli_qa.test.file_system import FILE_SYSTEM_OPS
from oci_cli_qa.test.dns import DNS_OPS
from oci_cli_qa.test.approve_sender import EMAIL_OPS
from oci_cli_qa.test.auxiliar import AUXILIAR_OPS, create_vcn


'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''
# Load Configurations
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["AUXILIAR", "COMPUTE", "LOAD_BALANCER", "OBJECT_STORAGE",
                 "BLOCK_STORAGE", "FILE_SYSTEM", "DNS", "EMAIL"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
AUX_RN, COMPUTE_RN, LB_RN, OS_RN, BS_RN, FS_RN, DNS_RN, EMAIL_RN = RESOURCE_LIST
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
RESOURCES["COMPUTE"]["OPERATIONS"] = COMPUTE_OPS

# Load LB Configuration
LB_CONFIGURATION_JSON = "config/job/load_balancer/config.json"
RESOURCES["LOAD_BALANCER"]["CFG"] = json.load(open(LB_CONFIGURATION_JSON))
RESOURCES["LOAD_BALANCER"]["OPERATIONS"] = LOAD_BALANCER_OPS

# Load Object Storage Configuration
BUCKET_CONFIGURATION_JSON = "config/job/os_bucket/config.json"
RESOURCES["OBJECT_STORAGE"]["CFG"] = json.load(open(BUCKET_CONFIGURATION_JSON))
RESOURCES["OBJECT_STORAGE"]["OPERATIONS"] = OBJECT_STORAGE_OPS

# Load Block Storage Configuration
BS_CONFIGURATION_JSON = "config/job/block_volume_instance/config.json"
RESOURCES["BLOCK_STORAGE"]["CFG"] = json.load(open(BS_CONFIGURATION_JSON))
RESOURCES["BLOCK_STORAGE"]["OPERATIONS"] = BLOCK_STORAGE_OPS

# Load File System Configuration
FILE_SYTEM_CONFIGURATION_JSON = "config/job/file_system/config.json"
RESOURCES["FILE_SYSTEM"]["CFG"] = json.load(open(FILE_SYTEM_CONFIGURATION_JSON))

RESOURCES["FILE_SYSTEM"]["OPERATIONS"] = FILE_SYSTEM_OPS

# Load DNS Configuration
DNS_CONFIGURATION_JSON = "config/job/dns/config.json"
RESOURCES["DNS"]["CFG"] = json.load(open(DNS_CONFIGURATION_JSON))
RESOURCES["DNS"]["OPERATIONS"] = DNS_OPS

# Load EMAIL Configuration
EMAIL_CONFIGURATION_JSON = "config/job/email_instance/config.json"
RESOURCES["EMAIL"]["CFG"] = json.load(open(EMAIL_CONFIGURATION_JSON))
RESOURCES["EMAIL"]["OPERATIONS"] = EMAIL_OPS

# Configuration steps
# ------------------------------------------------------------------------------
##run_command("mkdir test_results")
run_command("mkdir test_results".split())
CONFIG = configuration.load_configuration()
create_vcn(CONFIG)


'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''

FULL_TEST_SUITE = [
    ("PIC_DYN_DNS_QUERIES_Creation",               "DNS",            "CREATE",     DNS_RN),
    ("PIC_DYN_DNS_QUERIES_Functional_Test",        "DNS",            "FUNCTIONAL",     DNS_RN),
    ("PIC_DYN_DNS_QUERIES_Deletion",               "DNS",            "TERMINATE",     DNS_RN)
]


'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''

@pytest.mark.parametrize("test_name,shape,operation,resource", FULL_TEST_SUITE)
def test_case(test_name, shape, operation, resource):
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    if not TEST_CONFIG["CREATE"] and operation in CONSUME:
        pytest.skip("Test case skipped")
        return
    if not TEST_CONFIG["TERMINATE"] and operation in RELEASE:
        pytest.skip("Test case skipped")
        return
    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_AUTOMATION".format(shape)
    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"][operation]
    operation_runner(CONFIG, instance_cfg)
    #open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))