'''
Created on Jun 26, 2018

@author: umartine


===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    -----------------------------------------------------------------------
    2018-06-26   umartine    Initial creation

'''

import oci_cli_qa.lib.operations.approved_sender as approved_sender

def create_approve_sender(config, ap_cfg):
    approved_sender.create_instance(config, ap_cfg)


def delete_approve_sender(config, ap_cfg):
    approved_sender.terminate_instance(config, ap_cfg)


def send_emails(config, ap_cfg):
    approved_sender.send_email(config, ap_cfg)


EMAIL_OPS = {"CREATE":     create_approve_sender,
             "FUNCTIONAL": send_emails, 
             "TERMINATE":  delete_approve_sender
            }
