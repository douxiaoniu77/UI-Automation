'''
Created on Jul 2018
@author: xueniu

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-07-18   xueniu      Initial creation
    2018-08-07   umartine    Getting the usage in the Hourly manner

-------------------------------------------------------------------------------
                                 Load Modules
-------------------------------------------------------------------------------
'''

import requests
from oci_cli_qa.lib.logger import LOG

USER = "OCLOUD9_TAS_SERVICE_MANAGER_APPID"
PASSWORD = "cloud9_TAS"
MQS_BILLING_URL = "https://mqs.dc1.c9qa132.oraclecorp.com:443/metering/api/v1/usagecost/{0}?serviceName={1}&resourceName={2}&serviceEntitlementId={3}&startTime={4}&endTime={5}&timeZone=UTC&usageType=HOURLY"

def get_mqs_billings(cacct_id, service_name, resource_name, entitlement_id, time_range):
    LOG.info("[Start] Get MQS Billing Summary")
    LOG.info("Cloud Account ID: '{0}'".format(cacct_id))
    LOG.info("Service Name: '{0}'".format(service_name))
    LOG.info("Resource Name: '{0}'".format(resource_name))
    LOG.info("Entitlement ID: '{0}'".format(entitlement_id))   
    LOG.info("Time Range: '{0}'".format(time_range))
    # Get start and end times
    start_time, end_time = time_range

    # Build and send the request
    mqs_url_billing=MQS_BILLING_URL.format(cacct_id, service_name, resource_name, entitlement_id, start_time, end_time)
    # CREATE AND SEND THE API REST CALL
    
    # START
    mqs_billing_request = requests.get(mqs_url_billing, auth=(USER, PASSWORD))
    billing_entries = mqs_billing_request.json()
    # END
    LOG.info("MQS Response: ")
    LOG.info(billing_entries)
    # Build an usage billing_entries
    LOG.info("Building Billing Summary")
    billing_summary = []
    for billing_entry in billing_entries["items"]:
        sumamry_billing_entry = {}
        # Turn into date objects
        start_time = str(billing_entry["startTimeUtc"])
        end_time = str(billing_entry["endTimeUtc"])
        sumamry_billing_entry["quantity"] = float(billing_entry["costs"][0]["computedAmount"])
        sumamry_billing_entry["start_time"] = start_time
        sumamry_billing_entry["end_time"] = end_time
        billing_summary.append(sumamry_billing_entry)
    LOG.info("Billing summary:")
    LOG.info(billing_summary)
    LOG.info("[END] Get MQS Billing Summary")
    return billing_summary


def get_mqs_billing():
    pass


