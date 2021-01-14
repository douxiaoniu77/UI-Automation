'''
Created on Apr 27, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-21   umartine     Initial creation
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
if test_cfg["create_backup"]: DBAAS_OPS["CREATE_BACKUP"](config, db_cfg)
if test_cfg["list_records"]: DBAAS_OPS["LIST_RECORDS"](config, db_cfg)
if test_cfg["add_records"]: DBAAS_OPS["ADD_RECORDS"](config, db_cfg)
if test_cfg["terminate_backup"]: DBAAS_OPS["TERMINATE_BACKUP"](config, db_cfg)
if test_cfg["restore_backup"]: DBAAS_OPS["RESTORE_BACLUP"](config, db_cfg)
