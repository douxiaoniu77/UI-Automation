'''
Created on June 13, 2018
@author: Qin

'''

import json
import time
import os

import oci_cli_qa.lib.operations.exadata as exadata
import oci_cli_qa.lib.configuration as configuration
import oci_cli_qa.lib.operations.vcn as vcn

from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.runner import run_command

# Configuration files
INSTANCE_CONFIGURATION_JSON = "config/job/Exadata_instance/config.json"
TEST_CONFIGURATION_JSON = "config/job/Exadata_instance/tests.json"

# Load configuration
instance_cfg = json.load(open(INSTANCE_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Jobs
def create_instance():
    exadata.create_instance(config, instance_cfg)
    return

def terminate_instance():
    exadata.terminate_instance(config, instance_cfg)
    return

def update_instance():
    exadata.update_instance(config, instance_cfg)
    return

# Run jobs
if test_cfg["create_instance"]: create_instance()
if test_cfg["terminate_instance"]: terminate_instance()
if test_cfg["update_instance"]: update_instance()