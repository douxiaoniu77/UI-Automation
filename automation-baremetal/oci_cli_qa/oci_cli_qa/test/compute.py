'''
Created on May 17, 2018

@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-05-17   umartine    Initial creation
    2018-05-24   umartine    Generalizing get IP flow
    2018-05-01   umartine    Fixing Compartment ID from functional flow
    2018-06-11   umartine    Removing unused libs
    2018-11-21   umartine    Adding create fake instance features
    2018-12-05   umartine    Support for compartment selection
    2018-12-06   umartine    Fix in compartment selection
    2019-01-04   umartine    Handling exceptions for limits and authorization
    2019-01-09   umartine    Check if test end up properly

'''

from oci.exceptions import ServiceError

import oci_cli_qa.lib.operations.compute as compute

from oci_cli_qa.lib.runner import run_command_paramiko
from oci_cli_qa.lib.logger import LOG

from oci_cli_qa.lib.scenarios import TEST_TYPE_PROVISIONING
from oci_cli_qa.lib.scenarios import TEST_TYPE_LIMIT
from oci_cli_qa.lib.scenarios import TEST_TYPE_AUTHORIZATION
from oci_cli_qa.test.auxiliar import create_vcn


'''
-------------------------------------------------------------------------------
                        Shared Functions
-------------------------------------------------------------------------------
'''

def handle_exception(e, test_type):
    LOG.info("Service Exception found")
    LOG.info(e)
    if test_type == TEST_TYPE_LIMIT:
        LOG.info("Testing Service Limit")
        assert e.status == 400
        assert e.code == "LimitExceeded"
    elif test_type == TEST_TYPE_AUTHORIZATION:
        LOG.info("Testing Authorization")
        assert e.status == 404
        assert e.code == "NotAuthorizedOrNotFound"
    else:
        LOG.info("Exception not handable for this test type")
        LOG.info("Test type: '{0}'".format(test_type))
        assert False


'''
-------------------------------------------------------------------------------
                        Operation runners
-------------------------------------------------------------------------------
'''

def create_instance(config, instance_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        compute.create_instance(config, instance_cfg)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def create_instance_fake(config, instance_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        compute.create_instance(config, instance_cfg, fake_instance = True)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def compute_functional_instance(config, instance_cfg):
    instance_name = instance_cfg["display_name"]
    ip_address = compute.get_instance_public_ip(config, instance_cfg["compartment"], instance_name)
    cmd_output = run_command_paramiko(ip_address, instance_cfg["ssh_key"], "ls")
    assert cmd_output["rc"] == 0


def compute_list_instance(config, instance_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        instance = compute.get_instance(config,
                                        instance_cfg["compartment"],
                                        instance_cfg["display_name"]
                                        )
        assert instance != None
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def termiante_instance(config, instance_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        compute.terminate_instante(config, instance_cfg)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


def attach_vcn(config, instance_cfg, test_type = TEST_TYPE_PROVISIONING):
    try:
        create_vcn(config, compartment_id = instance_cfg["vcn_compartment"])
        compute.create_instance(config, instance_cfg)
        assert  test_type == TEST_TYPE_PROVISIONING
    except ServiceError as e:
        handle_exception(e, test_type)


COMPUTE_OPS = {"CREATE":        create_instance,
               "FUNCTIONAL":    compute_functional_instance,
               "TERMINATE":     termiante_instance,
               "CREATE_FAKE":   create_instance_fake,
               "LIST":          compute_list_instance,
               "ATTACH":        attach_vcn
               }
