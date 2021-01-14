'''
Created on Apr 2, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-04-02   umartine    Initial creation
    2018-04-03   umartine    Terminate LB flow
    2018-04-03   umartine    Adding details of LB created
    2018-12-04   umartine    Support for compartment selection

'''

import oci
from oci.load_balancer.models import CreateLoadBalancerDetails

import oci_cli_qa.lib.operations.vcn as vcn
from oci_cli_qa.lib.operations.identity import AD_LIST
from oci_cli_qa.lib.logger import LOG


def get_lb_client(config):
    LOG.info("Get Load Balancer Client")
    return oci.load_balancer.load_balancer_client.LoadBalancerClient(config)


def get_subnets_for_lb(config, vnc_name):
    LOG.info("[START] Get Subnets for Load Balancer")
    vcn_instance = vcn.get_vcn(config,
                               config["tenancy"],
                               vnc_name)
    subnets = vcn.list_subnets(config,
                               config["tenancy"],
                               vcn_instance.id)
    subnets_ids = []
    for ad in AD_LIST:
        for subnet in subnets:
            if ad in subnet.availability_domain:
                LOG.info("Subnet added for AD '{0}'".format(ad))
                LOG.info(subnet)
                subnets_ids.append(subnet.id)
                break
    LOG.info("[END] Get Subnets for Load Balancer")
    return subnets_ids


def get_lb(config, lb_name, compartment_id):
    LOG.info("[START] Get Load Balancer")
    LOG.info("Load Balancer name: '{0}'".format(lb_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    lb_client = get_lb_client(config)
    load_balancers = lb_client.list_load_balancers(compartment_id).data
    load_balancer = None
    for load_balancer_aux in load_balancers:
        if load_balancer_aux.display_name == lb_name:
            load_balancer = load_balancer_aux
    LOG.info(load_balancer)
    LOG.info("[END] Get Load Balancer")
    return load_balancer


def create_lb_instance(config, lb_cfg):
    LOG.info("[START] Create Load Balancer")
    LOG.info(lb_cfg)
    lb_client = get_lb_client(config)
    subnets_ids = get_subnets_for_lb(config, lb_cfg["vcn"])
    lb_details = CreateLoadBalancerDetails(compartment_id = lb_cfg["compartment"],
                                           display_name = lb_cfg["display_name"],
                                           shape_name = lb_cfg["shape"],
                                           subnet_ids = subnets_ids
                                           )
    lb_request_id = lb_client.create_load_balancer(lb_details).headers['opc-work-request-id']
    LOG.info("Wait the operation to complete")
    oci.wait_until(lb_client, 
                   lb_client.get_work_request(lb_request_id), 
                   'lifecycle_state',
                   'SUCCEEDED'
                   )
    lb_response = lb_client.get_work_request(lb_request_id)
    LOG.info("Operation completed")
    LOG.info(lb_response.data)
    LOG.info("Listing LB created")
    lb_instance = get_lb(config, lb_cfg["display_name"], lb_cfg["compartment"])
    LOG.info(lb_instance)
    LOG.info("[END] Create Load Balancer")
    return lb_instance


def terminate_lb_instance(config, lb_name):
    LOG.info("[START] Terminate Load Balancer")
    LOG.info("Load Balancer name: '{0}'".format(lb_name))
    lb_client = get_lb_client(config)
    lb_instance = get_lb(config, lb_name, config["tenancy"])
    lb_request_id = lb_client.delete_load_balancer(lb_instance.id).headers['opc-work-request-id']
    LOG.info("Wait the operation to complete")
    oci.wait_until(lb_client,
                   lb_client.get_work_request(lb_request_id),
                   'lifecycle_state',
                   'SUCCEEDED'
                   )
    lb_response = lb_client.get_work_request(lb_request_id)
    LOG.info("Operation completed")
    LOG.info(lb_response.data)
    LOG.info("[END] Terminate Load Balancer")
