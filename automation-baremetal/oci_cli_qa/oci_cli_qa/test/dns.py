'''
Created on May 30, 2018

@author: umartine, ravison


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-30   umartine    Initial creation
    2018-12-05   umartine    Support for compartment selection
    2018-12-12   ravison     Added query load using dnsflood
    2018-12-13   umartine    Refactoring code to allow any account to execute

'''

from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.runner import run_command_paramiko

import oci_cli_qa.lib.operations.dns as dns

DNS_FLOOD_SERVER = "129.213.151.42"
SSH_KEY = "config/SSH_KEY"

DIG_CMD = "dig +short {0}"
DNS_FLOOD_CMD = "dnsflood -address={0} -addname=5m-odd.{1},NS -qps=1 -count={2} -v=+extra"

def create_dns_zone(config, dns_config):
    compartment_id = dns_config["compartment"]
    zone_name = "{0}.com".format(dns_config["display_name"])
    dns.create_zone(config, zone_name, compartment_id)


def functional_dns_zone(config, dns_config):
    zone_name = "{0}.com".format(dns_config["display_name"])
    # Get first record
    dns_zone_records =  dns.get_dns_records(config, zone_name)
    single_dns_record = dns_zone_records[0].rdata
    LOG.info("Picking record '{0}' for DNS querying".format(single_dns_record))
    # Get DNS record IP
    dig_command = DIG_CMD.format(single_dns_record)
    record_ip = run_command_paramiko(DNS_FLOOD_SERVER, SSH_KEY, dig_command)["stdout"].splitlines()[0]
    # Execute DNS Flood
    dns_flood_command = DNS_FLOOD_CMD.format(record_ip, zone_name, dns_config["amount"]) 
    cmd_output = run_command_paramiko(DNS_FLOOD_SERVER, SSH_KEY, dns_flood_command)
    LOG.info("DNS Flood output: '{0}'".format(cmd_output["stdout"]))


def termiante_dns_zone(config, dns_config):
    compartment_id = dns_config["compartment"]
    zone_name = "{0}.com".format(dns_config["display_name"])
    dns.delete_zone(config, zone_name, compartment_id)


DNS_OPS = {"CREATE":     create_dns_zone,
           "FUNCTIONAL": functional_dns_zone, 
           "TERMINATE":  termiante_dns_zone
           }
