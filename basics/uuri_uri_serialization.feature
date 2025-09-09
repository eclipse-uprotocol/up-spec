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
Feature: String representation of endpoint identfiers (UUri)

  Scenario Outline:
    Developers using a uProtocol language library should be able to get the URI
    string representation of a UUri instance as specified by the UUri specification.

    [utest->req~uri-serialization~1]
    [utest->dsn~uri-scheme~1]
    [utest->dsn~uri-host-only~2]
    [utest->dsn~uri-authority-mapping~1]
    [utest->dsn~uri-path-mapping~1]

    Given a UUri having authority <authority_name>
    And having entity identifier <entity_id>
    And having major version <version>
    And having resource identifier <resource_id>
    When serializing the UUri to a URI
    Then the resulting URI string is <uri_string>
    And the original UUri can be recreated from the URI string

    Examples:
      | authority_name | entity_id  | version | resource_id | uri_string                      |
      | ""             | 0x00000001 |    0x01 |      0xa1fb | up:/1/1/A1FB                    |
      | "my_vin"       | 0x10000001 |    0x02 |      0x001a | up://my_vin/10000001/2/1A       |
      | "*"            | 0x00000101 |    0xa0 |      0xa1fb | up://*/101/A0/A1FB              |
      | "mcu1"         | 0x0000FFFF |    0x01 |      0xa1fb | up://mcu1/FFFF/1/A1FB           |
      | "vcu.my_vin"   | 0x01a40101 |    0x01 |      0x8000 | up://vcu.my_vin/1A40101/1/8000  |
      | "vcu.my_vin"   | 0xFFFF0101 |    0x01 |      0xa1fb | up://vcu.my_vin/FFFF0101/1/A1FB |
      | "vcu.my_vin"   | 0xFFFFFFFF |    0x01 |      0xa1fb | up://vcu.my_vin/FFFFFFFF/1/A1FB |
      | "vcu.my_vin"   | 0x00000101 |    0x00 |      0xa1fb | up://vcu.my_vin/101/0/A1FB      |
      | "vcu.my_vin"   | 0x00000101 |    0xFF |      0xa1fb | up://vcu.my_vin/101/FF/A1FB     |
      | "vcu.my_vin"   | 0x00000101 |    0x01 |      0x0000 | up://vcu.my_vin/101/1/0         |
      | "vcu.my_vin"   | 0x00000101 |    0x01 |      0xFFFF | up://vcu.my_vin/101/1/FFFF      |

  Scenario Outline:
    Developers using a uProtocol language library should not be able to create a UUri from a
    URI string that does not comply with the UUri specification.

    [utest->req~uri-serialization~1]
    [utest->dsn~uri-scheme~1]
    [utest->dsn~uri-host-only~2]
    [utest->dsn~uri-authority-mapping~1]
    [utest->dsn~uri-path-mapping~1]

    Given a URI string <uri_string>
    When deserializing the URI to a UUri
    Then the attempt fails

    Examples:
      | uri_string                            | reason for failure                                    |
      | ""                                    | not a URI                                             |
      | "  "                                  | not a URI                                             |
      | "$$"                                  | not a URI                                             |
      | "up:"                                 | not a URI                                             |
      | "up:/"                                | not a URI                                             |
      | "/"                                   | not a URI                                             |
      | "//"                                  | not a URI                                             |
      | "//vcu.my_vin"                        | just an authority                                     |
      | "//VCU"                               | authority with uppercase characters                   |
      | "////1/A1FB"                          | missing authority and entity                          |
      | "/////A1FB"                           | missing authority, entity and version                 |
      | "xy://vcu.my_vin/101/1/A"             | unsupported schema                                    |
      | "//vcu.my_vin/101/1/A?foo=bar"        | URI with query                                        |
      | "//vcu.my_vin/101/1/A#foo"            | URI with fragment                                     |
      | "//vcu.my-vin:1516/101/1/A"           | server-based authority with port                      |
      | "//user:pwd@vcu.my-vin/101/1/A"       | server-based authority with user info                 |
      | "//reg_based:1516/101/1/A"            | registry-based authority name with invalid characters |
      | "up://vcu.my-vin/1G1/1/A1FB"          | non-hex entity ID                                     |
      | "/123456789/1/A1FB"                   | entity ID exceeds max length                          |
      | "up:/101/G/A1FB"                      | non-hex version                                       |
      | "//vcu.my-vin/101/123/A1FB"           | version exceeds max length                            |
      | "/101/1/G1FB"                         | non-hex resource ID                                   |
      | "up://vcu.my-vin/101/1/12345"         | resource ID exceeds max length                        |
