'''
Created on Sep 19, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-09-19   umartine    Initial creation
    2018-09-26   umartine    Support for new testsuite payload
    2018-11-27   umartine    Support for retrieve previous results

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import json
import pytest

from oci_early_test.test.early_test_operations import OCI_EARLY_OPS
from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.runner import run_command

'''
-------------------------------------------------------------------------------
                            Load Configurations
-------------------------------------------------------------------------------
'''
# Globals
DATE_STRING = '%Y-%m-%dT%H:%M:%S.%fZ'

# LOAD Configurations
TEST_CONFIGURATION_JSON = "test/test_suites/test_early_test_sample.json"
TEST_CONFIG = json.load(open(TEST_CONFIGURATION_JSON))

# Generating config data from files
# ------------------------------------------------------------------------------
run_command(["mkdir", "test_results"])

'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''
TEST_NAMES = [test["name"] for test in TEST_CONFIG["tests"]]
TEST_SUITE = [(i, TEST_NAMES[i]) for i in xrange(len(TEST_NAMES))]

'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''

@pytest.mark.parametrize("test_index,test_name", TEST_SUITE)
def test_step(test_index, test_name):
    LOG.info("============================================================")
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    LOG.info("============================================================")
    test_details = TEST_CONFIG["tests"][test_index]
    test_type = test_details["type"]
    test_results = OCI_EARLY_OPS[test_type](TEST_CONFIG["tests"],
                                            test_details["details"],
                                            TEST_CONFIG["endpoints"],
                                            TEST_CONFIG["accounts"])
    test_details["result"] = test_results
    TEST_CONFIG["tests"][test_index] = test_details
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))

