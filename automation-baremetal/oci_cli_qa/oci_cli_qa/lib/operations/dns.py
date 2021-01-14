'''
Created on May 29, 2018

@author: umartine, ravison


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-29   umartine    Initial creation
    2018-12-12   ravison     Added fetch records logic for dnsflood 
    2018-12-13   umartine    Refactoring code


'''

import oci

from oci_cli_qa.lib.logger import LOG
from oci.identity.models import compartment

def get_dns_client(config):
    LOG.info("Get DNS Client")
    return oci.dns.DnsClient(config)


def create_zone(config, zone_name, compartment_id):
    LOG.info("[START] Create DNS Zone")
    LOG.info("Zone Name: '{0}'".format(zone_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    dns_client = get_dns_client(config)
    zone_details = oci.dns.models.CreateZoneDetails(
            name = zone_name,
            zone_type = 'PRIMARY',
            compartment_id = compartment_id
        )
    dns_create_response = dns_client.create_zone(zone_details)
    dns_zone = dns_create_response.data;
    LOG.info(dns_zone)
    LOG.info("[END] Create DNS Zone")
    return dns_zone


def get_zone(config, zone_name, compartment_id):
    LOG.info("[START] Get DNS Zone")
    LOG.info("Zone Name: '{0}'".format(zone_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    dns_client = get_dns_client(config)
    
    dns_zone_list = oci.pagination.list_call_get_all_results(
        dns_client.list_zones, compartment_id).data
    dns_zone = None
    for dns_zone_aux in dns_zone_list:
        if dns_zone_aux.name == zone_name:
            dns_zone = dns_zone_aux
    LOG.info(dns_zone)    
    LOG.info("[END] Get DNS Zone")
    return dns_zone

def get_dns_records(config, zone_name):
    LOG.info("[START] Getting DNS records for zone " + zone_name)
    dns_client = get_dns_client(config)
    dns_zone_records = dns_client.get_zone_records(zone_name).data.items
    LOG.info(dns_zone_records)
    LOG.info("[END] Get DNS Records")
    return dns_zone_records
    

def delete_zone(config, zone_name, compartment_id):
    LOG.info("[START] Delete DNS Zone")
    LOG.info("DNS Zone Name: '{0}'".format(zone_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    dns_client = get_dns_client(config)
    dns_client.delete_zone(zone_name)
    LOG.info("[END] Delete DNS Zone")

