'''
Created on May 17, 2018

@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-05-17   umartine    Initial creation
    2018-05-25   umartine    Adding new task operation for delays 
    2018-12-04   umartine    Check and use compartment for VCN 
    2019-01-04   umartine    Return created VCN

'''

import time

import oci_cli_qa.lib.operations.vcn as vcn

def lifespan_delay(_, test_config):
    wait_time = 0
    if test_config["CREATE"] and test_config["TERMINATE"]:
        wait_time = test_config["LIFE_SPAN"]
    time.sleep(wait_time * 60)

def pre_functional_delay(_, test_config):
    time.sleep(test_config["INSTANCE_SSH_WAIT"] * 60)


def create_vcn(config, compartment_id = None):
    if compartment_id == None:
        compartment_id = config["tenancy"]
    output_vcn = vcn.get_vcn(config, compartment_id, "PAAS_VCN")
    if output_vcn == None:
        vcn.create_full_vcn(config, compartment_id)
    return output_vcn

AUXILIAR_OPS = {"DELAY":  lifespan_delay,
                "PRE_FT": pre_functional_delay 
               }
