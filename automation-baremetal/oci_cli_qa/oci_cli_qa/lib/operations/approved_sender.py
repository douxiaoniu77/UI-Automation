'''
Created on May 29, 2018

@author: Qin


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-05-29   qxionng     Initial Creation
    2018-06-26   umartine    Change email address for display name and parsing
    2018-08-21   umartine    Remove paramiko and use local swaks
    2018-08-22   umartine    Use internal server for Swaks server

'''
import time

import oci

from oci_cli_qa.lib.logger import LOG
from oci_cli_qa.lib.runner import run_command_paramiko
from oci_cli_qa.lib.operations.identity import create_smtp_credentials, delete_smtp_credentials

RECEIVER = "blackhole@dyn.com"
SWAK_SEND = "swaks --server {0}:25 -tls --from {1} --to {2} --auth-user {3} --auth-password '{4}'"
SWAKS_SERVER = "129.213.151.42"
SSH_KEY = "config/SSH_KEY"


def get_email_client(config):
    LOG.info("[START] Get Email client")
    return oci.email.EmailClient(config)


def get_instance(config,compartment_id, instance_name,status=None):
    LOG.info("[START] Get Email Approved Sender")    
    LOG.info("Approved Sender: '{0}'".format(instance_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    email_client=get_email_client(config)
    
    instances=email_client.list_senders(compartment_id).data
    for instance_aux in instances:
        if instance_name == instance_aux.email_address:
            if status == None or instance_aux.lifecycle_state == status:
                LOG.info("Approved Sender found")
                instance = instance_aux
    LOG.info(instance)
    LOG.info("[END] Get Approved Sender")
    return instance
    
def create_instance(config,instance_cfg):
    LOG.info("[START] Create Approved Sender")
    LOG.info(instance_cfg)
    email_client = get_email_client(config)
    email_adress = "{0}@dyn.com".format(instance_cfg["display_name"])
    instance_details=oci.email.models.CreateSenderDetails(
        compartment_id = config["tenancy"],
        email_address = email_adress
        )
    instance_response=email_client.create_sender(instance_details)
    LOG.info(instance_response.data)
    
    LOG.info("Wait the operation to complete")
    instance_response = oci.wait_until(
        email_client,
        email_client.get_sender(instance_response.data.id),
        "lifecycle_state",
        "ACTIVE"
    )
    
    LOG.info("Operation completed")
    LOG.info("[END] Create Approved Sender")
    return instance_response.data


def terminate_instance(config,instance_cfg):
    LOG.info("[START] Delete Approved Sender")
    LOG.info(instance_cfg)
    email_client=get_email_client(config)
    email_adress = "{0}@dyn.com".format(instance_cfg["display_name"])
    instance = get_instance(config,
                            config["tenancy"],
                            email_adress,
                            status = 'ACTIVE'
                                    )
    LOG.info(instance)
    LOG.info("Delete Approved Sender")
    
    email_client.delete_sender(instance.id)
    
    LOG.info("Operation completed")
    LOG.info("[END] Delete Approved Sender")


def send_email(config, instance_cfg):
    LOG.info("[START] Send emails")
    sender= "{0}@dyn.com".format(instance_cfg["display_name"])
    number = instance_cfg["number"]
    server_name = instance_cfg["server_name"]
    smtp_response = create_smtp_credentials(config)
    user_name = smtp_response.username
    password = smtp_response.password
    LOG.info("Waiting one minute to STMP credential creation")
    time.sleep(60)
    LOG.info("Send emails")
    cmd = SWAK_SEND.format(server_name, sender, RECEIVER, user_name, password)
    for _ in xrange(int(number)):
        cmd_output = run_command_paramiko(SWAKS_SERVER, SSH_KEY, cmd)
        assert 0 == cmd_output['rc']
    #Delete SMTP Credential
    delete_smtp_credentials(config, smtp_response.id)
    LOG.info("[END] Send emails")
