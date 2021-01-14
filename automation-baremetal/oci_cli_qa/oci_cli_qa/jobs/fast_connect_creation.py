'''
Created on May 15, 2018
@author: xueniu

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-26   xueniu     Initial creation
'''

import json

import oci_cli_qa.lib.operations.fast_connect as fast_connection
import oci_cli_qa.lib.configuration as configuration

# Configuration files
DRG_CONFIGURATION_JSON = "config/job/fast_connect/config.json"
TEST_CONFIGURATION_JSON = "config/job/fast_connect/tests.json"

# Load configuration
fc_cfg = json.load(open(DRG_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Jobs
def create_fast_connect():
    fast_connection.create_fast_connect(config, fc_cfg)

def terminate_fast_connect():
    fast_connection.terminate_fast_connect(config, fc_cfg["display_name"])

# Run jobs
if test_cfg["create_fast_connect"]: create_fast_connect()
if test_cfg["terminate_fast_connect"]: terminate_fast_connect()
