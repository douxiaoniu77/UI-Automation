'''
Created on July 25, 2018
@author: Qin

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
def create_instance():
    instance = compute.create_instance(config, instance_cfg)


def terminate_instance():
    compute.terminate_instante(config, instance_cfg)
    return

def connect_instance():
    compute.connect_windows_instance(config, instance_cfg)
    return
# Run jobs
if test_cfg["create_instance"]: create_instance()
if test_cfg["connect_instance"]: connect_instance()
if test_cfg["terminate_instance"]: terminate_instance()
