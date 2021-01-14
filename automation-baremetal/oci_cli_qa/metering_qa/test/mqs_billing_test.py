'''
Created on Jul 2018
@author: xueniu


'''

import metering_qa.lib.billing as mqs_billing
from oci_cli_qa.lib.logger import LOG

def test_billing(billing_details, expected_quantity):
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
    total_quantity = 0
    for billing_entry in billing_summary:
        total_quantity += billing_entry["quantity"]
    expected_quantity = str(expected_quantity)
    total_quantity = str(total_quantity)
    LOG.info("Expected Total Quantity: '{0}'".format(expected_quantity))
    LOG.info("Total Quantity: '{0}'".format(total_quantity))
    assert expected_quantity == total_quantity
    

MQS_BILLING_OPS = {"BILLING": test_billing
           }
