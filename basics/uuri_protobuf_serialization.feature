#
# Copyright (c) 2025 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0
#
# SPDX-FileType: SOURCE
# SPDX-License-Identifier: Apache-2.0
#
Feature: Efficient binary encoding of endpoint identifiers (UUri)

  Scenario Outline:
    Developers using a uProtocol language library should be able to get the binary
    encoding of a UUri instance as specified by the UUri proto3 definition file.

    [utest->req~uri-data-model-proto~1]

    The byte sequences representing the Protocol Buffer encodings have been created
    using https://www.protobufpal.com/ based on the UUri proto3 definition from the
    uProtocol specification.

    Note that comparing the serialized Protobuf to the byte sequence is not feasible
    due to the fact that Proto serialization is not intended/designed to be canonical,
    as outlined here: https://protobuf.dev/programming-guides/serialization-not-canonical/

    Given a UUri having authority <authority_name>
    And having entity type <type_id>
    And having entity instance <instance_id>
    And having major version <version>
    And having resource identifier <resource_id>
    When serializing the UUri to its protobuf wire format
    Then the original UUri can be recreated from the protobuf wire format
    And the same UUri can be deserialized from <byte_sequence>

    Examples:
      | authority_name | type_id | instance_id | version | resource_id |byte_sequence                                  |
      | ""             |  0x0001 |      0x0001 |    0x01 |      0xa1fb |                                 0a0010011801200128fbc302 |
      | "my_vin"       |  0x0001 |      0x1000 |    0x02 |      0x001a |             0a066d795f76696e10011880202002281a |
      | "*"            |  0x0101 |      0x0001 |    0xa0 |      0xa1fb |                       0a012a108102180120a00128fbc302 |
      | "mcu1"         |  0xFFFF |      0x0001 |    0x01 |      0xa1fb |                 0a046d63753110ffff031801200128fbc302 |
      | "vcu.my_vin"   |  0x0101 |      0x01a4 |    0x01 |      0x8000 |   0a0a7663752e6d795f76696e10810218a403200128808002 |
      | "vcu.my_vin"   |  0x0101 |      0xFFFF |    0x01 |      0xa1fb | 0a0a7663752e6d795f76696e10810218ffff03200128fbc302 |
      | "vcu.my_vin"   |  0xFFFF |      0xFFFF |    0x01 |      0xa1fb | 0a0a7663752e6d795f76696e10ffff0318ffff03200128fbc302 |
      | "vcu.my_vin"   |  0x0101 |      0x0001 |    0x00 |      0xa1fb |           0a0a7663752e6d795f76696e1081021801200028fbc302 |
      | "vcu.my_vin"   |  0x0101 |      0x0001 |    0xFF |      0xa1fb |     0a0a7663752e6d795f76696e108102180120ff0128fbc302 |
      | "vcu.my_vin"   |  0x0101 |      0x0001 |    0x01 |      0x0000 |               0a0a7663752e6d795f76696e108102180120012800 |
      | "vcu.my_vin"   |  0x0101 |      0x0001 |    0x01 |      0xFFFF |       0a0a7663752e6d795f76696e1081021801200128ffff03 |
