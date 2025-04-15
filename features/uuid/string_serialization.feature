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
Feature: String representation of uProtocol UUIDs

  Scenario Outline:
    Developers using a uProtocol language library should be able to get the
    string representation of a UUID instance as specified by uProtocol's UUID
    specification.

    [utest->dsn~uuid-spec~1]

    Given a UUID having MSB <uuid_msb> and LSB <uuid_lsb>
    When serializing the UUID to a hyphenated string
    Then the resulting hyphenated string is <hyphenated_string>
    And the original UUID can be recreated from the hyphenated string

    Examples:
      | uuid_msb           | uuid_lsb           | hyphenated_string                    |
      | 0x0000000000017000 | 0x8010101010101a1a | 00000000-0001-7000-8010-101010101a1a |

  Scenario Outline:
    Developers using a uProtocol language library should not be able to create a UUID from a
    hyphenated string that does not comply with uProtocol's UUID specification.

    In particular, it should not be possible to create UUIDs having the wrong version
    or variant identifier.

    [utest->dsn~uuid-spec~1]

    Given a UUID string representation <uuid_string>
    When deserializing the hyphenated string to a UUID
    Then the attempt fails

    Examples:
      | uuid_string                          | reason for failure     |
      | 00000000-0001-0000-8000-0000000000ab | wrong version (0b0000) |
      | 00000000-0001-1000-8000-0000000000ab | wrong version (0b0001) |
      | 00000000-0001-2000-8000-0000000000ab | wrong version (0b0010) |
      | 00000000-0001-3000-8000-0000000000ab | wrong version (0b0011) |
      | 00000000-0001-4000-8000-0000000000ab | wrong version (0b0100) |
      | 00000000-0001-5000-8000-0000000000ab | wrong version (0b0101) |
      | 00000000-0001-6000-8000-0000000000ab | wrong version (0b0110) |
      | 00000000-0001-8000-8000-0000000000ab | wrong version (0b1000) |
      | 00000000-0001-9000-8000-0000000000ab | wrong version (0b1001) |
      | 00000000-0001-a000-8000-0000000000ab | wrong version (0b1010) |
      | 00000000-0001-b000-8000-0000000000ab | wrong version (0b1011) |
      | 00000000-0001-c000-8000-0000000000ab | wrong version (0b1100) |
      | 00000000-0001-d000-8000-0000000000ab | wrong version (0b1101) |
      | 00000000-0001-e000-8000-0000000000ab | wrong version (0b1110) |
      | 00000000-0001-f000-8000-0000000000ab | wrong version (0b1111) |
      | 00000000-0001-7000-0000-0000000000ab | wrong variant (0b00)   |
      | 00000000-0001-7000-4000-0000000000ab | wrong variant (0b01)   |
      | 00000000-0001-7000-c000-0000000000ab | wrong variant (0b11)   |
