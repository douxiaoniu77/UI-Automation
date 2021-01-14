'''
Created on Mar 12, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-12   umartine    Full routine to enable PAAS testing
    2018-03-13   umartine    Re-packing operations lib

'''
import time

from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.operations.identity import config_psm_compartment
from oci_cli_qa.lib.operations.vcn import create_full_vcn

def create_compartment_vcn(config):
    LOG.info("[START] Create Compartment and VCN for PaaS")
    tenancy_ocid = config["tenancy"]
    LOG.info("Tenancy OCID: {0}".format(tenancy_ocid))

    LOG.info("Create PSM compartment")
    compartment_ocid = config_psm_compartment(config)

    LOG.info("Wait until compartment provisioning...")
    time.sleep(60)

    LOG.info("Create VCN")
    create_full_vcn(config, compartment_ocid)
    LOG.info("[END] Create Compartment and VCN for PaaS")
