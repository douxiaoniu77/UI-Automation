'''
Created on Apr 27, 2018
@author: Qin

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-04-27   qxionng     Initial creation
    2018-06-11   umartine    Documentation and code cleaning
    2018-06-12   umartine    Refactor and adding functional test
'''

import json

import oci_cli_qa.lib.configuration as configuration

from oci_cli_qa.test.database import DBAAS_OPS

# Configuration files
DB_CONFIGURATION_JSON = "config/job/DB_System_instance/config.json"
TEST_CONFIGURATION_JSON = "config/job/DB_System_instance/tests.json"

# Load configuration
db_cfg = json.load(open(DB_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Run jobs
if test_cfg["create_instance"]: DBAAS_OPS["CREATE"](config, db_cfg)
if test_cfg["functional_test"]: DBAAS_OPS["FUNCTIONAL"](config, db_cfg)
if test_cfg["update_instance"]: DBAAS_OPS["UPDATE"](config, db_cfg)
if test_cfg["terminate_instance"]: DBAAS_OPS["TERMINATE"](config, db_cfg)
