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
import oci_cli_qa.lib.operations.blockvolume as block_storage

def create_block_volume(config, bs_cfg):
    block_storage.create_instance(config, bs_cfg)


def delete_block_volume(config, bs_cfg):
    block_storage.delete_instance(config, bs_cfg["display_name"])


BLOCK_STORAGE_OPS = {"CREATE":    create_block_volume,
                     "TERMINATE": delete_block_volume
                     }
