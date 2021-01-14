'''
Created on Jun 28, 2018
@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-06-28   umartine    Initial creation
    2018-06-29   umartine    Get details from aggregator maps and conf files
    2018-07-26   umartine    Renaming vars for aggregator Maps
    2018-08-07   umartine    Adding Billing test cases
    2018-08-15   umartine    Round expected hourly billing

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import json
import pytest

from datetime import datetime

from metering_qa.test.mqs_test import MQS_OPS
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
MQS_CONFIG_JSON = "config/job/mqs_usage/config.json"
MQS_TEST_JSON = "config/job/mqs_usage/tests.json"
AGGREGATOR_MAP_JSON = "config/mqs_aggregator_map_oci.json"
BILLING_MAP_JSON = "config/mqs_billing_map_oci.json"
TEST_CONFIGURATION_JSON = "test/config/test_config.json"

USAGE_DETAILS = json.load(open(MQS_CONFIG_JSON))
EXPECTED_USAGE_MAP = json.load(open(MQS_TEST_JSON))
AGGREGATOR_MAP = json.load(open(AGGREGATOR_MAP_JSON))
EXPECTED_CHARGES_MAP = json.load(open(MQS_TEST_JSON))
BILLING_MAP = json.load(open(BILLING_MAP_JSON))
TEST_CONFIG = json.load(open(TEST_CONFIGURATION_JSON))

# Generating config data from files
# ------------------------------------------------------------------------------
START_TIME = datetime.strptime(USAGE_DETAILS["start_time"], DATE_STRING)
END_TIME = datetime.strptime(USAGE_DETAILS["end_time"], DATE_STRING)
TIME_DELTA = END_TIME - START_TIME
EXPECTED_ENTRIES = (TIME_DELTA.days * 24) + (TIME_DELTA.seconds  / 3600)

run_command(["mkdir", "test_results"])

'''
-------------------------------------------------------------------------------
                            Test suite
-------------------------------------------------------------------------------
'''

USAGE_TEST_SUITE = [("{0}_Metering".format(sku), sku) for sku in EXPECTED_USAGE_MAP.keys()]
BILLING_TEST_SUITE = [("{0}_Billing".format(sku), sku) for sku in EXPECTED_USAGE_MAP.keys()]

'''
-------------------------------------------------------------------------------
                        Test runners
-------------------------------------------------------------------------------
'''

@pytest.mark.parametrize("test_name,sku_name", USAGE_TEST_SUITE)
def test_mqs_usage(test_name, sku_name):
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    if not TEST_CONFIG["USAGE"]:
        pytest.skip("Test case skipped")
        return
    # Pre-config test
    usage_details = dict(USAGE_DETAILS)
    usage_details["resource_name"] = sku_name
    if "PIC_DATABASE" in sku_name:
        usage_details["service_name"] = "DATABASEBAREMETAL"
    #Run operation
    expected_usage = EXPECTED_USAGE_MAP[sku_name] * AGGREGATOR_MAP[sku_name]
    MQS_OPS["USAGE"](usage_details,
                     (EXPECTED_ENTRIES + 1),
                     (EXPECTED_ENTRIES + 1) * expected_usage)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))


@pytest.mark.parametrize("test_name,sku_name", BILLING_TEST_SUITE)
def test_mqs_billing(test_name, sku_name):
    LOG.info("[TEST] Starting test '{0}'".format(test_name))
    if not TEST_CONFIG["BILLING"]:
        pytest.skip("Test case skipped")
        return
    # Pre-config test
    usage_details = dict(USAGE_DETAILS)
    usage_details["resource_name"] = sku_name
    if "PIC_DATABASE" in sku_name:
        usage_details["service_name"] = "DATABASEBAREMETAL"
    #Run operation
    expected_usage_charges = EXPECTED_CHARGES_MAP[sku_name] * BILLING_MAP[sku_name]
    expected_usage_charges = round(expected_usage_charges, 2)
    MQS_OPS["BILLING"](usage_details,
                     EXPECTED_ENTRIES,
                     EXPECTED_ENTRIES * expected_usage_charges)
    open("test_results/{0}.suc".format(test_name), 'a').close()
    LOG.info("[TEST] Finishing test '{0}'".format(test_name))
