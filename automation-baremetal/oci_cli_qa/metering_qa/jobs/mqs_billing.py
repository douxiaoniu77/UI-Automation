'''
Created on Jul 26, 2018
@author: xueniu

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-07-18   xueniu      Initial creation
    2018-07-26   umartine    Modification to allow expected entries


'''

import json
from metering_qa.test.mqs_test import MQS_OPS 

# Configuration files
MQS_CONFIG_JASON = "config/job/mqs_billing/config.json"

# Load configuration
billing_details = json.load(open(MQS_CONFIG_JASON))

MQS_OPS["BILLING"](billing_details,
                   billing_details["expected_entries"],
                   billing_details["expected_value"]
                   )


