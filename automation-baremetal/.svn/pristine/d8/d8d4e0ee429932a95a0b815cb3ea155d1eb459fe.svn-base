'''
Created on Mar 26, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-26   umartine    Initial creation
    2018-03-27   umartine    Add delete content scenario
    2019-01-08   umartine    Add get bucket function
    
'''

import oci

from oci_cli_qa.lib.logger import LOG


# Generate 1MB file
FILE_CONTENT = 'A' * (1024 * 1024)


def get_os_client(config):
    LOG.info("Get Object Storage Client")
    return oci.object_storage.object_storage_client.ObjectStorageClient(config)


def create_bucket(config, compartment_id, name, tier):
    LOG.info("[START] Create new Bucket")
    LOG.info("Name: '{0}'".format(name))
    LOG.info("Type: '{0}'".format(tier))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    os_client = get_os_client(config)
    name_space = os_client.get_namespace().data
    LOG.info("Name Space: '{0}'".format(name_space))
    os_details = oci.object_storage.models.CreateBucketDetails(
        name = name,
        compartment_id = compartment_id,
        storage_tier = tier
        )
    os_bucket = os_client.create_bucket(name_space, os_details).data
    LOG.info(os_bucket)
    LOG.info("[END] Create new Bucket")
    return os_bucket


def upload_content(config, name, amount):
    LOG.info("[START] Upload Content")
    LOG.info("Name: '{0}'".format(name))
    LOG.info("Amount to be uploaded: '{0}'MB".format(amount))
    os_client = get_os_client(config)
    name_space = os_client.get_namespace().data
    LOG.info("Name Space: '{0}'".format(name_space))
    significant_part = (amount / 10)
    for i in xrange(1, amount + 1):
        progess = (100 * i) / amount
        if i % significant_part == 0:
            LOG.info("Progress: {0}%".format(progess))
        object_name = "part_{0}.txt".format(i)
        os_client.put_object(name_space, name, object_name, FILE_CONTENT).data
    LOG.info("[END] Upload Content")


def delete_object(os_client, name, object_name):
    name_space = os_client.get_namespace().data
    os_client.delete_object(name_space, name, object_name)


def delete_content(config, name, amount): 
    LOG.info("[START] Delete capacity")
    LOG.info("Name: '{0}'".format(name))
    LOG.info("Amount to be deleted: '{0}'MB".format(amount))
    os_client = get_os_client(config)
    name_space = os_client.get_namespace().data
    LOG.info("Name Space: '{0}'".format(name_space))
    significant_part = (amount / 10)
    object_list = os_client.list_objects(name_space, name).data.objects
    for i in xrange(1, amount + 1):
        progess = (100 * i) / amount
        if i % significant_part == 0:
            LOG.info("Progress: {0}%".format(progess))
        os_file = object_list.pop(0)
        delete_object(os_client, name, os_file.name)
        if not object_list:
            object_list = os_client.list_objects(name_space, name).data.objects
    LOG.info("[END] Delete capacity")


def get_bucket(config, name, compartment_id):
    LOG.info("[START] Get Bucket")
    LOG.info("Name: '{0}'".format(name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    os_client = get_os_client(config)
    name_space = os_client.get_namespace().data
    buckets = os_client.list_buckets(name_space, compartment_id).data
    bucket = None
    for aux_bucket in buckets:
        if aux_bucket.name == name:
            LOG.info("Bucket Found")
            bucket = aux_bucket
    LOG.info(bucket)
    LOG.info("[END] Get Bucket")
    return bucket


def delete_bucket(config, name):
    LOG.info("[START] Delete Bucket")
    LOG.info("Name: '{0}'".format(name))
    os_client = get_os_client(config)
    name_space = os_client.get_namespace().data
    LOG.info("Name Space: '{0}'".format(name_space))
    LOG.info("Deleting Objects...")
    object_list = os_client.list_objects(name_space, name).data.objects
    while object_list:
        for os_file in object_list:
            delete_object(os_client, name, os_file.name)
        object_list = os_client.list_objects(name_space, name).data.objects
    os_client.delete_bucket(name_space, name).data
    LOG.info("[END] Delete Bucket")
