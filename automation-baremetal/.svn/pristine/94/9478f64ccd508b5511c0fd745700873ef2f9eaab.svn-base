'''
Created on Jun 28, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-27   umartine    Initial creation

'''

import json
from metering_qa.test.mqs_test import MQS_OPS

# Configuration files
MQS_CONFIG_JASON = "config/job/mqs_usage/config.json"

# Load configuration
usage_details = json.load(open(MQS_CONFIG_JASON))

MQS_OPS["USAGE"](usage_details, usage_details["expected_entries"],
                 usage_details["expected_value"])


