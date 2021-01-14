'''
Created on May 17, 2018

@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-05-17   umartine    Initial creation
    2018-12-05   umartine    Support for compartment selection
    2019-01-08   umartine    Exception handling and get bucket function
    2019-01-09   umartine    Minor fixes and checking if the test end up properly

'''
import pytest

from oci.exceptions import ServiceError

import oci_cli_qa.lib.operations.object_storage as obs

from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.scenarios import TEST_TYPE_PROVISIONING
from oci_cli_qa.lib.scenarios import TEST_TYPE_AUTHORIZATION

'''
-------------------------------------------------------------------------------
                        Shared Functions
-------------------------------------------------------------------------------
'''

def handle_exception(e, test_type):
    LOG.info("Service Exception found")
    LOG.info(e)
    if test_type == TEST_TYPE_AUTHORIZATION:
        LOG.info("Testing Authorization")
        assert e.status in [404, 409]
        assert e.code in ["NamespaceNotFound", "BucketNotFound", "BucketAlreadyExists"]
    else:
        LOG.info("Exception not handable for this test type")
        LOG.info("Test type: '{0}'".format(test_type))
        assert False


'''
-------------------------------------------------------------------------------
                        Operation runners
-------------------------------------------------------------------------------
'''

def create_bucket(config, bucket_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        compartment_id = bucket_cfg["compartment"]
        name = bucket_cfg["display_name"]
        tier = bucket_cfg["shape"]
        obs.create_bucket(config, compartment_id, name, tier)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def upload_content(config, bucket_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        name = bucket_cfg["display_name"]
        amount = int(bucket_cfg["amount"])
        obs.upload_content(config, name, amount)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def delete_content(config, bucket_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        name = bucket_cfg["display_name"]
        amount = int(bucket_cfg["amount"])
        obs.delete_content(config, name, amount)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def delete_bucket(config, bucket_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        name = bucket_cfg["display_name"]
        obs.delete_bucket(config, name)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def list_bucket(config, bucket_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        compartment_id = bucket_cfg["compartment"]
        name = bucket_cfg["display_name"]
        bucket = obs.get_bucket(config, name, compartment_id)
        assert bucket != None
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def attach(config, bucket_cfg, test_type = TEST_TYPE_PROVISIONING):
    LOG.info("Attachment not supported")
    pytest.skip("Test case skipped")
    pass


OBJECT_STORAGE_OPS = {"CREATE":    create_bucket,
                      "UPSIZE":    upload_content,
                      "DOWNSIZE":  delete_content,
                      "TERMINATE": delete_bucket,
                      "LIST":      list_bucket,
                      "ATTACH":    attach
                      }
