'''
Created on Mar 22, 2018
@author: Qin Xiong


'''

import json

import oci_cli_qa.lib.operations.blockvolume as blockvolume
import oci_cli_qa.lib.configuration as configuration
from oci_cli_qa.lib.logger import LOG
# Configuration files
INSTANCE_CONFIGURATION_JSON = "config/job/block_volume_instance/config.json"
TEST_CONFIGURATION_JSON = "config/job/block_volume_instance/tests.json"

# Load configuration
instance_cfg = json.load(open(INSTANCE_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Jobs
def create_instance():
    blockvolume.create_instance(config, instance_cfg)
    return

def terminate_instance():
    blockvolume.delete_instance(config, instance_cfg["display_name"])
    return

# Run jobs
if test_cfg["create_instance"]: create_instance()
if test_cfg["terminate_instance"]: terminate_instance()
