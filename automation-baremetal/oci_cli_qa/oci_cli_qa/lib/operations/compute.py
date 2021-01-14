'''
Created on Mar 13, 2018
@author: umartine

===============================================================================
                                     Change log
===============================================================================

    Date         GUID        Comment
    ----------------------------------------------------------------
    2018-03-13   umartine    Initial creation
    2018-03-14   umartine    Complete full instance creation flow
    2018-03-14   umartine    Display IP Address
    2018-03-15   umartine    Terminate instance flow
    2018-03-16   umartine    Add support to pick subnet by AD 
    2018-03-29   umartine    Change image used
    2018-04-02   umartine    Job to report Instance details
    2018-04-18   umartine    Job to create instance connection
    2018-04-18   umartine    Add support for connection flow
    2018-05-24   umartine    Generalizing get IP flow
    2018-11-21   umartine    Adding create fake instance features
    2018-12-04   umartine    Support for compartment selection
    2019-01-04   umartine    Support for admin account execution of pre-req
    2019-01-07   umartine    Add support for VCN in different compartment

'''

import time
#import wmi_client_wrapper as wmi

import oci
import oci_cli_qa.lib.operations.vcn as vcn
import oci_cli_qa.lib.operations.identity as identity
import oci_early_test.lib.rest_wrapper as rest_wrapper

from oci_cli_qa.lib.logger import LOG

ENDPOINTS = [ {"name": "LAUNC_INSTANCE_R1", 
               "uri": "https://iaas.r1.oracleiaas.com/20160918/instances"}]

TEST_DETAILS = { "account": "MAIN_ACCOUNT_R1",
                 "endpoint": "LAUNC_INSTANCE_R1",
                 "request_type": "POST",
                 "expected_response": "200",
                 "path": {},
                 "header": { "opc-host-serial": "FAKE",
                             "opc-pool-name": ""
                           }
               }

ACCOUNTS = [{ "name": "MAIN_ACCOUNT_R1",
              "api_footprint": "09:e4:86:07:74:59:8a:94:5f:7f:06:4e:cc:ae:9e:16",
              "api_privatekey": "-----BEGIN RSA PRIVATE KEY-----\nMIIEpQIBAAKCAQEAxgGZveKS4hO1ActrYRx/eeqBS8wX6SG7BfnwgwcuoQwdF5Kg\nLyrhoUKUSLCEVnoj2ocKahF7XxwRNumEHZ9LpQj37+HBVFlK3T3O9nZP532k7Z1D\nvDeQkYJFRjliDdmPERn2H5sBEMbQ8tTo3snm1WV/mAprA/w7KxNxLRe4zc8Mdlzi\nda94dtOhCK8tAax8uvQxXq6qswPjdLGLi8Zsl1qtspDEQvFdhvshlCpjaHQYe1cd\nd0QAVnc8A+GElNCF9IGWnTcQFn7v10MFZpPwgfRAfYL5pgsZCcRZGofM4kY3N3tj\n6Xb/q/2fN8ldAN2WQWAKnmYP+5r2W3CVds6S0wIDAQABAoIBAQCi6sA354vZbkOZ\ndWklnxAYHOZDmmr2DCXY1fyZorgwFEp/kcow5QR+7cwaPvq0OIz1ifT8ruOmru5P\nEzX8NYxE/ysZedPbdhjODsQSL+iw3MVuKN6Jp+JQhzI+hIp0QNShSo4jFSN8TUmw\n85Ojc9Z1MK0aSUAiKE/6smWFp6YL12PYkvHDfCfK3cXXoLpRIcbScQgZWF1ZS92M\nL+ysb1KLJjKE6rygJWondDntQRRadVV4pKpFQIMxLWeOOZuGjFrtV8grN/igeui+\ny+RLeWHhVklbFy00badko2a79wt55Mbdpf7QOMjNfk8vdhDSqDr9xXvxnFO3H8C/\nEwGqZtKBAoGBAPEfuXPMTqwJnwtdCyaG/HsVNnLhRvOEi6tG7hdyIZns/7cNdf4b\nw9lyldRSWYxWa2+JhHk3GkHZ/zm8e3PLVBixJSmplNFF1lnJ9Ua1KMrQr6DDIYeu\nopmHBARjQzmqdkptPMm1XmKskhDWW3kG0OaJCtYuqniZPKr+s0GnEY+TAoGBANI4\n4ctBShIl/qxdzXY7R4JbLDiJfXchNXOCFiar25EJ0wF+EcBN/kWkGxUHsSXJTuZS\nDxeRbCenjyFraZvALT7d/SQAXuSnKHMj4nNi5Dne5nys5mIxPQCTPsAnsdivEr4Y\notbPJtigCZGvmb8m0X3KXJMGgL/IjmgBO4NAXHfBAoGAdLrl/eBHqdOSkl692i35\nJuDzFhRnU9a0Avv4oZMYHztz5Dt5BsaMPsXRMROY1G613i5V2pTJel29yEauATXL\nBZUkp5G4QMOtbJRv0IY9NEpo68vrfJMXlnmdgT5Iui8Cyy8tAX1zLxPaNpp/eAzL\nrv9jJqM61d1hWGqrfzoRL08CgYEAjfZtvJXFkIXp+rqJwnr7qnMpZLCscqORdgR2\nMv1BlCt/O/XHxQamXJ8aztPX9D2Cs5Tcrjy9PfYweRARTGW16Et+5C3/5wdfEIo+\nJU+18cKuAam9FrhLirTk3lywBDUOQiNe7BMOF6HTdHeSKvzq4bijVYJBtcGZMXEL\noHH5P8ECgYEAkFHNMnxZO56PeKJdBmk2dIGeXejbOMv9Pxbgx3KbUbvojtgkuzLp\ns/FzNNbEVIAOQ8px8Pz7A/cNPN9VrL6roA1tg5ffdSLumekCyQ3tUXAVaSKVfQX5\n3UyyO6sd4UNT1XBcrshumFbnYSKmgq1djwze2oIc1wNyksj2VuFc2os=\n-----END RSA PRIVATE KEY-----",
           }]

def get_compute_client(config):
    LOG.info("Get Compute Client")
    return oci.core.ComputeClient(config)


def get_vnic_attachment(config, compartment_id, instance_id):
    LOG.info("[START] Get VNIC attachment")
    LOG.info("Instance ID: '{0}'".format(instance_id))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    compute_client = get_compute_client(config)
    vnic_attachments = compute_client.list_vnic_attachments(compartment_id,
                                                            instance_id = instance_id)
    vnic_attachment = vnic_attachments.data[0]
    LOG.info(vnic_attachment)
    LOG.info("[END] Get VNIC attachment")
    return vnic_attachment


def get_instance(config, compartment_id, instance_name, status = None):
    LOG.info("[START] Get Compute Instance")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    compute_client = get_compute_client(config)
    instances = compute_client.list_instances(compartment_id).data
    instance = None
    for instance_aux in instances:
        if instance_name == instance_aux.display_name:
            if status == None or instance_aux.lifecycle_state == status:
                LOG.info("Instance found")
                instance = instance_aux
    LOG.info(instance)
    LOG.info("[END] Get Compute Instance")
    return instance


def get_image(config, os_name, os_version, shape, compartment_id = None):
    LOG.info("[START] Get Image '{0}' ver '{1}'".format(os_name, os_version))
    LOG.info("Shape: '{0}'".format(shape))
    compute_client = get_compute_client(config)
    if compartment_id == None:
        compartment_id = config["tenancy"]
    LOG.info("Compartment ID: '{0}'".format(compartment_id))
    images_response = oci.pagination.list_call_get_all_results(
        compute_client.list_images,
        compartment_id,
        operating_system = os_name,
        operating_system_version = os_version,
        shape=shape
    ).data
    image = images_response[0]
    for image_aux in images_response:
        if "admin" not in image_aux.display_name:
            image = image_aux
            break
    LOG.info(image)
    LOG.info("[END] Get Image")
    return image

def get_initial_credentials(config,instance_name):
    LOG.info("[Start] Get Windows instance initial credentials")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    compute_client = get_compute_client(config)
    compute_instance = get_instance(config,
                                    config["tenancy"],
                                    instance_name,
                                    status = 'RUNNING'
                                    )
    instance_credentials=compute_client.get_windows_instance_initial_credentials(compute_instance.id)
    LOG.info(instance_credentials.data)
    LOG.info("[End] Get Windows instance initial credentials")
    return instance_credentials.data


def create_instance(config, instance_cfg, fake_instance = False):
    LOG.info("[START] Create Compute Instance")
    LOG.info(instance_cfg)
    compute_client = get_compute_client(config)

    admin_config = config
    if "admin_cfg" in instance_cfg:
        LOG.info("Running with Admin config")
        admin_config = instance_cfg["admin_cfg"]
        LOG.info(admin_config)
    if "vcn_compartment" not in instance_cfg:
        LOG.info("Getting VCN from same compartment instance")
        instance_cfg["vcn_compartment"] = instance_cfg["compartment"]

    LOG.info("Create VNIC Details")
    vcn_instance = vcn.get_vcn(admin_config,
                               instance_cfg["vcn_compartment"],
                               instance_cfg["vcn"])

    subnet = vcn.get_subnet(admin_config,
                            instance_cfg["vcn_compartment"],
                            vcn_instance.id,
                            subnet_name_pattern = "",
                            ad = instance_cfg["availability_domain"])

    LOG.info("Get Image details")
    image = get_image(admin_config,
                      instance_cfg["os_name"], 
                      instance_cfg["os_version"], 
                      instance_cfg["shape"],
                      compartment_id = instance_cfg["compartment"])

    LOG.info("Set SSH key in metadata")
    ssh_key_file = "{0}.pub".format(instance_cfg["ssh_key"])
    ssh_key = open(ssh_key_file, "r").read()
    instance_metadata = {"ssh_authorized_keys": ssh_key}
    ad = identity.get_availability_domain(admin_config, instance_cfg["availability_domain"])

    LOG.info("Launch Instance")
    instance_ocid = None
    if fake_instance:
        LOG.info("Create Fake Compute Instance")
        instance_ocid = instance_launcher_fake(config, instance_cfg, subnet,
                                               image, ad, instance_metadata)
    else:
        LOG.info("Create Real Compute Instance")
        instance_ocid = instance_launcher_real(config, instance_cfg, subnet,
                                               image, ad, instance_metadata)
    
    LOG.info("Wait the operation to complete")
    instance_response = oci.wait_until(
        compute_client,
        compute_client.get_instance(instance_ocid),
        "lifecycle_state",
        "RUNNING"
    )

    LOG.info("Operation completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Create Compute Instance")
    return instance_response.data

def instance_launcher_real(config, instance_cfg, subnet, image, ad, instance_metadata):
    LOG.info("[Start] Real Instance Launcher")
    compute_client = get_compute_client(config)
    LOG.info("Get VNIC, Image and Instance Details")
    vnic_details = oci.core.models.CreateVnicDetails(subnet_id = subnet.id)
    image_details = oci.core.models.InstanceSourceViaImageDetails(image_id = image.id)
    instance_details = oci.core.models.LaunchInstanceDetails(
        display_name = instance_cfg["display_name"],
        compartment_id = instance_cfg["compartment"],
        availability_domain = ad.name,
        shape = instance_cfg["shape"],
        source_details = image_details,
        create_vnic_details = vnic_details,
        metadata = instance_metadata
    )
    LOG.info("Request Sent")
    instance_response = compute_client.launch_instance(instance_details)
    LOG.info(instance_response.data)
    instance_ocid = instance_response.data.id
    LOG.info("[End] Real Instance Launcher")
    return instance_ocid


def instance_launcher_fake(config, instance_cfg, subnet, image, ad, instance_metadata):
    LOG.info("[Start] Fake  Instance Launcher")
    LOG.info("Get VNIC, Image and Instance Details")
    test_details = dict(TEST_DETAILS)
    accounts = list(ACCOUNTS)
    accounts[0]["tenancy_ocid"] = config["tenancy"]
    accounts[0]["user_ocid"] = config["user"]
    instance_details = { "displayName" : instance_cfg["display_name"],
                         "compartmentId" : instance_cfg["compartment"],
                         "availabilityDomain" : ad.name,
                         "shape" : instance_cfg["shape"],
                         "sourceDetails" : { "sourceType": "image",
                                             "imageId": image.id
                                           },
                         "createVnicDetails" : {"subnetId": subnet.id},
                         "metadata" : instance_metadata
                       }
    test_details["body"] = instance_details
    LOG.info("Request Sent")
    instance_response = rest_wrapper.api_request(test_details, ENDPOINTS, accounts)["body"]
    LOG.info(instance_response)
    instance_ocid = instance_response["id"]
    LOG.info("[End] Fake Instance Launcher")
    return instance_ocid


def terminate_instante(config, instance_cfg):
    LOG.info("[START] Terminate Compute Instance")
    LOG.info(instance_cfg)
    compute_client = get_compute_client(config)

    LOG.info("Get Instance details")
    compute_instance = get_instance(config,
                                    instance_cfg["compartment"],
                                    instance_cfg["display_name"],
                                    status = 'RUNNING'
                                    )

    LOG.info("Terminate instance")
    compute_client.terminate_instance(compute_instance.id)

    instance_response = oci.wait_until(
        compute_client,
        compute_client.get_instance(compute_instance.id),
        "lifecycle_state",
        "TERMINATED",
        succeed_on_not_found=True
    )

    LOG.info("Operation completed")
    LOG.info(instance_response.data)
    LOG.info("[END] Terminate Compute Instance")
    return instance_response.data


def list_instance(config, instance_cfg):
    LOG.info("[START] List Compute Instance")
    LOG.info(instance_cfg)
    LOG.info("Get Instance details")
    get_instance(config,
                 config["tenancy"],
                 instance_cfg["display_name"],
                 status = 'RUNNING'
                 )
    LOG.info("[END] List Compute Instance")


def get_instance_connections(config, instance_ocid, compartment_ocid):
    LOG.info("[START] Get Instance Connection")
    LOG.info("Instance: '{0}'".format(instance_ocid))
    LOG.info("Compartment: '{0}'".format(compartment_ocid))
    compute_client = get_compute_client(config)
    conections = compute_client.list_instance_console_connections(compartment_ocid,
                                                                  instance_id=instance_ocid).data
    connection_out = None
    for conection in conections:
        if "ACTIVE" in conection.lifecycle_state:
            connection_out = conection
    LOG.info(connection_out)
    LOG.info("[END] Get Instance Connection")
    return connection_out


def remove_instance_connection(config, instance_ocid, compartment_ocid):
    LOG.info("[START] Remove Instance Connection")
    LOG.info("Instance: '{0}'".format(instance_ocid))
    LOG.info("Compartment: '{0}'".format(compartment_ocid))
    compute_client = get_compute_client(config)
    connection = get_instance_connections(config, instance_ocid, compartment_ocid)
    if connection:
        LOG.info("Deleting instance...")
        compute_client.delete_instance_console_connection(connection.id)
        LOG.info("Completed")
    LOG.info("[END] Remove Instance Connection")
    return 


def create_instance_conection(config, instance_cfg):
    LOG.info("[START] Create Instance Connection")
    LOG.info(instance_cfg)
    compute_client = get_compute_client(config)
    remove_instance_connection(config, instance_cfg["ocid"], instance_cfg["compartment"])
    time.sleep(60)
    LOG.info("Reading the public Key")
    ssh_key = instance_cfg["ssh_key"]
    conection_details = oci.core.models.CreateInstanceConsoleConnectionDetails(
         instance_id = instance_cfg["ocid"],
         public_key = ssh_key
        )
    connection_response = compute_client.create_instance_console_connection(conection_details).data
    LOG.info(connection_response)
    LOG.info("=================================================")
    LOG.info("Your connection string is: \n{}".format(connection_response.connection_string))
    LOG.info("=================================================")
    LOG.info("[END] Create Instance Connection")


def get_instance_public_ip(config, compartment_ocid, instance_name):
    LOG.info("[Start] Get Public IP")
    LOG.info("Instance Name: '{0}'".format(instance_name))
    LOG.info("Compartment: '{0}'".format(compartment_ocid))
    instance = get_instance(config, compartment_ocid, instance_name, status = 'RUNNING')
    vnic_attachment = get_vnic_attachment(config, compartment_ocid, instance.id)
    ip_address = vcn.get_vnic(config, vnic_attachment.vnic_id).public_ip
    LOG.info("Public IP address: '{0}'".format(ip_address))
    LOG.info("[END] Get Public IP")
    return ip_address

def connect_windows_instance(config,instance_cfg):
    LOG.info("[Start] connect to Windows instance")
    LOG.info("Instance Name: '{0}'".format(instance_cfg["display_name"]))
    host_ip=get_instance_public_ip(config, 
                                config["tenancy"],
                                instance_cfg["display_name"])
    instance_credentials=get_initial_credentials(config, instance_cfg["display_name"])

    win_username=instance_credentials.username
    win_password=instance_credentials.password

    LOG.info("Windows ip : '{0}' , username : '{1}, password : '{2}'".format(host_ip,win_username,win_password))
    connect_instance=wmi.WmiClientWrapper(host = host_ip,
                                              username = win_username,
                                              password = win_password
                                              )
    output=connect_instance.query("select caption, name from win32_process")
    LOG.info(output)
    return output

#update compute instance with tag
def update_instance_tag(config,compartment_id,instance_name,defined_tags,freeform_tags):
    LOG.info("[START] apply defined tags '{0}' for '{1}' ".format(defined_tags,instance_name))
    LOG.info("[START] apply freeform tags : '{0}' for '{1}' ".format(freeform_tags,instance_name))
    compute_client = get_compute_client(config)
    
    instance = get_instance(config, compartment_id, instance_name, status = 'RUNNING')
    update_details = oci.core.models.UpdateInstanceDetails (
        defined_tags = defined_tags,
        freeform_tags = freeform_tags,
        display_name = "Apply tags for compute instance"
        )
    new_instance = compute_client.update_instance(
        instance_id = instance.id,
        update_instance_details = update_details
        )
    LOG.info("[END] apply tags for compute instance")
    LOG.info(new_instance)
    return new_instance
