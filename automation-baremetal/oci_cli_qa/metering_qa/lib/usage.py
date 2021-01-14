'''
Created on Jun 27, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-27   umartine    Initial creation
    2018-09-12   umartine    Set usageType as Hourly

'''

import requests
from oci_cli_qa.lib.logger import LOG

USER = "OCLOUD9_TAS_SERVICE_MANAGER_APPID"
PASSWORD = "cloud9_TAS"
MQS_USAGE_URL = "https://mqs.dc1.c9qa132.oraclecorp.com:443/metering/api/v1/usage/{0}?serviceName={1}&startTime={2}&endTime={3}&timeZone=UTC&resourceName={4}&serviceEntitlementId={5}&usageType=HOURLY"

def get_mqs_usage(cacct_id, entitlement_id, service_name, resource_name, time_range):
    LOG.info("[Start] Get MQS Usage Summary")
    LOG.info("Cloud Account ID: '{0}'".format(cacct_id))
    LOG.info("Entitlement ID: '{0}'".format(entitlement_id))
    LOG.info("Service Name: '{0}'".format(service_name))
    LOG.info("Resource Name: '{0}'".format(resource_name))
    LOG.info("Time Range: '{0}'".format(time_range))
    # Get start and end times
    start_time, end_time = time_range

    # Build and send the request
    mqs_url_usage= MQS_USAGE_URL.format(cacct_id, service_name, start_time, end_time, resource_name, entitlement_id)
    # CREATE AND SEND THE API REST CALL
    # START
    mqs_usage_request = requests.get(mqs_url_usage, auth=(USER, PASSWORD))
    usage_entries = mqs_usage_request.json()
    # END
    LOG.info("MQS Response: ")
    LOG.info(usage_entries)
    # Build an usage summary
    LOG.info("Building Usage Summary")
    usage_summary = []
    for usage_entry in usage_entries["items"]:
        sumamry_usage_entry = {}
        # Turn into date objects
        start_time = str(usage_entry["startTimeUtc"])
        end_time = str(usage_entry["endTimeUtc"])
        sumamry_usage_entry["quantity"] = float(usage_entry["usage"][0]["quantity"])
        sumamry_usage_entry["start_time"] = start_time
        sumamry_usage_entry["end_time"] = end_time
        usage_summary.append(sumamry_usage_entry)
    LOG.info("Usage summary:")
    LOG.info(usage_summary)
    LOG.info("[END] Get MQS Usage Summary")
    return usage_summary


def get_mqs_billing():
    pass

