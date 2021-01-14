'''
Created on Jun 5, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-06-05   umartine    Initial creation
    2018-06-08   umartine    Minor bug fixes on resource list and AD mapping
    2018-06-26   umartine    Add Fast Connect
    2018-11-28   pcamaril    Add Compute shapes
    2019-01-01   umartine    Correction on compartment id key

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
from oci_cli_qa.test.load_balancer import LOAD_BALANCER_OPS
from oci_cli_qa.test.fast_connect import FAST_CONNECT_OPS
from oci_cli_qa.test.auxiliar import AUXILIAR_OPS, create_vcn


'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''
# Load Configurations
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["AUXILIAR", "COMPUTE", "LOAD_BALANCER", "FAST_CONNECT"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
AUX_RN, COMPUTE_RN, LB_RN, FC_RN = RESOURCE_LIST
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

# Load LB Configuration
FC_CONFIGURATION_JSON = "config/job/fast_connect/config.json"
RESOURCES["FAST_CONNECT"]["CFG"] = json.load(open(FC_CONFIGURATION_JSON))
RESOURCES["FAST_CONNECT"]["OPERATIONS"] = FAST_CONNECT_OPS

# Configuration steps
# ------------------------------------------------------------------------------
#run_command("mkdir test_results")
run_command("mkdir test_results".split())
CONFIG = configuration.load_configuration()
create_vcn(CONFIG)


'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''

RESOURCE_AD_MAPPING = {
    "BM.Standard1.36":  "AD-3",
    "BM.HighIO1.36":    "AD-1",
    "BM.DenseIO1.36":   "AD-1",
    "BM.GPU2.2":        "AD-3",
    "BM.Standard2.52":  "AD-2",
    "BM.GPU3.8":        "AD-2",
    "BM.DenseIO2.52":   "AD-1",
    "BM.HPC2.36":       "AD-1",
    "BM.Standard.E2.64":"AD-3"
}


FULL_TEST_SUITE = [
    # Provisioning Test Cases
     ("PIC_COMPUTE_X5_STANDARD_Creation",        "BM.Standard1.36", "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X5_NVME_12_8_Creation",       "BM.HighIO1.36",   "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X5_NVME_28_8_Creation",       "BM.DenseIO1.36",  "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_STANDARD_Creation",        "BM.Standard2.52", "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_NVME_28_8_Creation",       "BM.DenseIO2.52",  "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_GPU_STANDARD_Creation",    "BM.GPU2.2",       "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_GPU_STANDARD_V2_Creation",    "BM.GPU3.8",       "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_HPC_X7",                      "BM.HPC2.36",      "CREATE",     COMPUTE_RN),
     ("PIC_COMPUTE_STANDARD_E2",                 "BM.Standard.E2.64", "CREATE",   COMPUTE_RN),
     ("PIC_LB_LARGE_Creation",                   "8000Mbps",        "CREATE",     LB_RN),
     ("PIC_COMPUTE_FASTCONNECT_Creation",        "Fast.Connect",    "CREATE",     FC_RN),
    # Delay before Functional Test
     ("FT_SPAN_DELAY",                              "None",           "PRE_FT",     AUX_RN),
     # Functional test
     ("PIC_COMPUTE_X5_STANDARD_Functional_Test",        "BM.Standard1.36", "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_X5_NVME_12_8_Functional_Test",       "BM.HighIO1.36",   "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_X5_NVME_28_8_Functional_Test",       "BM.DenseIO1.36",  "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_STANDARD_Functional_Test",        "BM.Standard2.52", "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_NVME_28_8_Functional_Test",       "BM.DenseIO2.52",  "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_GPU_STANDARD_Functional_Test",    "BM.GPU2.2",       "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_GPU_STANDARD_V2_Functional_Test",    "BM.GPU3.8",       "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_HPC_X7",                             "BM.HPC2.36",      "FUNCTIONAL",     COMPUTE_RN),
     ("PIC_COMPUTE_STANDARD_E2",                        "BM.Standard.E2.64", "FUNCTIONAL",   COMPUTE_RN),
    # Life SPAN
     ("LIFE_SPAN_DELAY",                            "None",           "DELAY",      AUX_RN),
     #Termination Test
     ("PIC_COMPUTE_X5_STANDARD_Deletion",        "BM.Standard1.36", "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X5_NVME_12_8_Deletion",       "BM.HighIO1.36",   "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X5_NVME_28_8_Deletion",       "BM.DenseIO1.36",  "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_STANDARD_Deletion",        "BM.Standard2.52", "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_NVME_28_8_Deletion",       "BM.DenseIO2.52",  "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_X7_GPU_STANDARD_Deletion",    "BM.GPU2.2",       "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_GPU_STANDARD_V2_Deletion",    "BM.GPU3.8",       "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_HPC_X7",                      "BM.HPC2.36",      "TERMINATE",     COMPUTE_RN),
     ("PIC_COMPUTE_STANDARD_E2",                 "BM.Standard.E2.64", "TERMINATE",   COMPUTE_RN),
     ("PIC_LB_LARGE_Deletion",                   "8000Mbps",        "TERMINATE",     LB_RN),
     ("PIC_COMPUTE_FASTCONNECT_Deletion",        "Fast.Connect",    "TERMINATE",     FC_RN)
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
    instance_cfg["compartment"] = CONFIG["tenancy"]
    if "availability_domain" in instance_cfg:
        instance_cfg["availability_domain"] = RESOURCE_AD_MAPPING[shape]
    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"][operation]
    operation_runner(CONFIG, instance_cfg)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))
