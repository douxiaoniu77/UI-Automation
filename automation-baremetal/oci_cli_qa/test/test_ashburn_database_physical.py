'''
Created on Jun 11, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-06-11   umartine    Initial creation
    2018-06-12   umartine    Add test suites for X5
    2018-06-12   umartine    Functional test for DB
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
    "SE":   "STANDARD_EDITION",
    "EE":   "ENTERPRISE_EDITION",
    "EEHP": "ENTERPRISE_EDITION_HIGH_PERFORMANCE",
    "EEEP": "ENTERPRISE_EDITION_EXTREME_PERFORMANCE",
    "BYOL": "ENTERPRISE_EDITION"
    }

X7_TEST_SUITE = [
    # Provisioning Test Cases
    ("PIC_DATABASE_STANDARD_DENSE_IO_X7_Creation",                          "SE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_DENSE_IO_X7_Creation",                        "EE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO_X7_Creation",       "EEHP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO_X7_Creation",    "EEEP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_DENSE_IO_BYOL_X7_Creation",                          "BYOL", "CREATE", DBAAS_RN),
    # Delay before Functional Test
    #("FT_SPAN_DELAY",   "PRE_FT",   AUX_RN),
    # Functional Tests
    ("PIC_DATABASE_STANDARD_DENSE_IO_X7_Functional_Test",                       "SE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_DENSE_IO_X7_Functional_Test",                     "EE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO_X7_Functional_Test",    "EEHP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO_X7_Functional_Test", "EEEP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ALL_DENSE_IO_BYOL_X7_Functional_Test",                       "BYOL", "FUNCTIONAL", DBAAS_RN),
    # Life SPAN
    #("LIFE_SPAN_DELAY", "DELAY",    AUX_RN),
    # Termination Test Cases
    ("PIC_DATABASE_STANDARD_DENSE_IO_X7_Deletion",                          "SE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_DENSE_IO_X7_Deletion",                        "EE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO_X7_Deletion",       "EEHP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO_X7_Deletion",    "EEEP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_DENSE_IO_BYOL_X7_Deletion",                          "BYOL", "TERMINATE", DBAAS_RN)
]

HIGHIO_TEST_SUITE = [
    # Provisioning Test Cases
    ("PIC_DATABASE_STANDARD_HIGH_IO_Creation",                          "SE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_IO_Creation",                        "EE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO_Creation",       "EEHP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO_Creation",    "EEEP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_HIGH_IO_BYOL_Creation",                          "BYOL", "CREATE", DBAAS_RN),
    # Delay before Functional Test
    #("FT_SPAN_DELAY",   "PRE_FT",   AUX_RN),
    # Functional Tests
    ("PIC_DATABASE_STANDARD_HIGH_IO_Functional_Test",                       "SE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_IO_Functional_Test",                     "EE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO_Functional_Test",    "EEHP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO_Functional_Test", "EEEP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ALL_HIGH_IO_BYOL_Functional_Test",                       "BYOL", "FUNCTIONAL", DBAAS_RN),
    # Life SPAN
    #("LIFE_SPAN_DELAY", "DELAY",    AUX_RN),
    # Termination Test Cases
    ("PIC_DATABASE_STANDARD_HIGH_IO_Deletion",                          "SE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_IO_Deletion",                        "EE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_HIGH_IO_Deletion",       "EEHP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_HIGH_IO_Deletion",    "EEEP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_HIGH_IO_BYOL_Deletion",                          "BYOL", "TERMINATE", DBAAS_RN)
]


DENSEIO_TEST_SUITE = [
    # Provisioning Test Cases
    ("PIC_DATABASE_STANDARD_DENSE_IO_Creation",                          "SE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_DENSE_IO_Creation",                        "EE",   "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO_Creation",       "EEHP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO_Creation",    "EEEP", "CREATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_DENSE_IO_BYOL_Creation",                          "BYOL", "CREATE", DBAAS_RN),
    # Delay before Functional Test
    #("FT_SPAN_DELAY",   "PRE_FT",   AUX_RN),
    # Functional Tests
    ("PIC_DATABASE_STANDARD_DENSE_IO_Functional_Test",                       "SE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_DENSE_IO_Functional_Test",                     "EE",   "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO_Functional_Test",    "EEHP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO_Functional_Test", "EEEP", "FUNCTIONAL", DBAAS_RN),
    ("PIC_DATABASE_ALL_DENSE_IO_BYOL_Functional_Test",                       "BYOL", "FUNCTIONAL", DBAAS_RN),
    # Life SPAN
    #("LIFE_SPAN_DELAY", "DELAY",    AUX_RN),
    # Termination Test Cases
    ("PIC_DATABASE_STANDARD_DENSE_IO_Deletion",                          "SE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_DENSE_IO_Deletion",                        "EE",   "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_HIGH_PERFORMANCE_DENSE_IO_Deletion",       "EEHP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ENTERPRISE_EXTREME_PERFORMANCE_DENSE_IO_Deletion",    "EEEP", "TERMINATE", DBAAS_RN),
    ("PIC_DATABASE_ALL_DENSE_IO_BYOL_Deletion",                          "BYOL", "TERMINATE", DBAAS_RN)
]

# Select appropiate AD, Host Prefix and test suite

SHAPE_TEST_MAPPING = {
    "BM.DenseIO2.52": ("AD-1", "x7", X7_TEST_SUITE),
    "BM.HighIO1.36":  ("AD-2", "h5", HIGHIO_TEST_SUITE),
    "BM.DenseIO1.36": ("AD-3", "d5", DENSEIO_TEST_SUITE)
}

SHAPE = RESOURCES["DATABASE"]["CFG"]["shape"]
DEFAULT_AD, HOSTNAME_PREFIX, TEST_SUITE = SHAPE_TEST_MAPPING[SHAPE]

'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''

@pytest.mark.parametrize("test_name,db_version,operation,resource", TEST_SUITE)
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
    db_cfg = dict(RESOURCES[resource]["CFG"])
    db_cfg["compartment"] = CONFIG["tenancy"]
    db_cfg["display_name"] = "{0}_{1}_AUTOMATION".format(SHAPE, db_version)
    db_cfg["hostname"] = "{0}{1}".format(HOSTNAME_PREFIX, db_version.lower())
    db_cfg["availability_domain"] = DEFAULT_AD
    db_cfg["database_edition"] = EDITION_MAPPING[db_version]
    if db_version == "BYOL":
        db_cfg["license_model"] = "BRING_YOUR_OWN_LICENSE"

    #Run operation
    operation_runner = RESOURCES[resource]["OPERATIONS"][operation]
    operation_runner(CONFIG, db_cfg)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))
