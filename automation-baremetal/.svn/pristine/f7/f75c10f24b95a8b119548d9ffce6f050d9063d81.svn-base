'''
Created on Mar 30, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-30   umartine    Initial creation

'''

import json
import time
import os

import oci_cli_qa.lib.operations.compute as compute
import oci_cli_qa.lib.configuration as configuration
import oci_cli_qa.lib.operations.vcn as vcn

from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.runner import run_command

# Configuration files
INSTANCE_CONFIGURATION_JSON = "config/job/compute_instance/config.json"
TEST_CONFIGURATION_JSON = "config/job/compute_instance/tests.json"

# Load configuration
instance_cfg = json.load(open(INSTANCE_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Jobs

def list_instance():
    compute.list_instance(config, instance_cfg)
    return