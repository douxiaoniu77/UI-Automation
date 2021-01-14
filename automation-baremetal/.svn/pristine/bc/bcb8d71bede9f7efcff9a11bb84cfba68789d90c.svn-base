'''
Created on Jun 28, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-27   umartine    Initial creation
    2018-06-26   umartine    Adding Billing test

'''

import metering_qa.lib.usage as mqs_usage
import metering_qa.lib.billing as mqs_billing
from oci_cli_qa.lib.logger import LOG

def test_usage(usage_details, expected_entries_count, expected_quantity):
    cacct_id = usage_details["cacct_id"]
    entitlement_id = usage_details["entitlement_id"]
    service_name = usage_details["service_name"]
    resource_name = usage_details["resource_name"]
    start_time = usage_details["start_time"]
    end_time = usage_details["end_time"]
    time_range = start_time, end_time
    LOG.info("Getting Usage from MQS")
    usage_summary = mqs_usage.get_mqs_usage(cacct_id, entitlement_id,
                                            service_name, resource_name,
                                            time_range)
    entries_count = len(usage_summary)
    LOG.info("Expected Entry Count: '{0}'".format(expected_entries_count))
    LOG.info("Real Entry Count: '{0}'".format(entries_count))
    assert expected_entries_count == entries_count
    total_quantity = 0
    for usage_entry in usage_summary:
        total_quantity += usage_entry["quantity"]
    expected_quantity = str(expected_quantity)
    total_quantity = str(total_quantity)
    LOG.info("Expected Total Quantity: '{0}'".format(expected_quantity))
    LOG.info("Total Quantity: '{0}'".format(total_quantity))
    assert expected_quantity == total_quantity


def test_billing(billing_details, expected_entries_count, expected_quantity):
    cacct_id = billing_details["cacct_id"]
    service_name = billing_details["service_name"]
    resource_name = billing_details["resource_name"]
    entitlement_id = billing_details["entitlement_id"]
    start_time = billing_details["start_time"]
    end_time = billing_details["end_time"]
    time_range = start_time, end_time
    LOG.info("Getting Billing from MQS")
    
    billing_summary = mqs_billing.get_mqs_billings(cacct_id, service_name, resource_name,
                                                  entitlement_id, time_range)
    entries_count = len(billing_summary)
    LOG.info("Expected Entry Count: '{0}'".format(expected_entries_count))
    LOG.info("Real Entry Count: '{0}'".format(entries_count))
    assert expected_entries_count == entries_count
    total_quantity = 0
    for billing_entry in billing_summary:
        total_quantity += billing_entry["quantity"]
    expected_quantity = str(expected_quantity)
    total_quantity = str(total_quantity)
    LOG.info("Expected Total Quantity: '{0}'".format(expected_quantity))
    LOG.info("Total Quantity: '{0}'".format(total_quantity))
    assert expected_quantity == total_quantity
    

MQS_OPS = {"USAGE":     test_usage,
           "BILLING":   test_billing
           }
