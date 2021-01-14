'''
Created on Apr 2, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-04-02   umartine    Initial creation
    2018-04-03   umartine    Terminate LB flow
'''

import json

import oci_cli_qa.lib.operations.load_balance as load_balancer
import oci_cli_qa.lib.configuration as configuration

# Configuration files
LB_CONFIGURATION_JSON = "config/job/load_balancer/config.json"
TEST_CONFIGURATION_JSON = "config/job/load_balancer/tests.json"

# Load configuration
lb_cfg = json.load(open(LB_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Jobs
def create_lb():
    load_balancer.create_lb_instance(config, lb_cfg)

def terminate_lb():
    load_balancer.terminate_lb_instance(config, lb_cfg["display_name"])

# Run jobs
if test_cfg["create_lb"]: create_lb()
if test_cfg["terminate_lb"]: terminate_lb()
