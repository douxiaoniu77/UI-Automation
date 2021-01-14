'''
Created on May 29, 2018
@author: Qin


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-29   qxionng     Initial Creation
    2018-06-26   umartine    Removing unused packages and using the OPS map
    2018-12-13   umartine    Add compartment

'''

import json

import oci_cli_qa.lib.configuration as configuration
from oci_cli_qa.test.approve_sender import EMAIL_OPS

# Configuration files
INSTANCE_CONFIGURATION_JSON = "config/job/email_instance/config.json"
TEST_CONFIGURATION_JSON = "config/job/email_instance/tests.json"

# Load configuration
instance_cfg = json.load(open(INSTANCE_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

instance_cfg["compartment_id"] = config["tenancy"]

if test_cfg["create_instance"]:     EMAIL_OPS["CREATE"](config,instance_cfg)
if test_cfg["send_emails"]:         EMAIL_OPS["FUNCTIONAL"](config,instance_cfg)
if test_cfg["terminate_instance"]:  EMAIL_OPS["TERMINATE"](config,instance_cfg)
