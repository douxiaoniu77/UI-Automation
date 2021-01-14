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
import json
import pytest

import oci_cli_qa.lib.configuration as configuration

from oci_cli_qa.lib.runner import run_command
from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.test.tag import TAG_OPS

'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''

# Load Configurations
# ------------------------------------------------------------------------------
RESOURCE_LIST = ["TAG_NAMESPACE_TEST","TAG_TEST"]
RESOURCES = {resource: {} for resource in RESOURCE_LIST}
TAGN_RN, TAG_RN= RESOURCE_LIST

# Operation types
CONSUME = ["CREATE_TAG_NAMESPACE","CREATE_TAG"]


TAG_CONFIGURATION_JSON = "config/job/tag/config.json"
RESOURCES["TAG_NAMESPACE"]["CFG"] = json.load(open(TAG_CONFIGURATION_JSON))
RESOURCES["TAG_NAMESPACE"]["OPERATIONS"] = TAG_OPS
RESOURCES["TAG"]["CFG"] = json.load(open(TAG_CONFIGURATION_JSON))
RESOURCES["TAG"]["OPERATIONS"] = TAG_OPS

# Configuration steps
# ------------------------------------------------------------------------------
#run_command("mkdir test_results")
run_command("mkdir test_results".split())
CONFIG = configuration.load_configuration()

FULL_TEST_SUITE = [
    ("Tag_NameSpace", "Test_Tag_NameSpace_01", "CREATE_TAG_NAMESPACE", TAGN_RN),
    ("Tag", "Test_Tag_01", "CREATE_TAG", TAG_RN),
    ("Tag", "Test_Tag_02", "CREATE_TAG", TAG_RN),
    ("Tag", "Test_Tag_03", "CREATE_TAG", TAG_RN)
    ]


@pytest.mark.parametrize("resource_name,display_name,operation,resource",FULL_TEST_SUITE)
def test_case(resource_name,display_name,operation,resource):
    LOG.info("[TEST] Starting test '{0}'".format(resource_name))
    # Pre-config test
    instance_cfg = dict(RESOURCES[resource]["CFG"])
    if resource_name == "TAG_NAMESPACE" :
        instance_cfg["tag_namespace_name"] = display_name
    else:
        instance_cfg["tag_name"] = display_name    
        
    operation_runner = RESOURCES[resource]["OPERATIONS"][operation]
    operation_runner(CONFIG, instance_cfg)
    open("test_results/{0}.suc".format(resource_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(resource_name))







