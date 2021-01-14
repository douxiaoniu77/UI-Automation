'''
Created on May 29, 2018

@author: umartine


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-29   umartine    Initial creation
    2018-06-26   umartine    Use Operations map
    2018-06-26   umartine    Use Operations map
    2018-12-13   umartine    Adding Support to DNS flood
    2018-12-13   umartine    Send queries operation

'''

import json, time

import oci_cli_qa.lib.configuration as configuration
from oci_cli_qa.test.dns import DNS_OPS


# Configuration files
DNS_CONFIGURATION_JSON = "config/job/dns/config.json"
TEST_CONFIGURATION_JSON = "config/job/dns/tests.json"


# Load configuration
dns_config = json.load(open(DNS_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

dns_config["compartment"] = config["tenancy"]

# Run jobs
if test_cfg["create_dns"]: DNS_OPS["CREATE"](config, dns_config)
if test_cfg["send_queries"]: DNS_OPS["FUNCTIONAL"](config, dns_config)
if test_cfg["create_dns"] and test_cfg["terminate_dns"]: time.sleep(10 * 60)
if test_cfg["terminate_dns"]: DNS_OPS["TERMINATE"](config, dns_config)
