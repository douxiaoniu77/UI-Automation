'''
Created on Sep 14, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-09-14   umartine    Initial creation
    2018-09-26   umartine    Support for new testsuite payload
    2018-11-26   umartine    Support for retrieve previous results

'''

import time

import oci_early_test.lib.rest_wrapper as rest_wrapper
import oci_early_test.lib.supporting_operations as supporting_operations
from oci_cli_qa.lib.logger import LOG


def test_call(tests, test_details, endpoints, accounts):
    test_details = supporting_operations.replace_values(tests, test_details)
    results = rest_wrapper.api_request(test_details, endpoints, accounts)
    LOG.info("Expected Status: '{0}'".format(test_details["expected_response"]))
    LOG.info("Call Status: '{0}'".format(results["status"]))
    assert results["status"] == int(test_details["expected_response"])
    return results["body"]


def test_wait(tests, test_details, _, __):
    test_details = supporting_operations.replace_values(tests, test_details)
    LOG.info("Wait '{0}' minutes".format(test_details["wait_time"]))
    time.sleep(int(test_details["wait_time"]) * 60)
    return None


OCI_EARLY_OPS = {"REST_CALL": test_call,
                 "WAIT":      test_wait}
