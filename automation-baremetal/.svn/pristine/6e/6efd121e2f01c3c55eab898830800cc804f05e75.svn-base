'''
Created on Jun 13, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-06-13   umartine    Initial creation
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
from oci_cli_qa.test.database import DBAAS_OPS
from oci_cli_qa.test.auxiliar import AUXILIAR_OPS, create_vcn


'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''
# Load Configurations
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["AUXILIAR", "DATABASE"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
AUX_RN, DBAAS_RN = RESOURCE_LIST
# Operation types
CONSUME = ["CREATE", "UPSIZE", "DELAY"]
FUNCTIONAL = ["FUNCTIONAL"]
RELEASE = ["DOWNSIZE", "TERMINATE"]

TEST_CONFIGURATION_JSON = "test/config/test_config.json"
TEST_CONFIG = json.load(open(TEST_CONFIGURATION_JSON))
RESOURCES["AUXILIAR"]["CFG"] = TEST_CONFIG
RESOURCES["AUXILIAR"]["OPERATIONS"] = AUXILIAR_OPS

# Load Compute Configuration
DB_CONFIGURATION_JSON = "config/job/DB_System_instance/config.json"
RESOURCES["DATABASE"]["CFG"] = json.load(open(DB_CONFIGURATION_JSON))
RESOURCES["DATABASE"]["OPERATIONS"] = DBAAS_OPS


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

EDITION_MAPPING = {
    "SE":   ("STANDARD_EDITION",                        1, "VM.Standard2.2"),
    "EE":   ("ENTERPRISE_EDITION",                      1, "VM.Standard1.2"),
    "EEHP": ("ENTERPRISE_EDITION_HIGH_PERFORMANCE",     1, "VM.Standard2.2"),
    "EEEP": ("ENTERPRISE_EDITION_EXTREME_PERFORMANCE",  1, "VM.Standard1.2"),
    "BYOL": ("ENTERPRISE_EDITION_HIGH_PERFORMANCE",     1, "VM.Standard2.2"),
    "RAC":  ("ENTERPRISE_EDITION_EXTREME_PERFORMANCE",  2, "VM.Standard1.2"),
    "RACB": ("ENTERPRISE_EDITION_EXTREME_PERFORMANCE",  2, "VM.Standard2.2")
    }

DBAAS_VM_TEST_SUITE = [
    # Provisioning Test Cases
    ("PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY_VM_Creation",                       "SE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY_VM_Creation",                     "EE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY_VM_Creation",    "EEHP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY_VM_Creation", "EEEP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_ADDITIONAL_CAPACITY_BYOL_VM_Creation",                       "BYOL", "CREATE", DBAAS_RN),
    # RAC
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY_VM_RAC_Creation", "RAC",  "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_ADDITIONAL_CAPACITY_BYOL_VM_RAC_Creation",                       "RACB", "CREATE", DBAAS_RN),

    # Delay before Functional Test
    #("FT_SPAN_DELAY",   "PRE_FT",   AUX_RN),

    # Functional Tests
    ("PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY_VM_Functional_Test",                        "SE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY_VM_Functional_Test",                      "EE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY_VM_Functional_Test",     "EEHP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY_VM_Functional_Test",  "EEEP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ALL_ADDITIONAL_CAPACITY_BYOL_VM_Functional_Test",                        "BYOL", "FUNCTIONAL", DBAAS_RN),
    # RAC
    # Functional Tests
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY_VM_RAC_Functional_Test",  "RAC",  "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ALL_ADDITIONAL_CAPACITY_BYOL_VM_RAC_Functional_Test",                        "RACB", "FUNCTIONAL", DBAAS_RN),

    # Life SPAN
    #("LIFE_SPAN_DELAY", "DELAY",    AUX_RN),

    # Termination Test Cases
    ("PIC_DATABASE_STANDARD_ADDITIONAL_CAPACITY_VM_Deletion",                       "SE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_ADDITIONAL_CAPACITY_VM_Deletion",                     "EE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_ADDITIONAL_CAPACITY_VM_Deletion",    "EEHP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY_VM_Deletion", "EEEP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_ADDITIONAL_CAPACITY_BYOL_VM_Deletion",                       "BYOL", "TERMINATE", DBAAS_RN),
    # RAC
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_ADDITIONAL_CAPACITY_VM_RAC_Deletion", "RAC",  "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_ADDITIONAL_CAPACITY_BYOL_VM_RAC_Deletion",                       "RACB", "TERMINATE", DBAAS_RN)
]


# Select appropiate AD, Host Prefix and test suite

SHAPE_TEST_MAPPING = {
    "VM.Standard1.2":  ("AD-3", "vmx5"),
    "VM.Standard2.2":  ("AD-3", "vmx7")
}


'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''

@pytest.mark.parametrize("test_name,db_version,operation,resource", DBAAS_VM_TEST_SUITE)
def test_case(test_name, db_version, operation, resource):
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    if not TEST_CONFIG["CREATE"] and operation in CONSUME:
        pytest.skip("Test case skipped")
        return
    if not TEST_CONFIG["FUNCTIONAL"] and operation in FUNCTIONAL:
        pytest.skip("Test case skipped")
        return
    if not TEST_CONFIG["TERMINATE"] and operation in RELEASE:
        pytest.skip("Test case skipped")
        return

    # Pre-config test
    sw_edition, node_count, shape = EDITION_MAPPING[db_version]
    ad, hostname_prefix = SHAPE_TEST_MAPPING[shape]
    db_cfg = dict(RESOURCES[resource]["CFG"])
    db_cfg["shape"] = shape
    db_cfg["compartment"] = CONFIG["tenancy"]
    db_cfg["display_name"] = "{0}_{1}_AUTOMATION".format(shape, db_version)
    db_cfg["hostname"] = "{0}{1}".format(hostname_prefix, db_version.lower())
    db_cfg["availability_domain"] = ad
    db_cfg["database_edition"] = sw_edition
    db_cfg["node_count"] = node_count
    if db_version == "BYOL" or db_version == "RACB":
        db_cfg["license_model"] = "BRING_YOUR_OWN_LICENSE"

    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"][operation]
    operation_runner(CONFIG, db_cfg)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))
