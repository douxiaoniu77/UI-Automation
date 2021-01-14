'''
Created on Nov 21, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-11-21   umartine    Initial creation

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import json, sys
import pytest

sys.path.append("/systemtests/automation-baremetal/oci_cli_qa")

import oci_cli_qa.lib.configuration as configuration

from oci_cli_qa.lib.runner import run_command
from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.test.compute import COMPUTE_OPS
from oci_cli_qa.test.auxiliar import AUXILIAR_OPS, create_vcn


'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''
# Configure General
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["AUXILIAR", "COMPUTE"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
AUX_RN, COMPUTE_RN = RESOURCE_LIST
# Operation types
CONSUME = ["CREATE", "UPSIZE", "FUNCTIONAL", "DELAY", "CREATE_FAKE"]
RELEASE = ["DOWNSIZE", "TERMINATE"]

TEST_CONFIGURATION_JSON = "test/config/test_config.json"
RESOURCES["AUXILIAR"]["CFG"] = json.load(open(TEST_CONFIGURATION_JSON))
RESOURCES["AUXILIAR"]["OPERATIONS"] = AUXILIAR_OPS

# Load Compute Configuration
INSTANCE_CONFIGURATION_JSON = "config/job/compute_instance/config.json"
RESOURCES["COMPUTE"]["CFG"] = json.load(open(INSTANCE_CONFIGURATION_JSON))
RESOURCES["COMPUTE"]["OPERATIONS"] = COMPUTE_OPS


# Configuration steps
# ------------------------------------------------------------------------------
run_command("mkdir test_results".split())
CONFIG = configuration.load_configuration()
create_vcn(CONFIG)

'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''

FULL_TEST_SUITE = [
    ("PIC_COMPUTE_X7_VM_STANDARD_Creation", "VM.Standard2.2", "CREATE_FAKE", COMPUTE_RN),
    ("LIFE_SPAN_DELAY",                     "None",           "DELAY",       AUX_RN),
    ("PIC_COMPUTE_X7_VM_STANDARD_Deletion", "VM.Standard2.2", "TERMINATE",   COMPUTE_RN),
]

'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''

@pytest.mark.parametrize("test_name,shape,operation,resource", FULL_TEST_SUITE)
def test_compute(test_name, shape, operation, resource):
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_AUTOMATION".format(shape)
    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"][operation]
    operation_runner(CONFIG, instance_cfg)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))
