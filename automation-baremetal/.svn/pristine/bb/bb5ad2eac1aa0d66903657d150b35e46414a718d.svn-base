'''
Created on Mar 26, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-26   umartine    Initial creation
    2018-03-27   umartine    Add delete content scenario
    2018-05-14   umartine    Remove unused modules
    2018-05-15   umartine    Rename configuration parameters
    2018-06-01   umartine    Using test job code to process requests

'''

import json

import oci_cli_qa.lib.configuration as configuration
from oci_cli_qa.test.object_storage import OBJECT_STORAGE_OPS

# Configuration files
BUCKET_CONFIGURATION_JSON = "config/job/os_bucket/config.json"
TEST_CONFIGURATION_JSON = "config/job/os_bucket/tests.json"

# Load configuration
config = configuration.load_configuration()
bucket_cfg = json.load(open(BUCKET_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))

# Run jobs
if test_cfg["create_bucket"]: OBJECT_STORAGE_OPS["CREATE"](config, bucket_cfg)
if test_cfg["upload_content"]: OBJECT_STORAGE_OPS["UPSIZE"](config, bucket_cfg)
if test_cfg["delete_content"]: OBJECT_STORAGE_OPS["DOWNSIZE"](config, bucket_cfg)
if test_cfg["delete_bucket"]: OBJECT_STORAGE_OPS["TERMINATE"](config, bucket_cfg)
