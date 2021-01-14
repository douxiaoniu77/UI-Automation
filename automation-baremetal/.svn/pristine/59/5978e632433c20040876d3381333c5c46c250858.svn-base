'''
Created on May 17, 2018

@author: umartine

===============================================================================
                                Change log
===============================================================================

    Date         GUID        Comment
    ---------------------------------------------------------------------------
    2018-05-17   umartine    Initial creation

'''

import oci_cli_qa.lib.operations.load_balance as load_balancer

def create_lb(config, lb_cfg):
    load_balancer.create_lb_instance(config, lb_cfg)


def terminate_lb(config, lb_cfg):
    load_balancer.terminate_lb_instance(config, lb_cfg["display_name"])


LOAD_BALANCER_OPS = {"CREATE":    create_lb,
                     "TERMINATE": terminate_lb
                     }
