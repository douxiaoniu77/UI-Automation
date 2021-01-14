'''
Created on Jun 11, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-06-11   umartine    Initial creation
    2018-06-21   umartine    Support for backup tests
    2018-06-22   umartine    Support for add records and restore testing
    2018-06-25   umartine    Support for SHH Tunnel with Python
    2018-12-05   umartine    Support for compartment selection
'''

import cx_Oracle

import oci_cli_qa.lib.operations.dbsystem as dbsystem

from oci_cli_qa.lib.runner import run_command
from oci_cli_qa.lib.logger import LOG

CREATE_TABLE_SQL = "CREATE TABLE records_table (recordID int, recordData varchar(255))"
SELECT_TABLE_SQL = "SELECT * FROM records_table"
INSERT_TABLE_SQL = "INSERT INTO records_table (recordID, recordData) VALUES ({0}, 'data {0}')"
COMMIT_SQL = "COMMIT WORK"
GET_SCN_SQL = "SELECT current_scn FROM V$DATABASE"

INITIAL_RECORDS = 0
TOTAL_RECORDS = 30

"""
-------------------------------------------------------------------------------
                            Support Functions
-------------------------------------------------------------------------------
"""

def get_connection(config, db_cfg):
    LOG.info("[START] Get SQL Connection")
    LOG.info("Get IP Address")
    compartment_id = db_cfg["compartment"]
    instance_name = db_cfg["display_name"]
    ip_address = dbsystem.get_instance_public_ip(config, compartment_id, instance_name)
    
    LOG.info("Create SSH Tunel")
    cmd = ["ssh", "-o", "StrictHostKeyChecking no", "-i", db_cfg["ssh_key"],
           "opc@{0}".format(ip_address), "-L",
           "8888:{0}:1521".format(ip_address), "-N"]
    cmd_output = run_command(cmd, need_to_wait = False)
    #assert cmd_output["rc"]
    
    LOG.info("Get DB Details")
    db_unique_name = dbsystem.get_database_unique_name(config, db_cfg)
    host_domain_name = dbsystem.get_database_domain(config, db_cfg)
    service_name = "{0}.{1}".format(db_unique_name, host_domain_name)
    username = 'sys'
    password = db_cfg["admin_password"]
    dsn_tns = "localhost:8888/{0}".format(service_name)
    
    LOG.info("Connect to '{0}/{1}@{2} as SYSDBA'".format(username, password,
                                                         dsn_tns))
    connection = cx_Oracle.connect (username, password, dsn_tns,
                                    mode=cx_Oracle.SYSDBA)
    LOG.info("[START] Get SQL Connection")
    return connection


def insert_records(cursor, initial, total):
    LOG.info("Insert records")
    for i in xrange(initial, total):
        record_sql = INSERT_TABLE_SQL.format(i)
        LOG.info("Insert record query: '{0}'".format(record_sql))
        cursor.execute(record_sql)
    LOG.info("Commit work query: '{0}'".format(COMMIT_SQL))
    cursor.execute(COMMIT_SQL)
    LOG.info("Get current SCN after commit:")
    cursor.execute(GET_SCN_SQL)
    records = cursor.fetchall()
    LOG.info(records)
    

def list_records(cursor, expected = None):
    LOG.info("Get current SCN:")
    cursor.execute(GET_SCN_SQL)
    records = cursor.fetchall()
    LOG.info(records)
    cursor.execute(SELECT_TABLE_SQL)
    records = cursor.fetchall()
    LOG.info("All records")
    LOG.info("Total records: '{0}'".format(len(records)))
    LOG.info(records)
    if expected != None:
        expected_list = [(i, "data {0}".format(i)) for i in xrange(expected)]
        assert records == expected_list
    

"""
-------------------------------------------------------------------------------
                            Test Cases
-------------------------------------------------------------------------------
"""

def create_db_instance(config, db_cfg):
    dbsystem.create_instance(config, db_cfg)


def terminate_db_instance(config, db_cfg):
    dbsystem.terminate_instance(config, db_cfg)


def update_db_instance(config, db_cfg):
    dbsystem.update_instance(config, db_cfg)


def db_functional_instance(config, db_cfg):
    compartment_id = db_cfg["compartment"]
    instance_name = db_cfg["display_name"]
    ip_address = dbsystem.get_instance_public_ip(config, compartment_id, instance_name)
    cmd = ["ssh", "-o", "StrictHostKeyChecking no", "-i", 
           db_cfg["ssh_key"], "opc@{0}".format(ip_address), "ls"]
    cmd_output = run_command(cmd)
    assert cmd_output["rc"] == 0


def db_create_backup(config, db_cfg):
    LOG.info("[START] Create Backup")
    connection = get_connection(config, db_cfg)
    cursor = connection.cursor()
    LOG.info("Create table query: '{0}'".format(CREATE_TABLE_SQL))
    cursor.execute(CREATE_TABLE_SQL)
    insert_records(cursor, 0, INITIAL_RECORDS)
    list_records(cursor)
    connection.close()
    dbsystem.create_backup(config, db_cfg)
    LOG.info("[END] Create Backup")


def db_delete_backup(config, db_cfg):
    dbsystem.delete_backup(config, db_cfg)


def db_restore_backup(config, db_cfg):
    LOG.info("[START] Restore Backup")
    connection = get_connection(config, db_cfg)
    cursor = connection.cursor()
    insert_records(cursor, INITIAL_RECORDS, TOTAL_RECORDS)
    list_records(cursor)
    connection.close()
    dbsystem.restore_backup(config, db_cfg)
    LOG.info("[END] Restore Backup")


def db_list_records(config, db_cfg):
    LOG.info("[START] List Records")
    connection = get_connection(config, db_cfg)
    cursor = connection.cursor()
    list_records(cursor)
    connection.close()
    LOG.info("[END] List Records")



def db_add_records(config, db_cfg):
    LOG.info("[START] Add Records")
    connection = get_connection(config, db_cfg)
    cursor = connection.cursor()
    list_records(cursor)
    insert_records(cursor, INITIAL_RECORDS, TOTAL_RECORDS)
    list_records(cursor)
    connection.close()
    LOG.info("[END] Add Records")


DBAAS_OPS = {"CREATE":     create_db_instance,
             "UPDATE":     update_db_instance, 
             "TERMINATE":  terminate_db_instance,
             "FUNCTIONAL": db_functional_instance,
             "CREATE_BACKUP":  db_create_backup,
             "TERMINATE_BACKUP": db_delete_backup,
             "RESTORE_BACLUP": db_restore_backup,
             "LIST_RECORDS": db_list_records,
             "ADD_RECORDS": db_add_records,
            }
