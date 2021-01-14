'''
Created on Apr 18, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-04-18   umartine    Job to create instance connection

'''

import json

import oci_cli_qa.lib.operations.compute as compute
import oci_cli_qa.lib.configuration as configuration

# Configuration files
INSTANCE_CONFIGURATION_JSON = "config/job/compute_instance/config.json"

# Load configuration
instance_cfg = json.load(open(INSTANCE_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Jobs
def create_connection():
    compute.create_instance_conection(config, instance_cfg)

# Run jobs
create_connection()
