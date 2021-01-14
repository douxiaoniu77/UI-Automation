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

import oci_cli_qa.lib.operations.fast_connect as fast_connection

def create_fast_connect(config, fc_cfg):
    fast_connection.create_fast_connect(config, fc_cfg)


def terminate_fast_connect(config, fc_cfg):
    fast_connection.terminate_fast_connect(config, fc_cfg["display_name"])


FAST_CONNECT_OPS = {"CREATE":     create_fast_connect,
                    "TERMINATE":  terminate_fast_connect
                    }
