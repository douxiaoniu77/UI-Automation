'''
Created on Mar 14, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-14   umartine    Initial creation

'''

import json

from oci_cli_qa.lib.configuration import load_configuration
from oci_cli_qa.lib.operations.vcn import create_full_vcn

INSTANCE_CONFIGURATION_JSON = "config/job/vcn_instance_cfg.json"

vcn_instance_cfg = json.load(open(INSTANCE_CONFIGURATION_JSON))
vcn_name = vcn_instance_cfg["vcn_name"]

config = load_configuration()
create_full_vcn(config, config["tenancy"], vcn_name)
