'''
Created on Dec 3, 2018

@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-12-03   umartine    Initial creation 
    2018-12-04   umartine    Modify Create compartment and get compartment

'''

import oci_cli_qa.lib.operations.identity as identity


'''
===============================================================================
                       Compartment features testing
===============================================================================
'''

def no_compartment_details(config, __):
    return config["tenancy"]


def create_compartment(config, compartment_cfg):
    parent_compartment = identity.get_compartment(config, compartment_cfg["parent"])
    if parent_compartment == None:
        parent_compartment = config["tenancy"]
    compartment = identity.create_compartment(config, compartment_cfg["name"], compartment_cfg["desc"], parent_compartment)
    return compartment.id


def get_existing_compartment(config, compartment_cfg):
    compartment = identity.get_compartment(config, compartment_cfg["name"])
    if compartment == None:
        return config["tenancy"]
    return compartment.id


def delete_compartment(config, compartment_cfg):
    compartment_name = compartment_cfg["name"]
    identity.delete_compartment(config, compartment_name)
    return None


COMPARTMENT_BAR_OPS = { "NONE": no_compartment_details,
                        "CREATE":  create_compartment,
                        "EXISTING": get_existing_compartment,
                        "DELETE": delete_compartment
                      }


'''
===============================================================================
                       Compartment features testing
===============================================================================
'''