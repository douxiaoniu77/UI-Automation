'''
Created on May 4, 2018
@author: umartine


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-04   umartine    Initial creation
    2018-05-23   umartine    Create and Delete File System Scenarios
    2018-05-24   umartine    Move test scenarios to the test module
    2018-05-25   umartine    Define Test Configuration file for job setup

'''

import json, time

import oci_cli_qa.test.file_system as file_system
import oci_cli_qa.lib.configuration as configuration

# Configuration files
FILE_SYTEM_CONFIGURATION_JSON = "config/job/file_system/config.json"
TEST_CONFIGURATION_JSON = "config/job/file_system/tests.json"

# Load configuration
fs_config = json.load(open(FILE_SYTEM_CONFIGURATION_JSON))
test_cfg = json.load(open(TEST_CONFIGURATION_JSON))
config = configuration.load_configuration()

# Run jobs
if test_cfg["create_fss"]: file_system.create_file_system(config, fs_config)
if test_cfg["create_fss"]: file_system.functional_file_systen(config, fs_config)
if test_cfg["upload_content"]: file_system.upsize_file_systen(config, fs_config)
if test_cfg["create_fss"] and test_cfg["delete_fss"]: time.sleep(10 * 60)
if test_cfg["delete_content"]: file_system.downsize_file_systen(config, fs_config)
if test_cfg["delete_fss"]: file_system.terminate_file_system(config, fs_config)
