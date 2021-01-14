'''
Created on Mar 15, 2018
@author: xueniu

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-03-15   xueniu     Initial creation
    2018-06-26   umartine   Select provider dinamically

'''

import oci

##import oci_cli_qa.lib.operations.identity as identity
from oci_cli_qa.lib.logger import LOG
from oci.core.models import CreateVirtualCircuitDetails
from oci.core.models import CrossConnectMapping
from compiler.ast import List
#from oci.core import VirtualNetworkClientCompositeOperations
import  random
import time

def get_vcn_client(config):
    LOG.info("Get VCN Client")
    return oci.core.virtual_network_client.VirtualNetworkClient(config)

#def get_vcn_client_additional(client):
 #   LOG.info("Get VCN Client Additional")
  #  return oci.core.virtual_network_client_composite_operations.VirtualNetworkClientCompositeOperations(client)

def create_drg(config, compartment_id, display_name="automation_drg"):
    LOG.info("[START] Create new DRG '{0}'".format(display_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    vcn_client = get_vcn_client(config)
   ## drg_display_name = display_name + random.randint(100,1000),
    drg_details = oci.core.models.CreateDrgDetails(
        compartment_id = compartment_id,
        display_name = display_name,
        )
    LOG.info("Request Sent")
    drg_response = vcn_client.create_drg(drg_details)
    LOG.info(drg_response.data)
  
    LOG.info("Wait the operation to complete")
    drg_response = oci.wait_until(
        vcn_client,
        vcn_client.get_drg(drg_response.data.id),
        "lifecycle_state",
        "AVAILABLE"
    )
    LOG.info("[END] Create new DRG")
    return drg_response.data

def get_drg_id_for_fc(config, drg_name, compartment_id):
    LOG.info("[START] Get DRG Instance")
    LOG.info("DRG name: '{0}'".format(drg_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    vcn_client = get_vcn_client(config)
    drgs = vcn_client.list_drgs(compartment_id).data
    drg = None
    for drg_element in drgs:
        if drg_element.display_name == drg_name:
            drg = drg_element
    LOG.info(drg)
    LOG.info("[END] Get DRG Instance")
    drg_id = drg.id
    return drg_id

def get_drg_instance(config, drg_name, compartment_id):
    LOG.info("[START] Get DRG Instance")
    LOG.info("DRG name: '{0}'".format(drg_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    vcn_client = get_vcn_client(config)
    drgs = vcn_client.list_drgs(compartment_id).data
    drg = None
    for drg_element in drgs:
        if drg_element.display_name == drg_name:
            drg = drg_element
    LOG.info(drg)
    LOG.info("[END] Get DRG Instance")
    return drg

def get_fc_provider(config, compartment_id):
    LOG.info("[START] Get Fast Connect Provider")
    vcn_client = get_vcn_client(config)
    LOG.info("Get provider's list")
    providers = vcn_client.list_fast_connect_provider_services(compartment_id).data
    LOG.info(providers)
    LOG.info("Select first provider")
    provider = providers[0]
    LOG.info(provider)
    LOG.info("[START] Get Fast Connect Provider")
    return provider


def create_fast_connect(config, fc_cfg):
    
    create_drg(config, config["tenancy"], "automation_drg")
    LOG.info("[START] Create Fast Connect")
    LOG.info(fc_cfg)
    vcn_client = get_vcn_client(config)
    ###List<CrossConnectMapping> 
    cross_connect_mappings_details = CrossConnectMapping (###bgp_md5_auth_key = " ",
                                         ###cross_connect_or_cross_connect_group_id = " ",          
                                         customer_bgp_peering_ip = fc_cfg["curstomer_bigip_address"],
                                         oracle_bgp_peering_ip =  fc_cfg["oracle_bigip_address"],
                                         ###vlan = 123                  
                                         )
    LOG.info(cross_connect_mappings_details)
    ##drg_id = get_drg_id_for_fc(config, drg_display_name,config["tenancy"])
    drg_id = get_drg_id_for_fc(config, fc_cfg["drg_display_name"],config["tenancy"])
    provider_service = get_fc_provider(config, config["tenancy"])
    fc_details = CreateVirtualCircuitDetails(bandwidth_shape_name = fc_cfg["provisioned_bandwidth"],
                                           compartment_id = config["tenancy"],
                                           cross_connect_mappings = [cross_connect_mappings_details],
                                           ##cross_connect_mappings_details,
                                           customer_bgp_asn = int(fc_cfg["customer_bgp_asn"]),
                                           display_name =  fc_cfg["display_name"],
                                           gateway_id = drg_id,
                                          ## provider_name = fc_cfg["provider"],
                                           provider_service_id = provider_service.id,
                                           ##provider_service_name = fc_cfg["provider_service"],
                                           type = fc_cfg["type"]
                                           )
    
    LOG.info("Request Sent")
    fc_response = vcn_client.create_virtual_circuit(fc_details)
    LOG.info(fc_response.data)
    LOG.info("Wait the operation to complete")
    fc_response = oci.wait_until(
        vcn_client,
        vcn_client.get_virtual_circuit(fc_response.data.id),
        "lifecycle_state",
        "PENDING_PROVIDER"
    )
    LOG.info("Operation completed")
    LOG.info(fc_response.data)
    LOG.info("Listing FC created")
    fc_instance = get_fc(config, fc_cfg["display_name"], config["tenancy"])
    LOG.info(fc_instance)
    LOG.info("[END] Create Fast Connect")
    return fc_instance


     
def get_fc(config, fc_name, compartment_id):
    LOG.info("[START] Get Fast Connect Instance")
    LOG.info("Fast Connect name: '{0}'".format(fc_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    vcn_client = get_vcn_client(config)
    fast_connects = vcn_client.list_virtual_circuits(compartment_id).data
    fast_connect = None
    for fast_connect_element in fast_connects:
        if fast_connect_element.display_name == fc_name:
            fast_connect = fast_connect_element
    LOG.info(fast_connect)
    LOG.info("[END] Get Fast Connect Instance")
    return fast_connect


def create_fast_connect1(config, provider_name, bandwidth_shape_name,compartment_id, display_name="automation_drg"):
    LOG.info("[START] Create new DRG '{0}'".format(compartment_id))
    LOG.info("Compartment ID: '{0}'".format(display_name))
    vcn_client = get_vcn_client(config)
    drg_details = oci.core.models.CreateDrgDetails(
        compartment_id = compartment_id,
        display_name = display_name,
        )
    drg_response = vcn_client.create_drg(drg_details)
    LOG.info(drg_response.data)
    LOG.info("[END] Create new DRG")
    return drg_response.data



def terminate_fast_connect(config, fc_name):
    LOG.info("[START] Fast Connect Terminate Test")
    LOG.info("Fast Connect Name: '{0}'".format(fc_name))
    vcn_client = get_vcn_client(config)
    fc_instance = get_fc(config, fc_name, config["tenancy"])
    LOG.info("[START] Terminating")
    fc_request_id =  vcn_client.delete_virtual_circuit(fc_instance.id)
   
   # vcn_client_additional = get_vcn_client_additional(vcn_client)
    
    
    ##oci.core.VirtualNetworkClientCompositeOperations(vcn_client).delete_virtual_circuit_and_wait_for_state(fc_instance.id, wait_for_states="TERMINATED")
   ## vcn_client_additional.delete_virtual_circuit_and_wait_for_state(fc_instance.id, wait_for_states="TERMINATED")

    LOG.info("[END] Fast Connect gets Terminated")

    
    terminate_drg(config, "automation_drg")
     

def terminate_drg(config, drg_name):
    LOG.info("[START] Terminate Dynamic Routing Gateways")
    LOG.info("Dynamic Routing Gateways Name: '{0}'".format(drg_name))
    vcn_client = get_vcn_client(config)
    drg_instance = get_drg_instance(config, drg_name, config["tenancy"])
    time.sleep(10)
  #  vcn_client_additional = get_vcn_client_additional(vcn_client)
    LOG.info("[START] Terminating")
    ##oci.core.VirtualNetworkClientCompositeOperations(vcn_client).delete_drg_and_wait_for_state(drg_instance.id, wait_for_states="TERMINATED")
   ## vcn_client_additional.delete_drg_and_wait_for_state(drg_instance.id, wait_for_states="TERMINATED")
    drg_response = vcn_client.delete_drg(drg_instance.id)
   
    LOG.info("Operation completed")
    LOG.info("[END] Dynamic Routing Gateways gets Terminated")
    
       
