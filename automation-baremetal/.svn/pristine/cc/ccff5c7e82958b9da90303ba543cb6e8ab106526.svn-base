'''
Created on Nov 21, 2018
@author: pcamaril

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-11-21   pcamaril    Initial creation
    2018-12-04   pcamaril    Add new compute test cases and a new method to 
                             write the export file as well.
    2018-12-06   pcamaril    Change directory's name where the results are written.
    2018-12-07   pcamaril    Fix to select tenancy ocid as default compartment
    2018-12-11   pcamaril    Add more services to the pipeline test cases
    2018-12-13   pcamaril    Fix export writing process.
    2018-12-14   pcamaril    Change the to 'resourceName' the field to be exported

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import json, sys, os
import pytest

sys.path.append("/systemtests/automation-baremetal/oci_cli_qa")

import oci_cli_qa.lib.configuration as configuration

from oci_cli_qa.lib.runner import run_command
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
# Configure General
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["AUXILIAR", "COMPUTE", "LOAD_BALANCER", "OBJECT_STORAGE",
                 "BLOCK_STORAGE", "FILE_SYSTEM", "DNS", "EMAIL"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
AUX_RN, COMPUTE_RN , LB_RN, OS_RN, BS_RN, FS_RN, DNS_RN, EMAIL_RN = RESOURCE_LIST
# Operation types
CONSUME = ["CREATE", "UPSIZE", "FUNCTIONAL", "DELAY"]
RELEASE = ["DOWNSIZE", "TERMINATE"]

TEST_CONFIGURATION_JSON = "test/config/test_config.json"
RESOURCES["AUXILIAR"]["CFG"] = json.load(open(TEST_CONFIGURATION_JSON))
RESOURCES["AUXILIAR"]["OPERATIONS"] = AUXILIAR_OPS

# Load Pipeline Configuration
PIPELINE_CONFIG_JSON = "test/config/test_config_pipeline.json"
PIPELINE_CONFIG = json.load(open(PIPELINE_CONFIG_JSON))

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
run_command("mkdir test_results".split())
CONFIG = configuration.load_configuration()
COMPARTMENT_ID = CONFIG["tenancy"]
create_vcn(CONFIG, COMPARTMENT_ID)

export_tas_resource_names = None

EXPORT_RESOURCES = {COMPUTE_RN: ["PIC_COMPUTE_X7_VM_STANDARD",
                                 "PIC_COMPUTE_X7_VM_DENSEIO"],
                    LB_RN: ["PIC_LB_SMALL", "PIC_LB_MEDIUM"],
                    OS_RN: ["PIC_OBJECT_STORAGE", "PIC_ARCHIVE_STORAGE"],
                    BS_RN: ["PIC_BLOCK_STORAGE_STANDARD"],
                    FS_RN: ["PIC_FILE_STORAGE"],
                    DNS_RN: ["PIC_DYN_DNS_QUERIES"],
                    EMAIL_RN: ["PIC_DYN_EMAIL_DELIVERY"]}


def setup_module():
    '''
    Writes resources tested in the current session
    '''
    LOG.info("[SETUP] Starting to export output properties")

    with open("test_results/export.txt", "w") as export_file:
        export_file.write("resourceName=")
        export_file.write(",".join(EXPORT_RESOURCES[PIPELINE_CONFIG["SERVICE"]]))
        export_file.write("\n")
        export_file.close()
    LOG.info("[SETUP] Export file created successfully")


'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''

FULL_TEST_SUITE = [
    ("PIC_COMPUTE_X7_VM_STANDARD_Creation",        "VM.Standard2.2", "CREATE",     COMPUTE_RN),
    ("PIC_COMPUTE_X7_VM_DENSEIO_Creation",         "VM.DenseIO2.8",  "CREATE",     COMPUTE_RN),
    ("PIC_LB_SMALL_Creation",                      "100Mbps",        "CREATE",     LB_RN),
    ("PIC_LB_MEDIUM_Creation",                     "400Mbps",        "CREATE",     LB_RN),
    ("PIC_OBJECT_STORAGE_Creation",                "Standard",       "CREATE",     OS_RN),
    ("PIC_OBJECT_STORAGE_Upsize",                  "Standard",       "UPSIZE",     OS_RN),
    ("PIC_ARCHIVE_STORAGE_Creation",               "Archive",        "CREATE",     OS_RN),
    ("PIC_ARCHIVE_STORAGE_Upsize",                 "Archive",        "UPSIZE",     OS_RN),
    ("PIC_BLOCK_STORAGE_STANDARD_Creation",        "BlockVolume",    "CREATE",     BS_RN),
    ("PIC_FILE_STORAGE_Creation",                  "FileSystem",     "CREATE",     FS_RN),
    ("PIC_DYN_DNS_QUERIES_Creation",               "DNS",            "CREATE",     DNS_RN),
    ("PIC_DYN_EMAIL_DELIVERY_Creation",            "EMAIL",          "CREATE",     EMAIL_RN),
    # Delay before Functional Test
    ("FT_SPAN_DELAY",                              "None",           "PRE_FT",     AUX_RN),
    # Functional test
    ("PIC_COMPUTE_X7_VM_STANDARD_Functional_Test", "VM.Standard2.2", "FUNCTIONAL", COMPUTE_RN),
    ("PIC_COMPUTE_X7_VM_DENSEIO_Functional_Test",  "VM.DenseIO2.8",  "FUNCTIONAL", COMPUTE_RN),
    ("PIC_FILE_STORAGE_Functional_Test",           "FileSystem",     "FUNCTIONAL", FS_RN),
    ("PIC_FILE_STORAGE_Upsize",                    "FileSystem",     "UPSIZE",     FS_RN),
    ("PIC_DYN_EMAIL_DELIVERY_Functional_Test",     "EMAIL",          "FUNCTIONAL", EMAIL_RN),
    # Life SPAN
    ("LIFE_SPAN_DELAY",                            "None",           "DELAY",      AUX_RN),
    #Termination Test
    ("PIC_COMPUTE_X7_VM_STANDARD_Deletion",        "VM.Standard2.2", "TERMINATE",  COMPUTE_RN),
    ("PIC_COMPUTE_X7_VM_DENSEIO_Deletion",         "VM.DenseIO2.8",  "TERMINATE",  COMPUTE_RN),
    ("PIC_LB_SMALL_Deletion",                      "100Mbps",        "TERMINATE",  LB_RN),
    ("PIC_LB_MEDIUM_Deletion",                     "400Mbps",        "TERMINATE",  LB_RN),
    ("PIC_OBJECT_STORAGE_Downsize",                "Standard",       "DOWNSIZE",   OS_RN),
    ("PIC_OBJECT_STORAGE_Deletion",                "Standard",       "TERMINATE",  OS_RN),
    ("PIC_ARCHIVE_STORAGE_Downsize",               "Archive",        "DOWNSIZE",   OS_RN),
    ("PIC_ARCHIVE_STORAGE_Deletion",               "Archive",        "TERMINATE",  OS_RN),
    ("PIC_BLOCK_STORAGE_STANDARD_Deletion",        "BlockVolume",    "TERMINATE",  BS_RN),
    ("PIC_DYN_DNS_QUERIES_Deletion",               "DNS",            "TERMINATE",  DNS_RN),
    ("PIC_DYN_EMAIL_DELIVERY_Deletion",            "EMAIL",          "TERMINATE",  EMAIL_RN),
    ("PIC_FILE_STORAGE_Downsize",                  "FileSystem",     "DOWNSIZE",   FS_RN),
    ("PIC_FILE_STORAGE_Deletion",                  "FileSystem",     "TERMINATE",  FS_RN),
]

'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''
@pytest.mark.parametrize("test_name,shape,operation,service", FULL_TEST_SUITE)
def test_pipeline(test_name, shape, operation, service):
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    if PIPELINE_CONFIG["SERVICE"] != service and (operation != "DELAY" or operation != "PRE_FT"):
        LOG.info("Service {0} is skipped".format(service))
        pytest.skip("Test case skipped")
        return
    if PIPELINE_CONFIG["TESTSUITE"] != "CONSUME" and operation in CONSUME:
        LOG.info("Operation {0} is not included in this test suite".format(operation))
        pytest.skip("Test case skipped")
        return
    if PIPELINE_CONFIG["TESTSUITE"] != "RELEASE" and operation in RELEASE:
        LOG.info("Operation {0} is not included in this test suite".format(operation))
        pytest.skip("Test case skipped")
        return
    # Pre-config test
    instance_cfg = dict(RESOURCES[service]["CFG"])
    instance_cfg["compartment"] = COMPARTMENT_ID
    instance_cfg["shape"] = shape
    instance_cfg["display_name"] = "{0}_AUTOMATION".format(shape)
    #Run operation
    operation_runner = RESOURCES[service]["OPERATIONS"][operation]
    operation_runner(CONFIG, instance_cfg)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))
