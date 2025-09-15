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
Feature: Matching endpoint identifiers (UUri) against patterns

  Scenario Outline:
    Developers using a uProtocol language library should be able to verify that a specific
    endpoint identifier matches a given pattern as specified by the UUri specification.

   [utest->dsn~uri-pattern-matching~2]

    Given a URI string <uri>
    When deserializing the URI to a UUri
    Then the UUri matches pattern <pattern>

    Examples:
      | uri                           | pattern               |
      | "/1/1/A1FB"                   | /1/1/A1FB             |
      | "/1/1/A1FB"                   | //*/1/1/A1FB          |
      | "/1/1/A1FB"                   | /FFFF/1/A1FB          |
      | "/1/1/A1FB"                   | //*/FFFF/1/A1FB       |
      | "/1/1/A1FB"                   | /FFFFFFFF/1/A1FB      |
      | "/1/1/A1FB"                   | //*/FFFFFFFF/1/A1FB   |
      | "/1/1/A1FB"                   | /1/FF/A1FB            |
      | "/1/1/A1FB"                   | //*/1/FF/A1FB         |
      | "/1/1/A1FB"                   | /1/1/FFFF             |
      | "/1/1/A1FB"                   | //*/1/1/FFFF          |
      | "/1/1/A1FB"                   | /FFFFFFFF/FF/FFFF     |
      | "/1/1/A1FB"                   | //*/FFFFFFFF/FF/FFFF  |
      | "/10001/1/A1FB"               | /10001/1/A1FB         |
      | "/10001/1/A1FB"               | //*/10001/1/A1FB      |
      | "/10001/1/A1FB"               | /FFFFFFFF/1/A1FB      |
      | "/10001/1/A1FB"               | //*/FFFFFFFF/1/A1FB   |
      | "/10001/1/A1FB"               | /FFFFFFFF/FF/FFFF     |
      | "/10001/1/A1FB"               | //*/FFFFFFFF/FF/FFFF  |
      | "//vcu.my_vin/1/1/A1FB"       | //vcu.my_vin/1/1/A1FB |
      | "//vcu.my_vin/1/1/A1FB"       | //*/1/1/A1FB          |

  Scenario Outline:
    Developers using a uProtocol language library should be able to verify that a specific
    endpoint identifier does not match a given pattern as specified by the UUri specification.

    [utest->dsn~uri-pattern-matching~2]

    Given a URI string <uri>
    When deserializing the URI to a UUri
    Then the UUri does not match pattern <pattern>

    Examples:
      | uri                     | pattern               |
      | "/1/1/A1FB"             | //mcu1/1/1/A1FB       |
      | "//vcu.my_vin/1/1/A1FB" | //mcu1/1/1/A1FB       |
      | "//vcu/B1A5/1/A1FB"     | //vc/FFFFFFFF/FF/FFFF |
      | "/B1A5/1/A1FB"          | //*/25B1/FF/FFFF      |
      | "/B1A5/1/A1FB"          | //*/FFFFFFFF/2/FFFF   |
      | "/B1A5/1/A1FB"          | //*/FFFFFFFF/FF/ABCD  |
      | "/B1A5/1/A1FB"          | /25B1/1/A1FB          |
      | "/B1A5/1/A1FB"          | /2B1A5/1/A1FB         |
      | "/10B1A5/1/A1FB"        | /40B1A5/1/A1FB        |
      | "/B1A5/1/A1FB"          | /B1A5/4/A1FB          |
      | "/B1A5/1/A1FB"          | /B1A5/1/90FB          |
